package com.github.springframework.boot.commons.web;

import com.github.springframework.boot.commons.web.interceptor.DuplicateHttpRequestInterceptor;
import com.github.springframework.boot.commons.web.mail.MailSendService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AutoConfiguration {

    @Bean
    public MailSendService mailSender() {
        return new MailSendService();
    }

    @Bean
    public DuplicateHttpRequestInterceptor duplicateHttpRequestInterceptor() {
        return new DuplicateHttpRequestInterceptor();
    }

}
