/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.action;

import java.util.List;
import java.util.Map;

import com.ideamoment.ideadata.annotation.DataItemType;

/**
 * @author Chinakite
 *
 */
public interface Query<T> extends Action<T> {
	
	/**
	 * 根据当前查询动作信息执行查询，如果查询动作中没有指定实体类，则将返回的结果映射为一个map的列表。
	 * 如果是单表，以字段为key，如果是多表，则以表名.字段名为key。
	 * 
	 * @return List 查询结果列表
	 */
	public List list();
	
	/**
	 * <p>返回分页查询结果</p>
	 * <b>注意：</b>这个方法适合于查询小表的分页，对于大数据量的查询需要自行优化sql语句，封装相应的方法来实现。
	 * 
	 * @return
	 */
	public Page page(int currentPage, int pageSize);
	
	/**
	 * 根据当前查询动作信息执行查询，如果查询动作中没有指定实体类，则将返回的结果映射为一个map的Map实例，实例中以id为key，数据对象为value。
	 * 在内部map中如果是单表，以字段为key，如果是多表，则以表名.字段名为key。
	 * 
	 * @return Map实例
	 */
//	public Map map();
	
	/**
	 * 将查询出来的结果转换为entityClass类返回
	 * 
	 * @param entityClass 实体类
	 * @return 结果列表
	 */
	public List<T> listTo(Class<T> entityClass);
	
	/**
     * 将查询出来的结果转换为entityClass类返回
     * 
     * @param entityClass 实体类
     * @param ignoreAnnotation 是否忽略注解作用, 默认为false
     * @return 结果列表
     */
    public List<T> listTo(Class<T> entityClass, boolean ignoreAnnotation);
	
	/**
	 * <p>返回分页查询结果</p>
	 * <b>注意：</b>这个方法适合于查询小表的分页，对于大数据量的查询需要自行优化sql语句，封装相应的方法来实现。
	 * 
	 * @param entityClass
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public Page<T> pageTo(Class<T> entityClass, int currentPage, int pageSize);
	
	/**
     * <p>返回分页查询结果</p>
     * <b>注意：</b>这个方法适合于查询小表的分页，对于大数据量的查询需要自行优化sql语句，封装相应的方法来实现。
     * 
     * @param entityClass
     * @param currentPage
     * @param pageSize
     * @param ignoreAnnotation 是否忽略注解作用, 默认为false
     * @return
     */
    public Page<T> pageTo(Class<T> entityClass, int currentPage, int pageSize, boolean ignoreAnnotation);
	
	/**
	 * <p>返回分页查询结果</p>
	 * <b>注意：</b>这个方法适合于查询小表的分页，对于大数据量的查询需要自行优化sql语句，封装相应的方法来实现。
	 * 
	 * @param entityClass
	 * @param entityAlias
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public Page<T> pageTo(Class<T> entityClass, String entityAlias, int currentPage, int pageSize);
	
	/**
     * <p>返回分页查询结果</p>
     * <b>注意：</b>这个方法适合于查询小表的分页，对于大数据量的查询需要自行优化sql语句，封装相应的方法来实现。
     * 
     * @param entityClass
     * @param entityAlias
     * @param currentPage
     * @param pageSize
     * @param ignoreAnnotation 是否忽略注解作用, 默认为false
     * @return
     */
    public Page<T> pageTo(Class<T> entityClass, String entityAlias, int currentPage, int pageSize, boolean ignoreAnnotation);
	
	/**
	 * 将查询出来的结果转换为entityClass类返回
	 * 
	 * @param entityClass 实体类
	 * @param entityAlias 主实体类的别名
	 * @return 结果列表
	 */ 
	public List<T> listTo(Class<T> entityClass, String entityAlias);
	
	/**
     * 将查询出来的结果转换为entityClass类返回
     * 
     * @param entityClass 实体类
     * @param entityAlias 主实体类的别名
     * @param ignoreAnnotation 是否忽略注解作用, 默认为false
     * @return 结果列表
     */ 
    public List<T> listTo(Class<T> entityClass, String entityAlias, boolean ignoreAnnotation);
	
