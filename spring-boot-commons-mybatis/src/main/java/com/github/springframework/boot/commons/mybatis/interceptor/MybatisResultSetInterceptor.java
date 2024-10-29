package com.github.springframework.boot.commons.mybatis.interceptor;

import com.github.springframework.boot.commons.mybatis.handler.MybatisFieldHandler;
import com.github.springframework.boot.commons.mybatis.handler.MybatisResultSetFieldHandler;
import com.github.springframework.boot.commons.mybatis.util.TableNameUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Statement;
import java.util.*;

@Intercepts(@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}))
public class MybatisResultSetInterceptor implements Interceptor {

	private static final Logger logger = LoggerFactory.getLogger(MybatisResultSetInterceptor.class);

	private final ParameterAndResultSetResolver resultSetResolver = new ParameterAndResultSetResolver();

	private final List<MybatisResultSetFieldHandler> resultSetFieldHandlerChain = new ArrayList<>();

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		List<?> resultList = (List<?>) invocation.proceed();
		Set<String> tableNames = new HashSet<>();
		try {
			tableNames.addAll(TableNameUtils.resolveResultSetTableName(invocation));
			for (MybatisFieldHandler handler : resultSetFieldHandlerChain) {
				for (String tableName : tableNames) {
					resultSetResolver.resolve(handler, tableName, resultList);
				}
			}
		} catch (Exception e) {
			logger.error("Error occurred when {} is handling table '{}' with result '{}'", getClass().getName(), tableNames, resultList, e);
		}
		return resultList;
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