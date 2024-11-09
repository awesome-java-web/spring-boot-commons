package com.github.springframework.boot.commons.retry;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class FixedRandomIntervalRetryHandler extends AbstractRetryHandler {

	private static final long DEFAULT_MAX_INTERVAL = TimeUnit.SECONDS.toMillis(30);

	private final Random random = new Random();

	private long maxInterval;

	public static FixedRandomIntervalRetryHandler newHandler() {
		return new FixedRandomIntervalRetryHandler();
	}

	public FixedRandomIntervalRetryHandler withMaxInterval(long maxInterval) {
		this.maxInterval = maxInterval;
		return this;
	}

	public FixedRandomIntervalRetryHandler withMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
		return this;
	}

	public FixedRandomIntervalRetryHandler() {
		this.maxInterval = DEFAULT_MAX_INTERVAL;
	}

	@Override
	public long getInterval() {
		return Math.min(random.nextLong(), maxInterval);
	}

}
