package com.github.springframework.boot.commons.mybatis.handler;

import com.github.springframework.boot.commons.mybatis.util.TableNameUtils;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ParameterInvocationHandler extends AbstractInvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(ParameterInvocationHandler.class);

    /**
     * @see org.apache.ibatis.executor.Executor#update(MappedStatement, Object)
     */
    @Override
    public Object preHandle(List<? extends MybatisFieldHandler> fieldHandlerChain, Invocation invocation)
        throws InvocationTargetException, IllegalAccessException {

        Object[] args = invocation.getArgs();
        Object parameter = args[1];
        String tableName = null;

        try {
            tableName = TableNameUtils.resolveExecutorTableName(invocation);
            for (MybatisFieldHandler fieldHandler : fieldHandlerChain) {
                if (logger.isDebugEnabled()) {
                    logger.debug("{} start to handle table '{}' with parameter type '{}'",
                        fieldHandler.getClass().getName(), tableName,
                        parameter == null ? null : parameter.getClass().getName()
                    );
                }
                super.handle(fieldHandler, tableName, parameter);
            }
        } catch (Exception e) {
            logger.error("Error occurred when {} is handling table '{}' with parameter type '{}'",
                getClass().getName(), tableName, parameter == null ? null : parameter.getClass().getName(), e
            );
        }

        return invocation.proceed();
    }

}
