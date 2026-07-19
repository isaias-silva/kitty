package org.zack.kitty.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class BaseNode {

	protected BaseNode() {
	}


	public void inject(Object prop) throws IllegalAccessException {
		Field[] fields = getClass().getDeclaredFields();

		for (Field field : fields) {
			if (field.getType().isAssignableFrom(prop.getClass())) {
				boolean isAccessible = field.canAccess(this);

				field.setAccessible(true);
				field.set(this, prop);
				field.setAccessible(isAccessible);

			}
		}
	}


	public void initMethod(Method method) throws InvocationTargetException, IllegalAccessException {
		boolean isAccessible = method.canAccess(this);

		method.setAccessible(true);
		method.invoke(this);
		method.setAccessible(isAccessible);
	}

}