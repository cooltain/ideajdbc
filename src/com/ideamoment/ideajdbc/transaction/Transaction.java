/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.transaction;

import java.sql.Connection;

/**
 * 事务类。
 * 
 * @author Chinakite Zhang
 * @version 2010/10/14
 * @since 0.1
 */
public interface Transaction {
	/**
	 * "读提交"，意思就是语句提交以后即执行了COMMIT以后别的事务就能读到这个改变。
	 * 等同于 java.sql.Connection.TRANSACTION_READ_COMMITTED.
	 */
	public static final int READ_COMMITTED = java.sql.Connection.TRANSACTION_READ_COMMITTED;

	/**
	 * "读未提交"，意思就是即使一个更新语句没有提交，但是别的事务可以读到这个改变.这是很不安全的。
	 * 等同于 java.sql.Connection.TRANSACTION_READ_UNCOMMITTED.
	 */
	public static final int READ_UNCOMMITTED = java.sql.Connection.TRANSACTION_READ_UNCOMMITTED;

	/**
	 * "可以重复读"，这是说在同一个事务里面先后执行同一个查询语句的时候,得到的结果是一样的。
	 * 等同于 java.sql.Connection.TRANSACTION_REPEATABLE_READ.
	 */
	public static final int REPEATABLE_READ = java.sql.Connection.TRANSACTION_REPEATABLE_READ;

	/**
	 * "串行化"，意思是说这个事务执行的时候不允许别的事务并发执行。
	 * 等同于 java.sql.Connection.TRANSACTION_SERIALIZABLE.
	 */
	public static final int SERIALIZABLE = java.sql.Connection.TRANSACTION_SERIALIZABLE;
	
	/**
	 * 从事务中得到数据库连接（java.sql.Connection）
	 * 
	 * @return Connection 数据库连接
	 */
	public Connection getConnection();
	
	/**
	 * 提交事务.
	 */
	public void commit();
	
	/**
	 * 回滚事务.
	 */
	public void rollback();
	
	/**
	 * 如果事务仍然活动则执行rollback方法，否则什么都不做.
	 */
	public void end();
	
	/**
	 * 获取事务是否活动.
	 * 
	 * @return boolean 如果活动返回true，否则返回false
	 */
	public boolean isActive();
	
	/**
	 * 获取DB名称
	 * 
	 * @return DB名称
	 */
	public String getDbName();
}
