/*
 * 文件名称: OracleEscape.java
 * 版权信息: Copyright 2005-2013 SKY-MOBI Inc. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2013-3-18
 * 修改内容: 
 */
package org.bigmouth.nvwa.mybatis.escape;


/**
 * 
 * @author Allen.Hu / 2013-3-18
 */
public class OracleEscape implements Escape {

    /**
     * (non-Javadoc)
     * @see com.skymobi.webframework.orm.mybatis.escape.Escape#escape(java.lang.String)
     */
    @Override
    public String escape(String value) {
        return null;
    }

    /**
     * (non-Javadoc)
     * @see com.skymobi.webframework.orm.mybatis.escape.Escape#unescape(java.lang.String)
     */
    @Override
    public String unescape(String value) {
        return null;
    }

}
