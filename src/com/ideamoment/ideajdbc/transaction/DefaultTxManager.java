/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.transaction;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.ideamoment.ideajdbc.configuration.DbConfig;

/**
 * @author Chinakite
 *
 */
public class DefaultTxManager implements TxManager {

	protected DataSource dataSource;		//数据源
	
	protected final String dbName;		//channel名称
	
	public DefaultTxManager(DbConfig dbConfig) {
		this.dataSource = dbConfig.getDataSource();
		this.dbName = dbConfig.getName();
	}
	
	/* (non-Javadoc)
	 * @see com.ideamoment.ideajdbc.transaction.TxManager#createTransaction()
	 */
	@Override
	public Transaction createTransaction() {
		return createTransaction(false, TxIsolation.READ_COMMITED);
	}
	
	public Transaction createTransaction(boolean readOnly, TxIsolation isolation) {
		try {
			Connection connection = dataSource.getConnection();
			connection.setReadOnly(readOnly);
			connection.setTransactionIsolation(isolation.getLevel());
			connection.setAutoCommit(false);
			Transaction transaction = new JdbcTransaction(this.dbName, connection);
			return transaction;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.ideamoment.ideajdbc.transaction.TxManager#getDataSource()
	 */
	@Override
	public DataSource getDataSource() {
		return this.dataSource;
	}

	/* (non-Javadoc)
	 * @see com.ideamoment.ideajdbc.transaction.TxManager#getDbName()
	 */
	@Override
	public String getDbName() {
		return this.dbName;
	} 

}
