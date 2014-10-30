/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.spring;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.ideamoment.ideajdbc.exception.IdeaJdbcException;
import com.ideamoment.ideajdbc.exception.IdeaJdbcExceptionCode;
import com.ideamoment.ideajdbc.transaction.JdbcTransaction;

/**
 * @author Chinakite Zhang
 * @version 2012-8-3
 * @since 0.1
 */
public class SpringJdbcTransaction extends JdbcTransaction {

	//事务状态为非活动时的异常信息。
	private static final String illegalStateMessage = "Transaction is Inactive";
	
	private ConnectionHolder holder;
	private DataSource dataSource;
	
	private boolean newConnectionHolder;
	
	public void setHolder(ConnectionHolder holder, boolean isNew) {
		this.holder = holder;
	}

	public ConnectionHolder getHolder() {
		return holder;
	}

	public boolean isNewConnectionHolder() {
		return this.newConnectionHolder;
	}
	
	public SpringJdbcTransaction(String channelName){
		super(channelName, null);
		this.holder = null;
	}
	
	public SpringJdbcTransaction(String channelName, ConnectionHolder holder) {
		super(channelName, holder.getConnection());
		this.holder = holder;
	}
	
	public SpringJdbcTransaction(String channelName, ConnectionHolder holder, DataSource dataSource) {
		super(channelName, holder.getConnection());
		this.holder = holder;
		this.dataSource = dataSource;
	}

	public Connection getConnection() {
		return this.holder.getConnection();
	}
	
	@Override
	public void commit() {
		if (!isActive()) {
			throw new IllegalStateException(illegalStateMessage);
		}
		try {
			holder.getConnection().commit();
			deactivate();
		} catch (SQLException e) {
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_COMMIT_ERR, "Transaction commit error.", e);
		}
	}

	@Override
	public void end() {
		if (isActive()) {
			rollback();
		}
	}

	@Override
	public void rollback() {
		if (!isActive()) {
			throw new IllegalStateException(illegalStateMessage);
		}
		try {
			holder.getConnection().rollback();
			deactivate();
		} catch (SQLException e) {
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_ROLLBACK_ERR, "Transaction rollback error.", e);
		}
	}

	@Override
	protected void deactivate() {
		try {
			holder.getConnection().close();
			TransactionSynchronizationManager.unbindResource(dataSource);
		} catch (SQLException e) {
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_CLOSE_ERR, "Transaction close error.", e);
		}
		holder = null;
		active = false;
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
}
