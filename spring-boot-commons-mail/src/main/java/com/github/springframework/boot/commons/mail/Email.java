package com.github.springframework.boot.commons.mail;

import com.github.springframework.boot.commons.util.Strings;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;
import java.util.Set;

public class Email {

	private String name = Strings.EMPTY;

	private String from;

	private String to;

	private String subject;

	private String content;

	private boolean asHtml;

	private String copyTo;

	private String blindCopyTo;

	private List<File> attachments;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isAsHtml() {
		return asHtml;
	}

	public void setAsHtml(boolean asHtml) {
		this.asHtml = asHtml;
	}

	public String getCopyTo() {
		return copyTo;
	}

	public void setCopyTo(String copyTo) {
		this.copyTo = copyTo;
	}

	public String getBlindCopyTo() {
		return blindCopyTo;
	}

	public void setBlindCopyTo(String blindCopyTo) {
		this.blindCopyTo = blindCopyTo;
	}

	public List<File> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<File> attachments) {
		this.attachments = attachments;
	}

	public static EmailBuilder builder() {
		return new EmailBuilder();
	}

	public static class EmailBuilder {

		private final Email email = new Email();

		public EmailBuilder from(String from) {
			email.from = from;
			return this;
		}

		public EmailBuilder to(String to) {
			email.to = to;
			return this;
		}

		public EmailBuilder subject(String subject) {
			email.subject = subject;
			return this;
		}

		public EmailBuilder content(String content) {
			email.content = content;
			return this;
		}

		public EmailBuilder asHtml(boolean asHtml) {
			email.asHtml = asHtml;
			return this;
		}

		public EmailBuilder copyTo(String copyTo) {
			email.copyTo = copyTo;
			return this;
		}

		public EmailBuilder blindCopyTo(String blindCopyTo) {
			email.blindCopyTo = blindCopyTo;
			return this;
		}

		public EmailBuilder withAttachments(List<File> attachments) {
			email.attachments = attachments;
			return this;
		}

		public Email build() {
			return email;
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

		if (copyTo != null && !copyTo.isEmpty()) {
			Set<String> distinctCopyTo = StringUtils.commaDelimitedListToSet(copyTo);
			messageHelper.setCc(StringUtils.toStringArray(distinctCopyTo));
		}

		if (blindCopyTo != null && !blindCopyTo.isEmpty()) {
			Set<String> distinctBlindCopyTo = StringUtils.commaDelimitedListToSet(blindCopyTo);
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
