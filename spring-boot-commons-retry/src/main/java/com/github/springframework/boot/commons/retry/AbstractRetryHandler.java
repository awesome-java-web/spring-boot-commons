package com.github.springframework.boot.commons.retry;

import java.util.concurrent.TimeUnit;

public abstract class AbstractRetryHandler implements RetryHandler {

	protected static final int DEFAULT_MAX_ATTEMPTS = 3;

	protected int maxAttempts = DEFAULT_MAX_ATTEMPTS;

	protected final ThreadLocal<Integer> attempts = ThreadLocal.withInitial(() -> 0);

	public abstract long getInterval();

	@Override
	public void retry(RetryCallback callback) {
		while (attempts.get() < maxAttempts) {
			final long interval = getInterval();
			sleepQuietly(interval);
			if (callback.execute()) {
				break;
			}
			attempts.set(attempts.get() + 1);
		}

		attempts.remove();

		if (attempts.get() == maxAttempts) {
			throw new RetryFailedException("Reached max attempts: " + maxAttempts);
		}
	}

	private void sleepQuietly(long milliseconds) {
		try {
			TimeUnit.MILLISECONDS.sleep(milliseconds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}
