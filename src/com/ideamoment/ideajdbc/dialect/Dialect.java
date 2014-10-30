/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.dialect;

/**
 * @author Chinakite
 *
 */
public interface Dialect {
	/**
	 * 处理分页语句
	 * 
	 * @param sql
	 * @return
	 */
	public String pageSql(String sql);
}
