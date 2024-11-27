package com.github.springframework.boot.commons;

import com.github.springframework.boot.commons.scripting.groovy.GroovyScriptExecutor;
import com.github.springframework.boot.commons.util.bean.AtomicRedisOperations;
import com.github.springframework.boot.commons.util.bean.SpringApplicationContextHolder;
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
    public AtomicRedisOperations atomicRedisOperations() {
        return new AtomicRedisOperations();
    }

    @Bean
    public SpringApplicationContextHolder springApplicationContextHolder() {
        return SpringApplicationContextHolder.initializeAsBean();
    }

    @Bean
    public DuplicateHttpRequestInterceptor duplicateHttpRequestInterceptor() {
        return new DuplicateHttpRequestInterceptor();
    }

}
