package com.github.springframework.boot.commons.retry;

public interface RetryPolicy {

    void retry(FunctionalRetry functionalRetry);

}
