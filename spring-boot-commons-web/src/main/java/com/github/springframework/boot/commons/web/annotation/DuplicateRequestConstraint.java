package com.github.springframework.boot.commons.web.annotation;

import com.github.springframework.boot.commons.web.request.DuplicateRequestConstraintStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicateRequestConstraint {
    Class<? extends DuplicateRequestConstraintStrategy> strategy();

    String identifier();

    long duration() default 30 * 1000;

    TimeUnit unit() default TimeUnit.MILLISECONDS;
}
