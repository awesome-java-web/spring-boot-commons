package com.github.springframework.boot.commons.retry;

public abstract class AbstractRetryPolicy implements RetryPolicy {

    protected int maxAttempts = 3;

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

}
