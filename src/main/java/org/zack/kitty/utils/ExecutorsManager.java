package org.zack.kitty.utils;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorsManager {

	public static final ExecutorsManager INSTANCE = new ExecutorsManager();

	private final ExecutorService executorService;


	public ExecutorsManager() {

		executorService = Executors.newFixedThreadPool(2, r -> {
			Thread t = new Thread(r);
			t.setName("k-pool-thread-" + UUID.randomUUID());
			t.setDaemon(false);
			return t;
		});
	}


	public ExecutorService getExecutor() {
		return executorService;
	}


	public void shutdown() {

		executorService.shutdown();

	}

}