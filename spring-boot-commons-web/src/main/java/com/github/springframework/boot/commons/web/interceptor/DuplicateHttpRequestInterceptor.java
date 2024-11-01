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
import java.util.concurrent.TimeUnit;

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

			final long duration = annotation.duration();
			TimeUnit unit = annotation.unit();
			final String requestURI = request.getRequestURI();
			final String requestIdCacheKey = requestURI + Chars.DASH.stringValue() + requestId;
			final String cachedRequestId = stringRedisTemplate.opsForValue().get(requestIdCacheKey);
			if (cachedRequestId == null) {
				final String requestArrivalTime = String.valueOf(System.currentTimeMillis());
				stringRedisTemplate.opsForValue().set(requestIdCacheKey, requestArrivalTime, duration, unit);
				return true;
			} else {
				final String log = "Duplicate http request received in {} {}: {}, the later requests will be ignored during this period";
				logger.warn(log, duration, unit.name().toLowerCase(), requestIdCacheKey);
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
