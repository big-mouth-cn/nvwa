/*
 * 文件名称: MyBatisDaoSupport.java
 * 版权信息: Copyright 2005-2012 SKY-MOBI Inc. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2012-6-29
 * 修改内容: 
 */
package org.bigmouth.nvwa.mybatis.support;

import org.apache.ibatis.session.SqlSessionFactory;

/**
 * MyBatis Dao 基类支持。
 * 
 * @author Allen.Hu / 2012-6-29
 * @since SkyMarket 1.0
 */
public class MyBatisDaoSupport {

    /** MyBatis Session 工厂 */
    protected SqlSessionFactory sqlSessionFactory;
    
    // -------------------------------- 以下为Getter/Setter方法 -------------------------------- //

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

}
