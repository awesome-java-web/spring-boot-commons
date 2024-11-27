package com.github.springframework.boot.commons.web.interceptor;

import com.github.springframework.boot.commons.web.annotation.DuplicateRequestConstraint;
import com.github.springframework.boot.commons.web.request.DuplicateRequestConstraintStrategy;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class DuplicateHttpRequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            DuplicateRequestConstraint constraint = method.getAnnotation(DuplicateRequestConstraint.class);
            if (constraint == null || constraint.strategy() == null) {
                return true;
            }

            Object strategyObject = ReflectionUtils.createInstanceIfPresent(constraint.strategy().getName(), new Object());
            DuplicateRequestConstraintStrategy strategy = (DuplicateRequestConstraintStrategy) strategyObject;
            return strategy.proceed(request, constraint);
        }
        return true;
    }

}
