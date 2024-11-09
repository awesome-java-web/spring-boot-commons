package com.github.springframework.boot.commons.retry;

import java.util.Random;

public class ExponentialRandomIntervalRetryHandler extends ExponentialIntervalRetryHandler {

	private final Random random = new Random();

	public static ExponentialRandomIntervalRetryHandler newHandler() {
		return new ExponentialRandomIntervalRetryHandler();
	}

	public ExponentialRandomIntervalRetryHandler() {
		super();
	}

	@Override
	public long getInterval() {
		final int multiplier = 1 << attempts.get();
		final long interval = initialInterval * multiplier;
		final long randomInterval = Math.min(random.nextLong(), interval);
		return Math.min(randomInterval, maxInterval);
	}

}
