package com.github.springframework.boot.commons.web.request;

import com.github.springframework.boot.commons.util.base.Chars;
import com.github.springframework.boot.commons.util.network.RedisOperations;
import com.github.springframework.boot.commons.web.annotation.DuplicateRequestConstraint;
import com.github.springframework.boot.commons.web.exception.DuplicateRequestConstraintException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

public interface DuplicateRequestConstraintStrategy {

	Logger logger = LoggerFactory.getLogger(DuplicateRequestConstraintStrategy.class);

	default boolean proceed(HttpServletRequest request, DuplicateRequestConstraint constraint) {
		final String identifier = constraint.identifier();
		if (identifier == null || identifier.trim().isEmpty()) {
			throw new DuplicateRequestConstraintException("Invalid identifier: null or empty");
		}

		final String requestUniqueId = parseRequestUniqueId(request, constraint);
		if (requestUniqueId == null || requestUniqueId.trim().isEmpty()) {
			throw new DuplicateRequestConstraintException("Invalid unique id for current request: null or empty");
		}

		final long duration = constraint.duration();
		if (duration <= 0) {
			throw new DuplicateRequestConstraintException("Invalid duration: " + duration);
		}

		final String key = request.getRequestURI() + Chars.DASH.stringValue() + requestUniqueId;
		final String value = Long.toString(System.currentTimeMillis());
		try {
			TimeUnit timeUnit = constraint.unit();
			final boolean successful = RedisOperations.setIfAbsent(key, value, duration, timeUnit);
			if (successful) {
				return true;
			} else {
				logger.warn("Duplicate HTTP request '{}' received in {}{}", key, duration, timeUnit.name().toLowerCase());
				return false;
			}
		} catch (Exception e) {
			// 如果出现Redis连接异常，就临时降级，不进行重复请求校验，防止原本正常的请求被误拦截
			logger.warn("Redis operations failed, will skip duplicate request constraint validation temporarily", e);
			return true;
		}
	}

	String parseRequestUniqueId(HttpServletRequest request, DuplicateRequestConstraint annotation);
}
