package com.github.awesome.springboot.commons.data.mybatis.interceptor;

import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * {@code MybatisResultSetInterceptor} 是一个 MyBatis 插件拦截器，用于拦截 SQL 查询结果的处理过程。
 * 该拦截器主要作用是在 MyBatis 处理查询结果集时，解析 SQL 中的表名，并依次调用注册的{@link MybatisFieldHandler}
 * 处理器对查询结果进行处理。
 *
 * <p>该类继承自{@link AbstractMybatisInterceptor}，并使用{@link Intercepts}注解指定拦截{@link ResultSetHandler#handleResultSets}方法。</p>
 *
 * <h3>功能说明：</h3>
 * <ul>
 *     <li>拦截{@link ResultSetHandler#handleResultSets}方法，该方法负责处理查询结果集。</li>
 *     <li>解析 SQL 语句中的表名，遍历每个表名并依次调用注册的{@link MybatisFieldHandler}处理器。</li>
 *     <li>对查询结果进行处理，确保查询结果符合预期的业务需求。</li>
 * </ul>
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @see AbstractMybatisInterceptor
 * @see MybatisFieldHandler
 * @see ResultSetHandler
 * @see Statement
 * @since 1.1.0
 */
@Intercepts(@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}))
public class MybatisResultSetInterceptor extends AbstractMybatisInterceptor {

    /**
     * 存储注册的{@link MybatisFieldHandler}处理器链。
     * 这些处理器将在拦截器执行时依次处理查询结果集中的数据。
     */
    private final List<MybatisFieldHandler> resultSetFieldHandlerChain = new ArrayList<>();

    /**
     * 拦截执行查询结果处理的核心方法。此方法会在处理查询结果集时被调用，
     * 它会解析 SQL 中涉及的表名并依次调用每个注册的字段处理器来处理结果集。
     *
     * @param invocation 当前拦截的调用信息，包含方法参数等信息
     * @return 处理后的查询结果
     * @throws Throwable 异常抛出
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        DefaultResultSetHandler resultSetHandler = (DefaultResultSetHandler) invocation.getTarget();
        Set<String> parsedTableNamesFromSql = super.parseTableNamesFromSql(resultSetHandler);
        List<?> resultList = (List<?>) invocation.proceed();
        for (String tableName : parsedTableNamesFromSql) {
            for (MybatisFieldHandler handler : resultSetFieldHandlerChain) {
                super.doIntercept(handler, tableName, resultList);
            }
        }
        return resultList;
    }

    /**
     * 注册一个{@link MybatisFieldHandler}处理器，该处理器会在执行拦截操作时对查询结果进行处理。
     *
     * @param handler {@link MybatisFieldHandler}处理器实例
     */
    public void registerResultSetFieldHandler(MybatisFieldHandler handler) {
        resultSetFieldHandlerChain.add(handler);
    }

}
