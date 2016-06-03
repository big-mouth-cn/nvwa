/*
 * 文件名称: IMyBatisDao.java
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

import org.apache.ibatis.annotations.Param;
import org.bigmouth.nvwa.mybatis.exception.NullColumnException;
import org.bigmouth.nvwa.mybatis.exception.NullSQLStatementException;
import org.bigmouth.nvwa.mybatis.page.PageInfo;

/**
 * <p>
 * MyBatis Dao 基类接口，动态代理Dao接口对某个对象（数据库表）的操作可直接继承该接口。
 * </p>
 * <p>
 * 该基类接口提供了一些基础的功能，比如CURD操作，分页查询，指定SQL语句查询等。 子接口无需重写这些方法，直接在MyBatis的mapper.xml实现即可。
 * </p>
 * <p>
 * 以下是一个简单的例子：
 * </p>
 * ExampleDao.java
 * 
 * <pre>
 * import com.skymobi.webframework.orm.mybatis.support.IMyBatisDao;
 * 
 * public interface ExampleDao extends IMyBatisDao&lt;Example, Long&gt; {
 *     // Override support interface
 * }
 * </pre>
 * 
 * example.mapper.xml
 * 
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8" ?>
 * &lt;!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
 * &lt;mapper namespace="ExampleDao">
 *      &lt;resultMap type="Example" id="resultMap">
 *          ....
 *      &lt;/resultMap>
 *      
 *      &lt;insert id="insert" parameterType="Example">
 *          ....
 *      &lt;/insert>
 *      
 *      &lt;update id="update" parameterType="Example">
 *          ....
 *      &lt;/update>
 *      
 *      &lt;select id="queryAll" resultMap="resultMap">
 *          ....
 *      &lt;/select>
 *      
 *      ......
 * &lt;/mapper>
 * </pre>
 * 
 * example.service.xml
 * 
 * <pre>
 * &lt;bean id="exampleDao" class="org.mybatis.spring.mapper.MapperFactoryBean">
 *      &lt;property name="sqlSessionFactory" ref="sqlSessionFactory" />
 *      &lt;property name="mapperInterface" value="ExampleDao" />
 * &lt;/bean>
 * </pre>
 * 
 * ExampleService.java
 * 
 * <pre>
 * 
 * &#064;Component
 * public class ExampleService {
 * 
 *     &#064;Autowired
 *     private ExampleDao exampleDao;
 * }
 * </pre>
 * 
 * @author Allen.Hu / 2013-2-1
 */
public interface IMyBatisDao<T, PK extends Serializable> {

    /**
     * 新增一个对象。
     * 
     * @param object
     */
    int insert(T object);

    /**
     * 删除一个对象。
     * 
     * @param id
     */
    int delete(PK id);

    /**
     * 修改一个对象。
     * 
     * @param object
     */
    int update(T object);

    /**
     * 根据唯一标识获得该对象信息。
     * 
     * @param id
     * @return
     */
    T query(PK id);

    /**
     * 无条件查询所有的对象(对于大数据量的数据库表，请慎用此方法，建议使用分页查询)。
     * 
     * @return
     */
    List<T> queryAll();

    /**
     * 根据分页信息查询对象。
     * 
     * @param pageInfo
     * @return
     */
    List<T> queryAll(PageInfo<T> pageInfo);
    
    /**
     * <p>
     * 根据查询条件返回查询的结果集。
     * </p>
     *
     * @param condition 查询条件
     * @return
     */
    List<T> queryAll(T condition);
    
    /**
     * <p>
     * 根据查询条件，分页返回查询的结果集。
     * </p>
     *
     * @param condition 查询条件
     * @param pageInfo 分页参数
     * @return
     */
    List<T> queryAll(T condition, PageInfo<T> pageInfo);

    /**
     * <p>
     * 根据指定列和指定值进行查询，并将结果返回。
     * </p>
     * 在mapper.xml中的SQL语句为：
     * 
     * <pre>
     *  &lt;select id="queryEqValue" resultMap="resultMap" parameterType="java.util.Map"&gt;
     *  SELECT ID, NAME FROM TABLE_NAME
     *  WHERE
     *      ${columnName} = #{1}
     *  &lt;/select&gt;
     * </pre>
     * 
     * @param columnName 列名——数据库表结构字段物理名称
     * @param value 值
     * @return
     * @throws NullColumnException 参数列名为<code>NULL</code>
     */
    List<T> queryEqValue(@Param("columnName") String columnName, Object value) throws NullColumnException;

    /**
     * <p>
     * 根据指定列和指定值进行模糊查询，并将结果返回。
     * </p>
     * 在mapper.xml中的SQL语句为：
     * 
     * <pre>
     *  &lt;select id="queryLikeValue" resultMap="resultMap" parameterType="java.util.Map"&gt;
     *  SELECT ID, NAME FROM TABLE_NAME
     *  WHERE
     *      ${columnName} LIKE '%'||#{1}||'%'
     *  &lt;/select&gt;
     * </pre>
     * 
     * @param columnName 列名——数据库表结构字段物理名称
     * @param value 值
     * @return
     * @throws NullColumnException 参数列名为<code>NULL</code>
     */
    List<T> queryLikeValue(@Param("columnName") String columnName, Object value) throws NullColumnException;

    /**
     * <p>
     * 根据指定SQL语句查询结果集并返回。
     * </p>
     * 在mapper.xml中的SQL语句为：
     * 
     * <pre>
     *  &lt;select id="queryBySQL" resultMap="resultMap" parameterType="java.lang.String"&gt;
     *  ${sql}
     *  &lt;/select&gt;
     * </pre>
     * 
     * 
     * @param sql 查询SQL语句 <br />
     *            需要注意的是，在查询语句中被查询的列名必须声明别名并与对象属性名称一致，否则会无法映射。
     * 
     *            <pre>
     * &quot;SELECT id as id, user_name as userName FROM tableName&quot;
     * </pre>
     * @return
     * @throws NullSQLStatementException 参数SQL语句为<code>NULL</code>
     */
    List<T> queryBySQL(@Param("sql") String sql) throws NullSQLStatementException;

    /**
     * <p>
     * 根据指定列和指定值进行查询唯一对象，并将结果返回。
     * </p>
     * 在mapper.xml中的SQL语句为：
     * 
     * <pre>
     *  &lt;select id="queryUnique" resultType="Object" parameterType="java.util.Map"&gt;
     *  SELECT ID, NAME FROM TABLE_NAME
     *  WHERE
     *      ${columnName} = #{1}
     *  &lt;/select&gt;
     * 
     * @param columnName 列名——数据库表结构字段物理名称，该列必须有唯一约束
     * @param value 值
     * @return
     * @throws NullColumnException 参数列名为<code>NULL</code>
     */
    T queryUnique(@Param("columnName") String columnName, Object value) throws NullColumnException;
}
