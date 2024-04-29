package com.github.springframework.boot.commons.mybatis.handler;

import com.github.springframework.boot.commons.mybatis.util.TableNameUtils;
import org.apache.ibatis.plugin.Invocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ResultSetInvocationHandler extends AbstractInvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(ResultSetInvocationHandler.class);

    @Override
    public Object preHandle(List<? extends MybatisFieldHandler> fieldHandlerChain, Invocation invocation)
        throws InvocationTargetException, IllegalAccessException {

        List<?> resultList = (List<?>) invocation.proceed();
        String tableName = null;

        try {
            tableName = TableNameUtils.resolveResultSetTableName(invocation);
            for (MybatisFieldHandler fieldHandler : fieldHandlerChain) {
                if (logger.isDebugEnabled()) {
                    logger.debug("{} start to handle table '{}' with result set '{}'",
                        fieldHandler.getClass().getName(), tableName, resultList
                    );
                }
                super.handle(fieldHandler, tableName, resultList);
            }
        } catch (Exception e) {
            logger.error("Error occurred when {} is handling table '{}' with result set '{}'", getClass().getName(), tableName, resultList, e);
        }

        return resultList;
    }

}
