package com.github.springframework.boot.commons.mybatis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class FieldNameUtils {

    private static final Logger logger = LoggerFactory.getLogger(FieldNameUtils.class);

    private static final String MATCH_ALL_SYMBOL = "*";

    private static final String UNDERSCORE = "_";

    FieldNameUtils() {
        throw new UnsupportedOperationException("Utility class should not be instantiated");
    }

    public static List<String> determineTargetFieldNames(Map<String, List<String>> targetTableFields, final String tableName) {
        if (targetTableFields.containsKey(tableName)) {
            return targetTableFields.get(tableName);
        }
        return targetTableFields.getOrDefault(MATCH_ALL_SYMBOL, Collections.emptyList());
    }

    public static String underscoreToCamelCase(final String underscoreName) {
        if (underscoreName == null || underscoreName.isEmpty()) {
            throw new IllegalArgumentException("underscoreName == null || underscoreName.isEmpty()");
        }

        String[] words = underscoreName.split(UNDERSCORE);
        StringBuilder result = new StringBuilder(words[0]);
        for (int i = 1; i < words.length; i++) {
            final String word = words[i];
            result.append(Character.toUpperCase(word.charAt(0)));
            if (word.length() > 1) {
                result.append(word.substring(1));
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Converted underscore field name to camel case style: {} -> {}", underscoreName, result);
        }

        return result.toString();
    }

}
