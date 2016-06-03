package org.bigmouth.nvwa.mybatis.generator;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.bigmouth.nvwa.mybatis.page.PageInfo;

/**
 * <p>
 * 基于MyBatis的基类数据访问接口，接口通过mybatis-generator-core-1.3.2自动生成。
 * 基类接口主要提供简单的单表CURD方法。
 * 子类接口继承后添加所符合业务需求的接口。
 * </p>
 * @param <T> 数据库表对象实体
 * @param <Condition> 数据库表对象条件实体
 * @param <PK> 主键类型
 * @author Allen.Hu / 2013-3-8
 */
public interface Dao<T, Condition, PK> {

    int countByCondition(Condition example);

    void deleteByCondition(Condition example);

    void deleteByPrimaryKey(PK id);

    void insert(T record);

    void insertSelective(T record);

    List<T> selectByCondition(Condition example);
    
    List<T> selectByCondition(PageInfo<T> pageInfo, Condition example);

    T selectByPrimaryKey(PK id);

    void updateByConditionSelective(@Param("record") T record, @Param("example") Condition example);

    void updateByCondition(@Param("record") T record, @Param("example") Condition example);

    void updateByPrimaryKeySelective(T record);

    void updateByPrimaryKey(T record);
}
