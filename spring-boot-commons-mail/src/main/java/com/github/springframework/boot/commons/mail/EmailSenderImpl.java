package com.github.springframework.boot.commons.mail;

import com.github.springframework.boot.commons.retry.RetryHandler;
import com.sun.mail.util.MailConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.Assert;

import javax.mail.internet.MimeMessage;

public class EmailSenderImpl implements EmailSender {

	private static final Logger logger = LoggerFactory.getLogger(EmailSenderImpl.class);

	@Autowired
	private JavaMailSender javaMailSender;

	private RetryHandler retryHandler;

	@Override
	public boolean send(Email email) {
		checkBeforeSend(email);
		try {
			MimeMessage originalMimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = email.toMimeMessageHelper(originalMimeMessage);
			javaMailSender.send(mimeMessageHelper.getMimeMessage());
			logger.info("Email [{}] sent to [{}] successfully.", email.getSubject(), email.getTo());
			return true;
		} catch (MailSendException e) {
			handleMailSendException(email, e);
			return false;
		} catch (Exception e) {
			throw new EmailSendException(e);
		}
	}

	@Override
	public void setRetryHandler(RetryHandler retryHandler) {
		this.retryHandler = retryHandler;
	}

	private void checkBeforeSend(Email email) {
		Assert.hasText(email.getFrom(), "You must specify the 'from' value for the email message.");
		Assert.hasText(email.getTo(), "You must specify the 'to' value for the email message.");
		Assert.hasText(email.getSubject(), "You must specify the 'subject' value for the email message.");
		Assert.hasText(email.getContent(), "You must specify the 'content' value for the email message.");
	}

	private void handleMailSendException(Email email, MailSendException exception) {
		Throwable cause = exception.getCause();
		if (cause instanceof MailConnectException) {
			logger.warn("Failed to send email [{}] to [{}] due to MailConnectException", email.getSubject(), email.getTo(), cause);
			if (retryHandler == null) {
				logger.warn("No retry handler set, the email will not be re-sent and MailConnectException will be thrown");
				throw new EmailSendException(exception);
			}
			logger.info("Starting to re-send this email with the retry handler: {}", retryHandler.getClass().getName());
			retryHandler.retry(() -> send(email));
		} else {
			throw new EmailSendException(exception);
		}
	}

}
