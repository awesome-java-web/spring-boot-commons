package com.github.springframework.boot.commons.retry;

import java.util.concurrent.TimeUnit;

public abstract class AbstractRetryHandler implements RetryHandler {

	private static final int DEFAULT_MAX_ATTEMPTS = 3;

	protected int maxAttempts = DEFAULT_MAX_ATTEMPTS;

	protected void sleepQuietly(long milliseconds) {
		try {
			TimeUnit.MILLISECONDS.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}
