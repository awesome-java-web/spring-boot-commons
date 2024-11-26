package com.github.springframework.boot.commons.web.request;

import com.github.springframework.boot.commons.web.annotation.DuplicateRequestConstraint;

import javax.servlet.http.HttpServletRequest;

public interface DuplicateRequestConstraintStrategy {
    boolean proceed(HttpServletRequest request, DuplicateRequestConstraint constraint);

    String parseRequestUniqueId(HttpServletRequest request, DuplicateRequestConstraint annotation);
}
