package com.github.springframework.boot.commons.mybatis.handler;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface MybatisFieldHandler {

    Object handle(final String tableName, final String fieldName, @Nullable Object fieldValue);

    Map<String, List<String>> targetTableFields();

}
