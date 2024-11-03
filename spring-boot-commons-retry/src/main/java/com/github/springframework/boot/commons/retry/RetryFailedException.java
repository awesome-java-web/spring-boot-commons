package com.github.springframework.boot.commons.retry;

public class RetryFailedException extends RuntimeException {

	public RetryFailedException(String message) {
		super(message);
	}

}
