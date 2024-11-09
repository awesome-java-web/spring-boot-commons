package com.github.springframework.boot.commons.retry;

import java.util.concurrent.TimeUnit;

public class FixedIntervalRetryHandler extends AbstractRetryHandler {

	private static final long DEFAULT_INTERVAL = TimeUnit.SECONDS.toMillis(3);

	private long interval;

	public static FixedIntervalRetryHandler newHandler() {
		return new FixedIntervalRetryHandler();
	}

	public FixedIntervalRetryHandler withInterval(long interval) {
		this.interval = interval;
		return this;
	}

	public FixedIntervalRetryHandler withMaxAttempts(int maxAttempts) {
		this.maxAttempts = maxAttempts;
		return this;
	}

	FixedIntervalRetryHandler() {
		this.interval = DEFAULT_INTERVAL;
	}

	@Override
	public long getInterval() {
		return interval;
	}

}
