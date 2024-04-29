package com.github.springframework.boot.commons.mybatis.interceptor;

import com.github.springframework.boot.commons.mybatis.handler.MybatisInvocationHandler;
import com.github.springframework.boot.commons.mybatis.handler.MybatisResultSetFieldHandler;
import com.github.springframework.boot.commons.mybatis.handler.ResultSetInvocationHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Intercepts(@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}))
public class MybatisResultSetInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(MybatisResultSetInterceptor.class);

    private final MybatisInvocationHandler invocationHandler = new ResultSetInvocationHandler();

    private final List<MybatisResultSetFieldHandler> resultSetFieldHandlerChain = new ArrayList<>();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return invocationHandler.preHandle(resultSetFieldHandlerChain, invocation);
    }

    @Override
    public Object plugin(Object target) {
        return Interceptor.super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }

    public void registerResultSetFieldHandler(MybatisResultSetFieldHandler handler) {
        resultSetFieldHandlerChain.add(handler);
        if (logger.isDebugEnabled()) {
            logger.debug("{} has been successfully registered for {}", handler.getClass().getName(), getClass().getName());
        }
    }

}