/*
 * 文件名称: MyBatisServiceSupport.java
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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bigmouth.nvwa.mybatis.exception.NullColumnException;
import org.bigmouth.nvwa.mybatis.exception.NullSQLStatementException;
import org.bigmouth.nvwa.mybatis.page.PageInfo;

/**
 * <p>业务层接口抽象实现类。</p>
 * 实现了业务基类接口中定义的接口，必须继承此抽象类再使用。
 * 
 * @see IMyBatisService
 * @see IMyBatisDao
 * @param <T> {@link java.lang.Object}
 * @param <PK> {@link java.lang.Object}
 * @param <Dao> {@link com.skymobi.webframework.orm.mybatis.support.IMyBatisDao}
 * @author Allen.Hu / 2013-2-1
 */
public abstract class MyBatisServiceSupport<T, PK extends Serializable, Dao extends IMyBatisDao<T, PK>> implements
        IMyBatisService<T, PK> {

    protected abstract Dao getDao();

    @Override
    public int insert(T object) {
        if (null == object)
            throw new NullPointerException("object");
        return getDao().insert(object);
    }

    @Override
    public int delete(PK id) {
        if (null == id)
            throw new IllegalArgumentException("id");
        return getDao().delete(id);
    }

    @Override
    public int update(T object) {
        if (null == object)
            throw new NullPointerException("object");
        return getDao().update(object);
    }

    @Override
    public T query(PK id) {
        if (id == null)
            return null;
        return getDao().query(id);
    }

    @Override
    public List<T> queryAll() {
        return getDao().queryAll();
    }

    @Override
    public List<T> queryAll(PageInfo<T> pageInfo) {
        if (null == pageInfo)
            return queryAll();
        return getDao().queryAll(pageInfo);
    }

    @Override
    public List<T> queryAll(T condition) {
        return getDao().queryAll(condition);
    }

    @Override
    public List<T> queryAll(T condition, PageInfo<T> pageInfo) {
        if (null == pageInfo)
            return queryAll(condition);
        return getDao().queryAll(condition, pageInfo);
    }

    @Override
    public List<T> queryEqValue(String columnName, Object value) throws NullColumnException {
        if (StringUtils.isBlank(columnName)) {
            throw new NullColumnException();
        }
        return getDao().queryEqValue(columnName, value);
    }

    @Override
    public List<T> queryLikeValue(String columnName, Object value) throws NullColumnException {
        if (StringUtils.isBlank(columnName)) {
            throw new NullColumnException();
        }
        return getDao().queryLikeValue(columnName, value);
    }

    @Override
    public List<T> queryBySQL(String sql) throws NullSQLStatementException {
        if (StringUtils.isBlank(sql)) {
            throw new NullSQLStatementException();
        }
        return getDao().queryBySQL(sql);
    }

    @Override
    public T queryUnique(String columnName, Object value) throws NullColumnException {
        if (StringUtils.isBlank(columnName)) {
            throw new NullColumnException();
        }
        return getDao().queryUnique(columnName, value);
    }

}
