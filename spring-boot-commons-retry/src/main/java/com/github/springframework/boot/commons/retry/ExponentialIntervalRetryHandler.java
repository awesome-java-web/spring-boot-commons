package com.github.springframework.boot.commons.retry;

import java.util.concurrent.TimeUnit;

public class ExponentialIntervalRetryHandler extends AbstractRetryHandler {

	private static final long DEFAULT_INITIAL_INTERVAL = TimeUnit.SECONDS.toMillis(3);

	private static final long DEFAULT_MAX_INTERVAL = TimeUnit.SECONDS.toMillis(30);

	protected long initialInterval;

	protected long maxInterval;

	public static ExponentialIntervalRetryHandler newHandler() {
		return new ExponentialIntervalRetryHandler();
	}

	public ExponentialIntervalRetryHandler withInitialInterval(long initialInterval) {
		this.initialInterval = initialInterval;
		return this;
	}

	public ExponentialIntervalRetryHandler withMaxInterval(long maxInterval) {
		this.maxInterval = maxInterval;
		return this;
	}

	ExponentialIntervalRetryHandler() {
		this.initialInterval = DEFAULT_INITIAL_INTERVAL;
		this.maxInterval = DEFAULT_MAX_INTERVAL;
	}

	@Override
	public long getInterval() {
		final int multiplier = 1 << attempts.get();
		return Math.min(initialInterval * multiplier, maxInterval);
	}

}
