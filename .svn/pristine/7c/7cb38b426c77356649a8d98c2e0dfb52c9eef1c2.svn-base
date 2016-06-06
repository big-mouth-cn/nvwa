/*
 * 文件名称: OraclePaggingDialect.java
 * 版权信息: Copyright 2012 Big-mouth framework All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2012-6-29
 * 修改内容: 
 */
package org.bigmouth.nvwa.mybatis.page.oracle;

import org.bigmouth.nvwa.mybatis.page.DefaultPaggingDialect;

/**
 * Oracle 数据库分页查询方言。以ROWNUM方式实现, 注意ROWNUM从1开始.
 * 
 * <pre>
 *  SELECT * FROM ( 
 *    SELECT row_.*, ROWNUM rownum_ FROM (
 *       select * from myLargeTable 
 *    ) row_ WHERE rownum_ &lt; 11
 *  ) WHERE rownum_ &gt;= 1
 * </pre>
 * 
 * @author Allen.Hu / 2012-6-29
 * @since Bigmouth-Framework 1.0
 */
public class OraclePaggingDialect extends DefaultPaggingDialect {

    /**
     * (non-Javadoc)
     * 
     * @see com.skymobi.commons.dao.page.PaggingDialect#getPaggingSql(java.lang.String, int, int)
     */
    @Override
    public String getPaggingSql(String querySql, int pageNo, int pageSize) {
        int myPageNo = (pageNo > 0 ? pageNo : 1);
        int myPageSize = (pageSize > 0 ? pageSize : 10);
        int begin = (myPageNo - 1) * myPageSize + 1;
        int end = begin + myPageSize;

        querySql = querySql.trim();
        boolean isForUpdate = false;
        if (querySql.toLowerCase().endsWith(" for update")) {
            querySql = querySql.substring(0, querySql.length() - 11);
            isForUpdate = true;
        }

        StringBuffer pagingSelect = new StringBuffer(querySql.length() + 100);
        pagingSelect.append("SELECT * FROM ( SELECT row_.*, rownum rownum_ FROM ( ");
        pagingSelect.append(querySql);
        pagingSelect.append(" ) row_ WHERE rownum < " + end + ") WHERE rownum_ >= " + begin);

        if (isForUpdate) {
            pagingSelect.append(" for update");
        }

        return pagingSelect.toString();
    }

}
