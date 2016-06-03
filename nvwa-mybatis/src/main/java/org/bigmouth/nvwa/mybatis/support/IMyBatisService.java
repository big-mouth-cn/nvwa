/*
 * 文件名称: IMyBatisService.java
 * 版权信息: Copyright 2005-2013 SKY-MOBI Inc. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2013-2-1
 * 修改内容: 
 */
package org.bigmouth.nvwa.mybatis.support;

import java.io.Serializable;

/**
 * <p>
 * 业务层的基类接口。
 * </p>
 * 该接口封装了一些基础公共的处理方法，比如CURD，分页查询，指定SQL语句查询等。 
 * 如果需要添加其他业务处理方法，请在子接口中添加。
 * 
 * @param <T> {@link java.lang.Object}
 * @param <PK> {@link com.skymobi.webframework.orm.mybatis.support.IMyBatisDao}
 * @author Allen.Hu / 2013-2-1
 */
public interface IMyBatisService<T, PK extends Serializable> extends IMyBatisDao<T, PK> {

}
