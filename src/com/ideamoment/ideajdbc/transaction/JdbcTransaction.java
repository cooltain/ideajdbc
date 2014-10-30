/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import com.ideamoment.ideajdbc.exception.IdeaJdbcException;
import com.ideamoment.ideajdbc.exception.IdeaJdbcExceptionCode;

/**
 * @author Chinakite
 * 
 */
public class JdbcTransaction implements Transaction {

	// 事务状态为非活动时的异常信息。
	private static final String illegalStateMessage = "Transaction is Inactive";

	/**
	 * 事务的状态。
	 */
	protected boolean active;

	/**
	 * 数据库连接。
	 */
	private Connection connection;

	/**
	 * 数据库名称
	 */
	protected String dbName;

	/**
	 * 构造函数
	 * @param dbName
	 * @param connection
	 */
	public JdbcTransaction(String dbName, Connection connection) {
		this.dbName = dbName;
		this.connection = connection;
		this.active = true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ideamoment.ideajdbc.transaction.Transaction#getConnection()
	 */
	@Override
	public Connection getConnection() {
		return connection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ideamoment.ideajdbc.transaction.Transaction#commit()
	 */
	@Override
	public void commit() {
		if (!isActive()) {
			throw new IllegalStateException(illegalStateMessage);
		}
		try {
			connection.commit();
			deactivate();
		} catch (SQLException e) {
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_COMMIT_ERR, "Transaction commit error.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ideamoment.ideajdbc.transaction.Transaction#rollback()
	 */
	@Override
	public void rollback() {
		if (!isActive()) {
			throw new IllegalStateException(illegalStateMessage);
		}
		try {
			connection.rollback();
			deactivate();
		} catch (SQLException e) {
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_ROLLBACK_ERR, "Transaction rollback error.", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ideamoment.ideajdbc.transaction.Transaction#end()
	 */
	@Override
	public void end() {
		if (isActive()) {
			rollback();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ideamoment.ideajdbc.transaction.Transaction#getChannelName()
	 */
	@Override
	public String getDbName() {
		return this.dbName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ideamoment.ideajdbc.transaction.Transaction#isActive()
	 */
	@Override
	public boolean isActive() {
		return active;
	}
	
	/**
	 * 将事务的状态置为 <b>非活动</b>
	 */
	protected void deactivate() {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_CLOSE_ERR, "Transaction close error.", e);
		}
		connection = null;
		active = false;
	}
}
