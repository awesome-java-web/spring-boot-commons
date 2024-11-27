package com.github.springframework.boot.commons.web.interceptor;

import com.github.springframework.boot.commons.util.base.Chars;
import com.github.springframework.boot.commons.util.bean.AtomicRedisOperations;
import com.github.springframework.boot.commons.web.enums.DuplicateHttpRequestIdentifierLocation;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class DuplicateHttpRequestInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(DuplicateHttpRequestInterceptor.class);

    private String duplicateRequestIdentifier;

    private DuplicateHttpRequestIdentifierLocation duplicateRequestIdentifierLocation;

    private long duplicateRequestTimeThreshold = 30 * 1000;

    private TimeUnit timeUnit = TimeUnit.MILLISECONDS;

    @Autowired
    private AtomicRedisOperations atomicRedisOperations;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        final boolean isInvalidIdentifier = duplicateRequestIdentifier == null || duplicateRequestIdentifier.trim().isEmpty();
        final boolean isInvalidIdentifierLocation = duplicateRequestIdentifierLocation == null;
        final boolean isInvalidTimeThreshold = duplicateRequestTimeThreshold <= 0;
        if (isInvalidIdentifier || isInvalidIdentifierLocation || isInvalidTimeThreshold) {
            return true;
        }

        if (handler instanceof HandlerMethod) {
            String requestUniqueId = null;
            if (duplicateRequestIdentifierLocation == DuplicateHttpRequestIdentifierLocation.HTTP_HEADER) {
                requestUniqueId = request.getHeader(duplicateRequestIdentifier);
            } else if (duplicateRequestIdentifierLocation == DuplicateHttpRequestIdentifierLocation.QUERY_STRING) {
                requestUniqueId = parseRequestUniqueIdFromQueryString(request, duplicateRequestIdentifier);
            }

            if (requestUniqueId == null || requestUniqueId.trim().isEmpty()) {
                return true;
            }

            final String redisKey = request.getRequestURI() + Chars.DASH.stringValue() + requestUniqueId;
            final String redisValue = Long.toString(System.currentTimeMillis());
            try {
                final boolean successful = atomicRedisOperations.setIfAbsent(redisKey, redisValue, duplicateRequestTimeThreshold, timeUnit);
                if (!successful) {
                    logger.warn("Duplicate HTTP request '{}' received in {}{}", redisKey, duplicateRequestTimeThreshold, timeUnit.name().toLowerCase());
                    return false;
                }
            } catch (Exception e) {
                // 如果出现Redis连接异常，就临时降级，不进行重复请求校验，防止原本正常的请求被误拦截
                logger.warn("Redis operation failed, will skip duplicate request constraint validation temporarily", e);
                return true;
            }
        }

        return true;
    }

    public DuplicateHttpRequestInterceptor withDuplicateRequestIdentifier(String identifier) {
        this.duplicateRequestIdentifier = identifier;
        return this;
    }

    public DuplicateHttpRequestInterceptor withDuplicateRequestIdentifierLocation(DuplicateHttpRequestIdentifierLocation identifierLocation) {
        this.duplicateRequestIdentifierLocation = identifierLocation;
        return this;
    }

    public DuplicateHttpRequestInterceptor withDuplicateRequestTimeThreshold(long timeThreshold) {
        this.duplicateRequestTimeThreshold = timeThreshold;
        return this;
    }

    public DuplicateHttpRequestInterceptor withTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
        return this;
    }

    private String parseRequestUniqueIdFromQueryString(HttpServletRequest request, String identifier) {
        final String queryString = request.getQueryString();
        if (queryString == null || queryString.trim().isEmpty()) {
            return null;
        }
        String[] tokens = queryString.split(Chars.AND.stringValue());
        for (String token : tokens) {
            String[] keyValuePair = token.split(Chars.EQUAL.stringValue());
            final String parameterName = keyValuePair[0];
            final String parameterValue = keyValuePair[1];
            if (identifier.equals(parameterName)) {
                return parameterValue;
            }
        }
        return null;
    }

}
