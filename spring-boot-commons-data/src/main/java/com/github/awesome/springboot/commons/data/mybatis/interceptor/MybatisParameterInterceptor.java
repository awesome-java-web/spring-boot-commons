package com.github.awesome.springboot.commons.data.mybatis.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * {@code MybatisParameterInterceptor} 是一个 MyBatis 插件拦截器，用于在执行 SQL 更新操作（包括插入和更新）时，
 * 对参数进行处理。该拦截器主要作用是通过解析 SQL 语句中的表名，并针对每个表名使用指定的{@link MybatisFieldHandler}
 * 对相关参数进行处理。
 *
 * <p>该类继承自{@link AbstractMybatisInterceptor}，并实现了{@link Intercepts}注解所指定的拦截功能。</p>
 *
 * <p>拦截的 SQL 操作为{@link Executor#update}方法，该方法用于执行插入或更新操作。</p>
 *
 * <h3>功能说明：</h3>
 * <ul>
 *     <li>通过{@link SqlCommandType#INSERT}和{@link SqlCommandType#UPDATE}判断 SQL 操作类型。</li>
 *     <li>解析 SQL 语句中的表名，并对每个表名进行处理。</li>
 *     <li>对每个表名使用注册的{@link MybatisFieldHandler}进行参数处理。</li>
 * </ul>
 *
 * @author <a href="https://github.com/codeboyzhou">codeboyzhou</a>
 * @see AbstractMybatisInterceptor
 * @see MybatisFieldHandler
 * @see Executor
 * @see MappedStatement
 * @since 1.1.0
 */
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class MybatisParameterInterceptor extends AbstractMybatisInterceptor {

    /**
     * 存储注册的{@link MybatisFieldHandler}处理器链。
     * 这些处理器将在拦截器执行时依次处理 SQL 参数。
     */
    private final List<MybatisFieldHandler> parameterFieldHandlerChain = new ArrayList<>();

    /**
     * 拦截执行 SQL 更新操作的核心方法。此方法会在插入或更新操作时被调用，
     * 它会解析 SQL 中涉及的表名并依次调用每个注册的字段处理器来处理相应的参数。
     *
     * @param invocation 当前拦截的调用信息，包含方法参数等信息
     * @return 方法的执行结果
     * @throws Throwable 异常抛出
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] invocationArgs = invocation.getArgs();
        MappedStatement statement = (MappedStatement) invocationArgs[0];
        SqlCommandType sqlCommandType = statement.getSqlCommandType();
        if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {
            Object parameter = invocationArgs[1];
            Set<String> parsedTableNamesFromSql = super.parseTableNamesFromSql(statement, parameter);
            for (String tableName : parsedTableNamesFromSql) {
                for (MybatisFieldHandler handler : parameterFieldHandlerChain) {
                    super.doIntercept(handler, tableName, parameter);
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return super.plugin(target);
    }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
    }

    /**
     * 注册一个{@link MybatisFieldHandler}处理器，该处理器会在执行拦截操作时对参数进行处理。
     *
     * @param handler {@link MybatisFieldHandler}处理器实例
     */
    public void registerParameterFieldHandler(MybatisFieldHandler handler) {
        parameterFieldHandlerChain.add(handler);
    }

}
