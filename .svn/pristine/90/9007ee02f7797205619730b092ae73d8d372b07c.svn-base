/*
 * 文件名称: Service.java
 * 版权信息: Copyright 2005-2013 SKY-MOBI Inc. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: Allen.Hu
 * 修改日期: 2013-3-8
 * 修改内容: 
 */
package org.bigmouth.nvwa.mybatis.generator;

import java.util.List;

import org.bigmouth.nvwa.mybatis.page.PageInfo;

public abstract class Service<T, Condition, PK, DAO extends Dao<T, Condition, PK>> implements Dao<T, Condition, PK> {

    protected abstract DAO getDao();

    @Override
    public int countByCondition(Condition condition) {
        return getDao().countByCondition(condition);
    }

    @Override
    public void deleteByCondition(Condition condition) {
        getDao().deleteByCondition(condition);
    }

    @Override
    public void deleteByPrimaryKey(PK id) {
        getDao().deleteByPrimaryKey(id);
    }

    @Override
    public void insert(T record) {
        getDao().insert(record);
    }

    @Override
    public void insertSelective(T record) {
        getDao().insertSelective(record);
    }

    @Override
    public List<T> selectByCondition(Condition condition) {
        return getDao().selectByCondition(condition);
    }

    @Override
    public List<T> selectByCondition(PageInfo<T> pageInfo, Condition condition) {
        return getDao().selectByCondition(pageInfo, condition);
    }

    @Override
    public T selectByPrimaryKey(PK id) {
        return getDao().selectByPrimaryKey(id);
    }

    @Override
    public void updateByConditionSelective(T record, Condition condition) {
        getDao().updateByConditionSelective(record, condition);
    }

    @Override
    public void updateByCondition(T record, Condition condition) {
        getDao().updateByCondition(record, condition);
    }

    @Override
    public void updateByPrimaryKeySelective(T record) {
        getDao().updateByPrimaryKeySelective(record);
    }

    @Override
    public void updateByPrimaryKey(T record) {
        getDao().updateByPrimaryKey(record);
    }

}
