package com.github.springframework.boot.commons.mybatis.interceptor;

import com.github.springframework.boot.commons.mybatis.handler.MybatisFieldHandler;
import com.github.springframework.boot.commons.mybatis.handler.MybatisParameterFieldHandler;
import com.github.springframework.boot.commons.mybatis.util.TableNameUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class MybatisParameterInterceptor implements Interceptor {

	private static final Logger logger = LoggerFactory.getLogger(MybatisParameterInterceptor.class);

	private final ParameterAndResultSetResolver parameterResolver = new ParameterAndResultSetResolver();

	private final List<MybatisParameterFieldHandler> parameterFieldHandlerChain = new ArrayList<>();

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object[] args = invocation.getArgs();
		MappedStatement statement = (MappedStatement) args[0];
		SqlCommandType sqlCommandType = statement.getSqlCommandType();
		if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {
			Object parameter = args[1];
			Set<String> tableNames = new HashSet<>();
			try {
				tableNames.addAll(TableNameUtils.resolveExecutorTableName(invocation));
				for (MybatisFieldHandler handler : parameterFieldHandlerChain) {
					for (String tableName : tableNames) {
						parameterResolver.resolve(handler, tableName, parameter);
					}
				}
			} catch (Exception e) {
				logger.error("Error occurred when {} is handling table '{}' with parameter '{}'", getClass().getName(), tableNames, parameter, e);
			}
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Interceptor.super.plugin(target);
	}

	@Override
	public void setProperties(Properties properties) {
		Interceptor.super.setProperties(properties);
	}

	public void registerParameterFieldHandler(MybatisParameterFieldHandler handler) {
		parameterFieldHandlerChain.add(handler);
		if (logger.isDebugEnabled()) {
			logger.debug("{} has been successfully registered for {}", handler.getClass().getName(), getClass().getName());
		}
	}

}
