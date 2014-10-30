/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.annotation;

/**
 * 使用SQL操作实体。
 * 
 * @author Chinakite Zhang
 * @version 2010-11-8
 * @since 0.1
 */
public @interface SqlUpdate {
	/**
	 * 为SQL命名，以方便使用
	 * 
	 * @return 查询名称
	 */
	String name();
	
	/**
	 * SQL语句
	 * 
	 * @return SQL语句
	 */
	String sql();
}
