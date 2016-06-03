/*
 * 文件名称: PostgreSqlPaggingDialect.java
 * 版权信息: Copyright 2005-2012 SKY-MOBI Inc. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2012-6-29
 * 修改内容: 
 */
package org.bigmouth.nvwa.mybatis.page.postgre;


import org.apache.ibatis.session.RowBounds;
import org.bigmouth.nvwa.mybatis.page.DefaultPaggingDialect;


/**
 * PostgreSql 数据库分页查询方言。
 * 
 * @author Allen.Hu / 2012-6-29
 * @since SkyMarket 1.0
 */
public class PostgreSqlPaggingDialect extends DefaultPaggingDialect {

    @Override
    public String getPaggingSql(String querySql, int pageNo, int pageSize) {
        int myPageNo = (pageNo > 0 ? pageNo : 1);
        int myPageSize = (pageSize > 0 ? pageSize : 10);
        int begin = (myPageNo - 1) * myPageSize;

        String sql = querySql.trim() + " OFFSET " + begin + " LIMIT " + myPageSize;
        return sql;
    }

    @Override
    public String getCountSql(String querySql) {
//        querySql = querySql.trim().toUpperCase();
//        String endSql = querySql.substring(querySql.indexOf("FROM"));
//        if (endSql.toUpperCase().indexOf("ORDER BY") != -1) {
//            endSql = endSql.substring(0, endSql.toUpperCase().indexOf("ORDER BY"));            
//        }
        return "SELECT COUNT(*) " + RowBounds.COUNT_COLUMN_ALIAS + " FROM (" + querySql + ") AS TEMP_TABLE";
    }

}
