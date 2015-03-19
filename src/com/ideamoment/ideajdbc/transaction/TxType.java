/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.transaction;

/**
 * 事务控制类型
 * 
 * @author Chinakite
 *
 */
public enum TxType {
	/**
	 * 当前线程已开启事务则使用已有事务,没有就新开启一个.
	 * 默认选项.
	 */
	REQUIRED,

	/**
	 * 运行此方法时，事务必须是已经开启的，不然抛出IdeaJdbcException.
	 */
	MANDATORY,

	/**
	 * 如果有事务则使用已有事务，如果没有则不使用事务
	 */
	SUPPORTS,

	/**
	 * 总是开启新事务，不管是不是已有事务
	 */
	REQUIRES_NEW,

	/**
	 * 如果已有事务,则将事务挂起. 不使用事务
	 */
	NOT_SUPPORTED,

	/**
	 * 如果已有事务，则抛出异常IdeaJdbcException
	 */
	NEVER;
}
