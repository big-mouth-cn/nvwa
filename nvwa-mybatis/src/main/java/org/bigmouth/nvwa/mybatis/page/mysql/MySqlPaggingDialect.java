/*
 * 文件名称: MySqlPaggingDialect.java
 * 版权信息: Copyright 2012 Big-mouth framework. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2012-7-5
 * 修改内容: 
 */
package org.bigmouth.nvwa.mybatis.page.mysql;

import org.apache.ibatis.session.RowBounds;
import org.bigmouth.nvwa.mybatis.page.PaggingDialect;

/**
 * MySql分页方言. 注意MySQL的分页方式, limit值含义: 起始行(从0开始), 返回行数,
 * 
 * <pre>
 *  SELECT * FROM ( 
 *    select * from myLargeTable 
 *  ) LIMIT 0, 10;
 * </pre>
 * 
 * @author Allen.Hu / 2012-7-5
 * @since Bigmouth-Framework 1.0
 */
public class MySqlPaggingDialect implements PaggingDialect {

    @Override
    public String getPaggingSql(String querySql, int pageNo, int pageSize) {
        int myPageNo = (pageNo > 0 ? pageNo : 1);
        int myPageSize = (pageSize > 0 ? pageSize : 10);
        int begin = (myPageNo - 1) * myPageSize;

        String sql = querySql.trim() + " limit " + begin + " ," + myPageSize;
        return sql;
    }

    @Override
    public String getCountSql(String querySql) {
        querySql = querySql.trim().toLowerCase();
        String endSql = querySql.substring(querySql.indexOf("FROM"));

        return "SELECT COUNT(*) " + RowBounds.COUNT_COLUMN_ALIAS + " " + endSql;
    }

}
