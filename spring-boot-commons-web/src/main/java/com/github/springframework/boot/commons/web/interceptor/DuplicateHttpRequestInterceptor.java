package com.github.springframework.boot.commons.web.interceptor;

import com.github.springframework.boot.commons.util.Chars;
import com.github.springframework.boot.commons.util.RedisTemplateHelper;
import com.github.springframework.boot.commons.web.annotation.DuplicateRequestConstraint;
import com.github.springframework.boot.commons.web.enums.DuplicateRequestConstraintKeySource;
import com.github.springframework.boot.commons.web.exception.DuplicateHttpRequestInterceptorException;
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
				throw new DuplicateHttpRequestInterceptorException("Invalid parameter value for DuplicateRequestConstraint annotation: null or empty");
			}

			final String requestId = parseRequestId(request, annotation);
			if (requestId == null) {
				throw new DuplicateHttpRequestInterceptorException("Invalid requestId value for DuplicateRequestConstraint annotation: null");
			}

			final long duration = annotation.duration();
			if (duration <= 0) {
				throw new DuplicateHttpRequestInterceptorException("Invalid duration value for DuplicateRequestConstraint annotation: " + duration);
			}

			final String key = request.getRequestURI() + Chars.DASH.stringValue() + requestId;
			final String value = Long.toString(System.currentTimeMillis());
			try {
				final boolean success = RedisTemplateHelper.setIfAbsent(stringRedisTemplate, key, value, duration, annotation.unit());
				if (!success) {
					logger.warn("Duplicate HTTP request '{}' received in a short period, subsequent requests will be ignored during this time", key);
					return false;
				}
			} catch (Exception e) {
				// 如果出现Redis连接异常，就临时降级，不进行重复请求校验，防止原本正常的请求被误拦截
				logger.error("Failed to execute Redis command, will skip duplicate request constraint validation temporarily", e);
				return true;
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
