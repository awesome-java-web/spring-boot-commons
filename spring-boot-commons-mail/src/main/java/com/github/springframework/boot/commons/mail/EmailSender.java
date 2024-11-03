package com.github.springframework.boot.commons.mail;

import com.github.springframework.boot.commons.retry.RetryHandler;

public interface EmailSender {

	boolean send(Email email);

	void setRetryHandler(RetryHandler retryHandler);

}
