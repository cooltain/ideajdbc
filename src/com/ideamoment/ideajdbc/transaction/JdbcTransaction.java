/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ideamoment.ideajdbc.exception.IdeaJdbcException;
import com.ideamoment.ideajdbc.exception.IdeaJdbcExceptionCode;

/**
 * @author Chinakite
 * 
 */
public class JdbcTransaction implements Transaction {
	
	private static Logger logger = LoggerFactory.getLogger(JdbcTransaction.class);

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

	public JdbcTransaction(){}
	
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
			int connHashCode = connection.hashCode();
			connection.commit();
			logger.debug("Connection [{}] commit.", connHashCode);
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
//			if(!connection.isClosed()) {
			    int connHashCode = connection.hashCode();
			    connection.rollback();
			    logger.debug("Connection [{}] rollback.", connHashCode);
//			}
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
			deactivate();
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
		    if(!connection.isClosed()) {
		        connection.close();
		    }
		} catch (SQLException e) {
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_CLOSE_ERR, "Transaction close error.", e);
		}
		connection = null;
		active = false;
	}
}
