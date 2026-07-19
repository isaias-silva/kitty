package org.zack.kitty.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zack.kitty.Bootstrap;
import org.zack.kitty.core.annotations.InjectNode;
import org.zack.kitty.core.annotations.Node;
import org.zack.kitty.core.annotations.OnCreate;

public class ContextManager {

	private final Set<BaseNode> nodes = new HashSet<>();

	private static final Logger LOGGER = LoggerFactory.getLogger(ContextManager.class);

	private final Reflections reflections;

	public static final ContextManager Context;

	static {
		Context = new ContextManager(new Reflections(Bootstrap.class));
	}

	public ContextManager(Reflections rootReflection) {
		this.reflections = rootReflection;
	}


	public void init() {

		Set<Class<?>> reflectionsTypesAnnotatedWith = reflections.getTypesAnnotatedWith(Node.class);

		reflectionsTypesAnnotatedWith.forEach(s -> {
			try {
				BaseNode node = (BaseNode) s.getDeclaredConstructor().newInstance();
				this.nodes.add(node);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});

		for (BaseNode node : this.nodes) {
			try {
				injectNodes(node);
				initNode(node);

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		this.nodes.forEach(n -> LOGGER.info("declared node {}", n.getClass().getName()));

	}


	private void injectNodes(final BaseNode node)
		throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {

		Field[] fields = node.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (field.getAnnotation(InjectNode.class) != null) {
				Optional<BaseNode> exists = this.nodes.stream().filter(i -> i.getClass().equals(field.getType()))
					.findFirst();

				BaseNode connectInstance = exists.orElse(
					(BaseNode) field.getType().getDeclaredConstructor().newInstance());

				node.inject(connectInstance);

				LOGGER.info("inject Node {} in {}", connectInstance.getClass().getName(), node.getClass().getName());

				if (exists.isEmpty())
					this.nodes.add(connectInstance);
			}
		}
	}


	private void initNode(BaseNode objectNode) throws InvocationTargetException, IllegalAccessException {
		Set<Method> initMethods = Arrays.stream(objectNode.getClass().getDeclaredMethods())
			.filter(m -> m.getAnnotation(OnCreate.class) != null).filter(m -> m.getParameterCount() == 0)
			.collect(Collectors.toSet());

		for (Method method : initMethods) {
			objectNode.initMethod(method);
		}
	}


	public <T> T getNode(Class<T> clazz) throws Exception {

		try {
			Object node = nodes.stream().filter(n -> n.getClass().equals(clazz)).findFirst().orElseThrow();

			return clazz.cast(node);

		} catch (Exception e) {
			throw new NoSuchElementException();
		}
	}
}