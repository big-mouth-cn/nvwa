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

import java.util.regex.Pattern;

import org.apache.ibatis.session.RowBounds;


/**
 * 默认（不分页）分页查询方言。
 * 
 * @author Allen.Hu / 2012-6-29
 * @since SkyMarket 1.0
 */
public class DefaultPaggingDialect implements PaggingDialect {

    protected static final Pattern ORDERBY = Pattern.compile("order\\s+by\\s+[^,\\s]+(\\s+(asc|desc))?(\\s*,\\s*[^,\\s]+(\\s+(asc|desc))?\\s*)*", Pattern.CASE_INSENSITIVE);
    protected static final String EMPTY = "";
    
    /**
     * (non-Javadoc)
     * 
     * @see com.skymobi.commons.dao.page.PaggingDialect#getCountSql(java.lang.String)
     */
    @Override
    public String getCountSql(String querySql) {
        querySql = ORDERBY.matcher(querySql).replaceAll(EMPTY);
        return "SELECT COUNT(0) " + RowBounds.COUNT_COLUMN_ALIAS + " FROM (" + querySql + ") AS COUNT_TABLE";
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
