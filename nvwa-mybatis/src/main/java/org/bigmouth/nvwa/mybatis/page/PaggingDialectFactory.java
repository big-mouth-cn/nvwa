/*
 * 文件名称: PaggingDialectFactory.java
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

/**
 * 分页查询方言配置工厂。 可根据不同的数据库类型返回不同的分页查询接口实现。
 * 
 * @author Allen.Hu / 2012-6-29
 * @since SkyMarket 1.0
 */
public interface PaggingDialectFactory {

    /**
     * 根据不同的数据库类型返回不同的分页查询接口实现。
     * 
     * @param dbType 数据库类型
     * @return 分页查询方言实现
     * @author Allen.Hu / 2012-6-29
     * @since SkyMarket 1.0
     */
    public PaggingDialect getPaggingDialect(String dbType);
}
