package com.github.springframework.boot.commons.retry;

import java.util.concurrent.TimeUnit;

public class LinearIntervalRetryHandler extends AbstractRetryHandler {

	private static final long DEFAULT_INITIAL_INTERVAL = TimeUnit.SECONDS.toMillis(3);

	private static final long DEFAULT_INTERVAL_STEP = TimeUnit.SECONDS.toMillis(3);

	private static final long DEFAULT_MAX_INTERVAL = TimeUnit.SECONDS.toMillis(30);

	private long initialInterval;

	private long initialStep;

	private long maxInterval;

	public static LinearIntervalRetryHandler newHandler() {
		return new LinearIntervalRetryHandler();
	}

	public LinearIntervalRetryHandler withInitialInterval(long initialInterval) {
		this.initialInterval = initialInterval;
		return this;
	}

	public LinearIntervalRetryHandler withIntervalStep(long intervalStep) {
		this.initialStep = intervalStep;
		return this;
	}

	public LinearIntervalRetryHandler withMaxInterval(long maxInterval) {
		this.maxInterval = maxInterval;
		return this;
	}

	LinearIntervalRetryHandler() {
		this.initialInterval = DEFAULT_INITIAL_INTERVAL;
		this.initialStep = DEFAULT_INTERVAL_STEP;
		this.maxInterval = DEFAULT_MAX_INTERVAL;
	}

	@Override
	public long getInterval() {
		final long step = initialStep * attempts.get();
		return Math.min(initialInterval + step, maxInterval);
	}

}
