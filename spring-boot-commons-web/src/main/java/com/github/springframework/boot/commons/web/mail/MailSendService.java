package com.github.springframework.boot.commons.web.mail;

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

public class MailSendService {

    private static final Logger logger = LoggerFactory.getLogger(MailSendService.class);

    @Autowired
    private JavaMailSender javaMailSender;

    public boolean send(Mail mail) {
        checkBeforeSend(mail);
        try {
            MimeMessage originalMimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = mail.toMimeMessageHelper(originalMimeMessage);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            if (logger.isInfoEnabled()) {
                logger.info("The mail [{}] has been sent successfully to [{}]", mail.getSubject(), mail.getTo());
            }
            return true;
        } catch (MailSendException e) {
            handleMailSendException(mail, e);
            return false;
        } catch (Exception e) {
            if (logger.isErrorEnabled()) {
                logger.error("Failed to send the mail [{}] to [{}] due to unexpected exception", mail.getSubject(), mail.getTo(), e);
            }
            return false;
        }
    }

    private void checkBeforeSend(Mail mail) {
        Assert.hasText(mail.getFrom(), "mail.from is null or empty");
        Assert.hasText(mail.getTo(), "mail.to is null or empty");
        Assert.hasText(mail.getSubject(), "mail.subject is null or empty");
        Assert.hasText(mail.getContent(), "mail.content is null or empty");
    }

    private void handleMailSendException(Mail mail, MailSendException exception) {
        Throwable cause = exception.getCause();
        if (cause instanceof MailConnectException) {
            if (logger.isWarnEnabled()) {
                logger.warn("Failed to send the mail [{}] to [{}] due to MailConnectException", mail.getSubject(), mail.getTo(), cause);
            }

            RetryHandler autoRetryHandler = mail.getAutoRetryHandler();
            if (autoRetryHandler == null) {
                if (logger.isWarnEnabled()) {
                    logger.warn("No auto retry handler set, the mail will not be re-sent and the MailConnectException will be thrown");
                }
                throw exception;
            }

            if (logger.isInfoEnabled()) {
                logger.info("Starting to re-send the mail [{}] with auto retry handler: {}", mail.getSubject(), autoRetryHandler.getClass().getName());
            }
            autoRetryHandler.retry(() -> send(mail));
        } else {
            throw exception;
        }
    }

}
