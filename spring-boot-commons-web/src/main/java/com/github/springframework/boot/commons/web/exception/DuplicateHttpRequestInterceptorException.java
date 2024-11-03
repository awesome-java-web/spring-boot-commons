package com.github.springframework.boot.commons.web.exception;

public class DuplicateHttpRequestInterceptorException extends RuntimeException {

	public DuplicateHttpRequestInterceptorException(String message) {
		super(message);
	}

}
