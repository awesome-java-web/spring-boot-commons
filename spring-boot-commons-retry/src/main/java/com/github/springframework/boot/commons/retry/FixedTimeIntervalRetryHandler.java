package com.github.springframework.boot.commons.retry;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedTimeIntervalRetryHandler extends AbstractRetryHandler {

	private static final long DEFAULT_TIME_INTERVAL_MILLISECONDS = 3000;

	private final AtomicInteger attempts = new AtomicInteger(0);

	private final AtomicBoolean successful = new AtomicBoolean(false);

	private final long timeIntervalMilliseconds;

	public FixedTimeIntervalRetryHandler(long timeIntervalMilliseconds) {
		this.timeIntervalMilliseconds = timeIntervalMilliseconds;
	}

	public FixedTimeIntervalRetryHandler() {
		this(DEFAULT_TIME_INTERVAL_MILLISECONDS);
	}

	@Override
	public void retry(RetryCallback callback) {
		while (attempts.getAndIncrement() < maxAttempts && !successful.get()) {
			sleepQuietly(timeIntervalMilliseconds);
			successful.set(callback.execute());
		}

		if (attempts.get() == maxAttempts) {
			throw new RetryFailedException("Reached max attempts: " + maxAttempts);
		}
	}

}
