/*
 * 文件名称: DefaultEscapeFactory.java
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

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.mybatis.DbTypes;


/**
 * 默认转义工厂实现。
 * 
 * @author Allen.Hu / 2013-3-18
 */
public class DefaultEscapeFactory implements EscapeFactory {

    /**
     * (non-Javadoc)
     * @see com.skymobi.webframework.orm.mybatis.escape.EscapeFactory#getEscape(java.lang.String)
     */
    @Override
    public Escape getEscape(String dbType) {
        Escape escape = null;
        if (StringUtils.equals(dbType, DbTypes.POSTGRESQL)) {
            escape = new PostgreSQLEscape();
        }
        else if (StringUtils.equals(dbType, DbTypes.ORACLE)) {
            escape = new OracleEscape();
        }
        return escape;
    }

}