	/**
	 * 根据当前查询返回单条结果。
	 * 
	 * @return 查询结果（单条）
	 */
	public T unique();
	
	/**
	 * 根据当前查询返回单条结果。
	 * 
	 * @param entityClass 实体类
	 * @return 查询结果（单条）
	 */
	public T uniqueTo(Class<T> entityClass);
	
	/**
	 * 将返回结果的别名为tableAlias表的字段映射到propName属性中去
	 * 
	 * @param propName 属性名
	 * @param tableAlias	查询结果的表别名
	 */
	public Query populate(String propName, String tableAlias);
	
	/**
	 * 根据当前查询动作信息执行范围查询。
	 * 
	 * @return List 查询结果列表
	 */
	public List<T> rangeList(int start, int end);
	
	/**
	 * 根据当前查询动作信息执行范围查询。
	 * 
	 * @return List 查询结果列表
	 */
	public List<T> rangeListTo(Class<T> entityClass, int start, int end);
	
	/**
	 * 根据当前查询动作信息执行范围查询。
	 * 
	 * @return List 查询结果列表
	 */
	public List<T> rangeListTo(Class<T> entityClass, String entityAlias, int start, int end);
	
	/**
	 * 返回唯一值查询,如果查询结果不是唯一值则会抛出异常。
	 * 
	 * @return
	 */
	public Object uniqueValue();
	
	/**
	 * 为查询设置参数的值。
	 * 主要用于SQL查询和NamedQuery查询方式。
	 * 
	 * @param index 参数位置索引，同JDBC中的，即是第几个问号，不过不同的是此处索引是从0开始的。
	 * @param value 参数的值
	 * @return 当前Query对象
	 */
	public Query<T> setParameter(int index, Object value);
	
	/**
	 * 为查询设置参数的值。
	 * 主要用于SQL查询和NamedQuery查询方式。
	 * 
	 * @param index 参数位置索引，同JDBC中的，即是第几个问号，不过不同的是此处索引是从0开始的。
	 * @param value 参数的值
	 * @param type JDBC字段的类型，一旦指定了此参数，那么IdeaJdbc在使用setParameter时则按指定的类型调用相应的API
	 * @return 当前Query对象
	 */
	public Query<T> setParameter(int index, Object value, DataItemType type);
	
	/**
	 * 为查询设置参数的值。
	 * 主要用于SQL查询和NamedQuery查询方式。
	 * 
	 * @param paramName 参数名称占位符
	 * @param value 参数的值
	 * @return 当前Query对象
	 */
	public Query<T> setParameter(String paramName, Object value);
	
	/**
	 * 为查询设置参数的值。
	 * 主要用于SQL查询和NamedQuery查询方式。
	 * 
	 * @param paramName 参数名称占位符
	 * @param value 参数的值
	 * @param type JDBC字段的类型，一旦指定了此参数，那么IdeaJdbc在使用setParameter时则按指定的类型调用相应的API
	 * @return 当前Query对象
	 */
	public Query<T> setParameter(String paramName, Object value, DataItemType type);
	
	/**
	 * 获取实体指定的别名
	 * 
	 * @return the entityAlias
	 */
	public String getEntityAlias();
	
	/**
	 * 返回实体别名映射表。
	 * 
	 * @return
	 */
	public Map<String, String> getPopulates();
	
	/**
     * @return the partitionQuery
     */
    public boolean isPartitionQuery();
    
    /**
     * @param partitionQuery the partitionQuery to set
     */
    public void setPartitionQuery(boolean partitionQuery);
    
    /**
     * @return the partitionValue
     */
    public Object getPartitionValue();
    
    /**
     * @param partitionValue the partitionValue to set
     */
    public void setPartitionValue(Object partitionValue);
}
