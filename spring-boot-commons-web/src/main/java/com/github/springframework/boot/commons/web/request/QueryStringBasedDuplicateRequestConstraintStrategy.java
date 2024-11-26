package com.github.springframework.boot.commons.web.request;

import com.github.springframework.boot.commons.util.base.Chars;
import com.github.springframework.boot.commons.web.annotation.DuplicateRequestConstraint;

import javax.servlet.http.HttpServletRequest;

public class QueryStringBasedDuplicateRequestConstraintStrategy extends DefaultDuplicateRequestConstraintStrategy {

    @Override
    public String parseRequestUniqueId(HttpServletRequest request, DuplicateRequestConstraint constraint) {
        final String queryString = request.getQueryString();
        if (queryString == null || queryString.trim().isEmpty()) {
            return null;
        }
        String[] tokens = queryString.split(Chars.AND.stringValue());
        for (String token : tokens) {
            String[] keyValuePair = token.split(Chars.EQUAL.stringValue());
            final String parameterName = keyValuePair[0];
            final String parameterValue = keyValuePair[1];
            if (constraint.identifier().equals(parameterName)) {
                return parameterValue;
            }
        }
        return null;
    }

}
