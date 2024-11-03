package com.github.springframework.boot.commons.retry;

public interface RetryHandler {

	void retry(RetryCallback callback);

}
