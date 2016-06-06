/*
 * 文件名称: DefaultPaggingDialectFactory.java
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

import org.bigmouth.nvwa.mybatis.DbTypes;
import org.bigmouth.nvwa.mybatis.page.mysql.MySqlPaggingDialect;
import org.bigmouth.nvwa.mybatis.page.oracle.OraclePaggingDialect;
import org.bigmouth.nvwa.mybatis.page.postgre.PostgreSqlPaggingDialect;


/**
 * 分页查询方言工厂的默认实现。<br/>
 * 目前支持的数据库有：
 * <ul>
 * <li>Oracle</li>
 * <li>PostgreSql</li>
 * <li>MySQL</li>
 * </ul>
 * 
 * @author Allen.Hu / 2012-6-29
 * @since SkyMarket 1.0
 */
public class DefaultPaggingDialectFactory implements PaggingDialectFactory {

    @Override
    public PaggingDialect getPaggingDialect(String dbType) {
        PaggingDialect dialect = null;
        if (DbTypes.ORACLE.equals(dbType)) {
            dialect = new OraclePaggingDialect();
        }
        else if (DbTypes.POSTGRESQL.equals(dbType)) {
            dialect = new PostgreSqlPaggingDialect();
        }
        else if (DbTypes.MYSQL.equals(dbType)) {
            dialect = new MySqlPaggingDialect();
        }
        return dialect;
    }
}
