package com.github.springframework.boot.commons.retry;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedTimeIntervalRetryPolicy extends AbstractRetryPolicy {

    private static final long DEFAULT_TIME_INTERVAL_MILLISECONDS = 3000;

    private final AtomicInteger attempts = new AtomicInteger(0);

    private final long timeIntervalMilliseconds;

    public FixedTimeIntervalRetryPolicy(long timeIntervalMilliseconds) {
        this.timeIntervalMilliseconds = timeIntervalMilliseconds;
    }

    public FixedTimeIntervalRetryPolicy() {
        this(DEFAULT_TIME_INTERVAL_MILLISECONDS);
    }

    @Override
    public void retry(FunctionalRetry functionalRetry) {
        while (attempts.get() < maxAttempts) {
            try {
                TimeUnit.MILLISECONDS.sleep(timeIntervalMilliseconds);
                functionalRetry.apply();
                break;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                attempts.getAndIncrement();
            }
        }
    }

}
