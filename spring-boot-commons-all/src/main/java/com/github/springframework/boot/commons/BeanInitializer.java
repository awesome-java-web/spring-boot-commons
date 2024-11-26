package com.github.springframework.boot.commons;

import com.github.springframework.boot.commons.groovy.GroovyScriptExecutor;
import com.github.springframework.boot.commons.web.interceptor.DuplicateHttpRequestInterceptor;
import com.github.springframework.boot.commons.web.mail.MailSendService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanInitializer {

    @Bean
    public MailSendService mailSender() {
        return new MailSendService();
    }

    @Bean
    public GroovyScriptExecutor groovyScriptExecutor() {
        return new GroovyScriptExecutor();
    }

    @Bean
    public DuplicateHttpRequestInterceptor duplicateHttpRequestInterceptor() {
        return new DuplicateHttpRequestInterceptor();
    }

}
