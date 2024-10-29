package com.github.springframework.boot.commons.mail;

public class EmailSendException extends RuntimeException {

	public EmailSendException(Exception e) {
		super(e.getMessage(), e.getCause());
	}

}
