package com.github.springframework.boot.commons.mail;

import com.github.springframework.boot.commons.retry.RetryPolicy;

public interface EmailSender {

	void send(Email email);

	void setRetryPolicy(RetryPolicy retryPolicy);

}
