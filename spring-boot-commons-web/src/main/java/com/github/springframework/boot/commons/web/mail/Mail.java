package com.github.springframework.boot.commons.web.mail;

import com.github.springframework.boot.commons.retry.RetryHandler;
import org.apache.logging.log4j.util.Strings;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.Set;

public class Mail {

    private String name = Strings.EMPTY;

    private String from;

    private String to;

    private String subject;

    private String content;

    private boolean asHtml;

    private String cc = Strings.EMPTY;

    private String bcc = Strings.EMPTY;

    private List<File> attachments;

    private RetryHandler autoRetryHandler;

    public String getName() {
        return name;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public boolean isAsHtml() {
        return asHtml;
    }

    public String getCc() {
        return cc;
    }

    public String getBcc() {
        return bcc;
    }

    public List<File> getAttachments() {
        return attachments;
    }

    public RetryHandler getAutoRetryHandler() {
        return autoRetryHandler;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final Mail mail = new Mail();

        public Builder name(String name) {
            mail.name = name;
            return this;
        }

        public Builder from(String from) {
            mail.from = from;
            return this;
        }

        public Builder to(String to) {
            mail.to = to;
            return this;
        }

        public Builder subject(String subject) {
            mail.subject = subject;
            return this;
        }

        public Builder content(String content) {
            mail.content = content;
            return this;
        }

        public Builder asHtml(boolean asHtml) {
            mail.asHtml = asHtml;
            return this;
        }

        public Builder cc(String cc) {
            mail.cc = cc;
            return this;
        }

        public Builder bcc(String bcc) {
            mail.bcc = bcc;
            return this;
        }

        public Builder withAttachments(List<File> attachments) {
            mail.attachments = attachments;
            return this;
        }

        public Builder withAutoRetryHandler(RetryHandler autoRetryHandler) {
            mail.autoRetryHandler = autoRetryHandler;
            return this;
        }

        public Mail build() {
            return mail;
        }

    }

    public MimeMessageHelper toMimeMessageHelper(MimeMessage mimeMessage) throws MessagingException {
        final boolean multipart = attachments != null && !attachments.isEmpty();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, multipart);

        final String fromName = String.format("%s<%s>", name, from);
        messageHelper.setFrom(new InternetAddress(fromName));
        Set<String> distinctTo = StringUtils.commaDelimitedListToSet(to);
        messageHelper.setTo(StringUtils.toStringArray(distinctTo));
        messageHelper.setSubject(subject);
        messageHelper.setText(content, asHtml);

        if (cc != null && !cc.isEmpty()) {
            Set<String> distinctCopyTo = StringUtils.commaDelimitedListToSet(cc);
            messageHelper.setCc(StringUtils.toStringArray(distinctCopyTo));
        }

        if (bcc != null && !bcc.isEmpty()) {
            Set<String> distinctBlindCopyTo = StringUtils.commaDelimitedListToSet(bcc);
            messageHelper.setBcc(StringUtils.toStringArray(distinctBlindCopyTo));
        }

        if (multipart) {
            for (File attachment : attachments) {
                messageHelper.addAttachment(attachment.getName(), attachment);
            }
        }

        return messageHelper;
    }

}
