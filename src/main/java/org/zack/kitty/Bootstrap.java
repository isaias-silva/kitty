package org.zack.kitty;

import java.util.concurrent.ExecutorService;

import org.zack.kitty.core.ContextManager;
import org.zack.kitty.core.ExecutorsManager;
import javafx.application.Application;

public class Bootstrap {

	public static void main(String... args) {

		final ExecutorService executor = ExecutorsManager.INSTANCE.getExecutor();

		executor.submit(ContextManager.Context::init);

		try {
			executor.submit(()-> {
				try {
					Application.launch(AppView.class);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			});

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}