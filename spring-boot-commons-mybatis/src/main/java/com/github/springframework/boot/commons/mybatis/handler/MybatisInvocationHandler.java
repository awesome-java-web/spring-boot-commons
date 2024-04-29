package com.github.springframework.boot.commons.mybatis.handler;

import org.apache.ibatis.plugin.Invocation;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface MybatisInvocationHandler {

    Object preHandle(List<? extends MybatisFieldHandler> fieldHandlerChain, Invocation invocation)
        throws InvocationTargetException, IllegalAccessException;

}
