/*
 * 文件名称: EscapeFactory.java
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
 * 转义工厂。
 * 
 * @author Allen.Hu / 2013-3-18
 */
public interface EscapeFactory {

    /**
     * <p>
     * 根据数据库类型获取该数据库转义的方言。
     * </p>
     *
     * @param dbType
     * @return
     */
    Escape getEscape(String dbType);
}
