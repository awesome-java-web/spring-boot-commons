package com.github.springframework.boot.commons.web.interceptor;

import com.github.springframework.boot.commons.util.Chars;
import com.github.springframework.boot.commons.web.annotation.DuplicateRequestConstraint;
import com.github.springframework.boot.commons.web.enums.DuplicateRequestConstraintKeySource;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class DuplicateHttpRequestInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(DuplicateHttpRequestInterceptor.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();

            DuplicateRequestConstraint annotation = method.getAnnotation(DuplicateRequestConstraint.class);
            if (annotation == null) {
                return true;
            }

            final String expectedParameter = annotation.parameter();
            if (expectedParameter == null || expectedParameter.trim().isEmpty()) {
                return true;
            }

            final String requestId = parseRequestId(request, annotation);
            if (requestId == null) {
                return true;
            }

            final String requestURI = request.getRequestURI();
            final String cachedRequestId = stringRedisTemplate.opsForValue().get(requestURI);
            if (cachedRequestId == null) {
                stringRedisTemplate.opsForValue().set(requestURI, requestId, annotation.duration(), annotation.unit());
                return true;
            } else if (cachedRequestId.equals(requestId)) {
                logger.warn("Duplicate http request received in {} {}: {} ==> {}, the later requests will be ignored",
                    annotation.duration(), annotation.unit().name().toLowerCase(), requestURI, requestId
                );
                return false;
            }
        }
        return true;
    }

    private String parseRequestId(HttpServletRequest request, DuplicateRequestConstraint annotation) {
        DuplicateRequestConstraintKeySource source = annotation.source();
        if (source == DuplicateRequestConstraintKeySource.QUERY_STRING) {
            final String queryString = request.getQueryString();
            if (queryString == null || queryString.trim().isEmpty()) {
                return null;
            }
            String[] tokens = queryString.split(Chars.AND.stringValue());
            for (String token : tokens) {
                String[] keyValuePair = token.split(Chars.EQUAL.stringValue());
                final String parameter = keyValuePair[0];
                final String parameterValue = keyValuePair[1];
                if (annotation.parameter().equals(parameter)) {
                    return parameterValue;
                }
            }
        }
        return null;
    }

}
