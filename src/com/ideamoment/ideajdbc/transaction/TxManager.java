/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.transaction;

import javax.sql.DataSource;

/**
 * 事务管理器。
 * 主要负责创建新事务。供Channel调用。
 * 
 * @author Chinakite Zhang
 * @version 2010/10/14
 * @since 0.1
 */
public interface TxManager {
	/**
	 * 创建一个新事务。
	 * 
	 * @return Transaction 新事务
	 */
	public Transaction createTransaction();
	
	/**
	 * 取数据源。
	 * 
	 * @return DataSource 数据源
	 */
	public DataSource getDataSource();
	
	/**
	 * 取DB名称
	 * 
	 * @return String DB名称
	 */
	public String getDbName();
	
}