package com.github.springframework.boot.commons.mail;

import com.github.springframework.boot.commons.retry.RetryPolicy;
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

    private RetryPolicy retryPolicy;

    @Override
    public void send(Email email) {
        checkBeforeSend(email);
        try {
            MimeMessage originalMimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = email.toMimeMessageHelper(originalMimeMessage);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            logger.info("Email [{}] sent to [{}] successfully.", email.getSubject(), email.getTo());
        } catch (MailSendException e) {
            Throwable cause = e.getCause();
            if (cause instanceof MailConnectException) {
                logger.warn("Failed to send email [{}] to [{}] due to network connection error", email.getSubject(), email.getTo(), cause);
                if (retryPolicy == null) {
                    logger.warn("No retry policy is set, the email will not be re-sent and MailConnectException will be thrown");
                    throw new EmailSendException(e);
                }
                logger.info("Starting to re-send this email with the retry policy: {}", retryPolicy.getClass().getName());
                retryPolicy.retry(() -> send(email));
            } else {
                throw new EmailSendException(e);
            }
        } catch (Exception e) {
            throw new EmailSendException(e);
        }
    }

    @Override
    public void setRetryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
    }

    private void checkBeforeSend(Email email) {
        Assert.hasText(email.getFrom(), "You must specify the 'from' value for the email message.");
        Assert.hasText(email.getTo(), "You must specify the 'to' value for the email message.");
        Assert.hasText(email.getSubject(), "You must specify the 'subject' value for the email message.");
        Assert.hasText(email.getContent(), "You must specify the 'content' value for the email message.");
    }

}
