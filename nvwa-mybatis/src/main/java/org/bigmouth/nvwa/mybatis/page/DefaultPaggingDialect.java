/*
 * 文件名称: DefaultPaggingDialect.java
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

import org.apache.ibatis.session.RowBounds;


/**
 * 默认（不分页）分页查询方言。
 * 
 * @author Allen.Hu / 2012-6-29
 * @since SkyMarket 1.0
 */
public class DefaultPaggingDialect implements PaggingDialect {

    /**
     * (non-Javadoc)
     * 
     * @see com.skymobi.commons.dao.page.PaggingDialect#getCountSql(java.lang.String)
     */
    @Override
    public String getCountSql(String querySql) {
        return "SELECT COUNT(*) " + RowBounds.COUNT_COLUMN_ALIAS + " FROM (" + querySql + ")";
    }

    /**
     * (non-Javadoc)
     * 
     * @see com.skymobi.commons.dao.page.PaggingDialect#getPaggingSql(java.lang.String, int, int)
     */
    @Override
    public String getPaggingSql(String querySql, int pageNo, int pageSize) {
        return querySql;
    }

}
