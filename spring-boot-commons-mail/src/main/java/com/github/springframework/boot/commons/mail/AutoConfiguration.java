package com.github.springframework.boot.commons.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfiguration {

	@Bean
	public EmailSender emailSender() {
		return new EmailSenderImpl();
	}

}
