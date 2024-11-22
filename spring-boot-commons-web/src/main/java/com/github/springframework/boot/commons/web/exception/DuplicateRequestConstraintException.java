package com.github.springframework.boot.commons.web.exception;

public class DuplicateRequestConstraintException extends RuntimeException {

	public DuplicateRequestConstraintException(String message) {
		super(message);
	}

}
