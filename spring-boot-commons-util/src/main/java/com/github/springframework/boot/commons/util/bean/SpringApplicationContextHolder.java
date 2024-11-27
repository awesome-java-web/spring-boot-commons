package com.github.springframework.boot.commons.util.bean;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public final class SpringApplicationContextHolder implements ApplicationContextAware {

    private static final SpringApplicationContextHolder INSTANCE = new SpringApplicationContextHolder();

    private static ApplicationContext applicationContext;

    private SpringApplicationContextHolder() {
        // 禁止从外部直接实例化来使用
    }

    public static SpringApplicationContextHolder initializeAsBean() {
        return INSTANCE;
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContextHolder.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

}
