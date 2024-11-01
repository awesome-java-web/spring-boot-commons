package com.github.springframework.boot.commons.web.annotation;

import com.github.springframework.boot.commons.web.enums.DuplicateRequestConstraintKeySource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DuplicateRequestConstraint {

    String parameter();

    DuplicateRequestConstraintKeySource source();

    long duration();

    TimeUnit unit();
}
