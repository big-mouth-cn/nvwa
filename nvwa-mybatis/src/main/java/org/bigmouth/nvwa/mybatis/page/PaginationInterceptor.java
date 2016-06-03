/*
 * 文件名称: PaginationInterceptor.java
 * 版权信息: Copyright 2005-2012 SKY-MOBI Inc. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2012-6-29
 * 修改内容: 
 */
package org.bigmouth.nvwa.mybatis.page;

import java.util.List;
import java.util.Properties;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.ArrayUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.bigmouth.nvwa.mybatis.DbTypes;
import org.bigmouth.nvwa.spring.SpringContextHolder;

/**
 * 基于 MyBatis 的分页查询拦截器。
 * 
 * @author Allen.Hu / 2012-6-29
 * @since SkyMarket 1.0
 */
@Intercepts( { @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class }) })
public class PaginationInterceptor implements org.apache.ibatis.plugin.Interceptor {

    /** MappedStatement 索引位置 */
    static final int MAPPED_STATEMENT_INDEX = 0;

    /** 参数 索引位置 */
    static final int PARAMETER_INDEX = 1;

    /** RowBounds 分页信息 索引位置 */
    static final int ROWBOUNDS_INDEX = 2;

    /** ResultHandler 查询结果处理 索引位置 */
    static final int RESULT_HANDLER_INDEX = 3;

    private Properties properties;

    private static Logger logger = Logger.getLogger(PaginationInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

//        long start = System.currentTimeMillis();
        Object proceed = paggingProcessIntercept(invocation);
//        logger.debug("<== SQL excuting times: " + (System.currentTimeMillis() - start) + " ms");
        return proceed;
    }

    public Object paggingProcessIntercept(Invocation invocation) throws Throwable {

        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[MAPPED_STATEMENT_INDEX];
        Object parameter = args[PARAMETER_INDEX];
        RowBounds rowBounds = (RowBounds) args[ROWBOUNDS_INDEX];
//        logger.debug(ms.getBoundSql(parameter).getSql().trim());
        if (rowBounds == null || rowBounds == RowBounds.DEFAULT
                || (rowBounds.getLimit() == RowBounds.NO_ROW_LIMIT && rowBounds.getOffset() == RowBounds.NO_ROW_OFFSET)) {
            return invocation.proceed();
        }

        int offset = rowBounds.getOffset();
        int limit = rowBounds.getLimit();

        if (offset <= 0 || limit <= 0) {
            return invocation.proceed();
        }
        try {
            String dialectType = getDialectType();
            if (dialectType != null) {
                BoundSql boundSql = ms.getBoundSql(parameter);
                String sql = boundSql.getSql().trim();
                PaggingDialectFactory dialectFactory = SpringContextHolder.getBean(PaggingDialectFactory.class);
                PaggingDialect dialect = dialectFactory.getPaggingDialect(dialectType);

                // 分页SQL
                String paggingSql = dialect.getPaggingSql(sql, offset, limit);
                BoundSql newPaggingBoundSql = new BoundSql(ms.getConfiguration(), paggingSql, boundSql
                        .getParameterMappings(), boundSql.getParameterObject());
                copyAdditionalParameters(boundSql, newPaggingBoundSql);
                MappedStatement newPaggingMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newPaggingBoundSql));

                // 分页获取count
                try {
                    String countSql = dialect.getCountSql(sql);
                    
                    BoundSql newCountBoundSql = new BoundSql(ms.getConfiguration(), countSql, boundSql
                            .getParameterMappings(), boundSql.getParameterObject());
                    copyAdditionalParameters(boundSql, newCountBoundSql);
                    MappedStatement newCountMs = copyFromMappedStatement(ms, new BoundSqlSqlSource(newCountBoundSql));
                    args[ROWBOUNDS_INDEX] = RowBounds.DEFAULT;
                    args[MAPPED_STATEMENT_INDEX] = newCountMs;

                    RowBounds myRowBounds = (RowBounds) args[ROWBOUNDS_INDEX];

                    invocation.proceed();

                    rowBounds.setTotalCount(myRowBounds.getTotalCount());
//                    logger.debug("<== SQL total count is :" + rowBounds.getTotalCount());
                }
                catch (Exception e) {
                    // Ignore 忽略, 不要因为获取TotalCount失败影响正常的查询执行
                    logger.error("<== SQL 分页发生异常:", e);
                }

                args[ROWBOUNDS_INDEX] = RowBounds.DEFAULT;
                args[MAPPED_STATEMENT_INDEX] = newPaggingMs;
            }

            Object object = invocation.proceed();
            args[ROWBOUNDS_INDEX] = rowBounds;
            return object;
        }
        catch (Exception e) {
            logger.error("<== SQL 分页发生异常:", e);
            args[MAPPED_STATEMENT_INDEX] = ms;
            args[PARAMETER_INDEX] = parameter;
            args[ROWBOUNDS_INDEX] = rowBounds;
            return invocation.proceed();
        }

    }

    /**
     * 拷贝BoundSql的additionalParameters属性，该属性为私有并且没有提供set方法
     * 所以拷贝时代码较复杂，可以参考org.apache.ibatis.executor.parameter.setParameters 
     * 这儿并没有拷贝到所有的参数，但是用户传递参数已经全部拷贝出来 。
     * 
     * @param fromBoundSql
     * @param toBoundSql
     * @author Allen.Hu / 2012-6-29 
     * @since SkyMarket 1.0
     */
    private void copyAdditionalParameters(BoundSql fromBoundSql, BoundSql toBoundSql) {
        List<ParameterMapping> parameterMappings = fromBoundSql.getParameterMappings();
        Object value;
        ParameterMapping parameterMapping;
        String propertyName;
        if (parameterMappings != null) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    propertyName = parameterMapping.getProperty();
                    if (fromBoundSql.hasAdditionalParameter(propertyName)) {
                        value = fromBoundSql.getAdditionalParameter(propertyName);
                        toBoundSql.setAdditionalParameter(propertyName, value);
                    }
                }
            }
        }
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
                ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        builder.keyProperty(ArrayUtils.toString(ms.getKeyProperties()));
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        MappedStatement newMs = builder.build();
        return newMs;
    }

    /**
     * 取得需要分页的数据库类型. 优先获取MyBatis的配置; 如果没有配置, 
     * 从上下文获取当前使用的数据源的数据库类型.
     * @return
     * @author Allen.Hu / 2012-6-29 
     * @since SkyMarket 1.0
     */
    private String getDialectType() {
        String dialectType = null;
        if (properties != null) {
            dialectType = properties.getProperty("dialect");
        }

        try {
            if (dialectType == null) {
                dialectType = DbTypes.getDataSourceType((BasicDataSource) SpringContextHolder.getBean("dataSource"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return dialectType;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public static class BoundSqlSqlSource implements SqlSource {

        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        @Override
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

}
