/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.action;

import com.ideamoment.ideadata.annotation.DataItemType;

/**
 * @author Chinakite
 *
 */
public interface Command {
	
	/**
	 * 执行sql
	 * 
	 * @return
	 */
	public int execute();
	
	/**
	 * 为查询设置参数的值。
	 * 
	 * @param index 参数位置索引，同JDBC中的，即是第几个问号，不过不同的是此处索引是从0开始的。
	 * @param value 参数的值
	 * @return 当前Command对象
	 */
	public Command setParameter(int index, Object value);
	
	/**
	 * 为Command设置参数的值。
	 * 
	 * @param index 参数位置索引，同JDBC中的，即是第几个问号，不过不同的是此处索引是从0开始的。
	 * @param value 参数的值
	 * @param type JDBC字段的类型，一旦指定了此参数，那么IdeaJdbc在使用setParameter时则按指定的类型调用相应的API
	 * @return 当前Command对象
	 */
	public Command setParameter(int index, Object value, DataItemType type);
	
	/**
	 * 为查询设置参数的值。
	 * 
	 * @param paramName 参数名称占位符
	 * @param value 参数的值
	 * @return 当前Command对象
	 */
	public Command setParameter(String paramName, Object value);
	
	/**
	 * 为查询设置参数的值。
	 * 
	 * @param paramName 参数名称占位符
	 * @param value 参数的值
	 * @param type JDBC字段的类型，一旦指定了此参数，那么IdeaJdbc在使用setParameter时则按指定的类型调用相应的API
	 * @return 当前Command对象
	 */
	public Command setParameter(String paramName, Object value, DataItemType type);
}
