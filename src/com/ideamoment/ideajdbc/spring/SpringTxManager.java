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

import com.ideamoment.ideajdbc.configuration.DbConfig;
import com.ideamoment.ideajdbc.transaction.DefaultTxManager;
import com.ideamoment.ideajdbc.transaction.Transaction;


/**
 * @author Chinakite
 * @version 2013-5-4
 * @since 0.1
 */
public class SpringTxManager extends DefaultTxManager {
	
	public SpringTxManager(DbConfig config) {
		super(config);
	}

	/* (non-Javadoc)
	 * @see com.ideamoment.rainbow.transaction.TxManager#createTransaction()
	 */
	@Override
	public Transaction createTransaction() {
		ConnectionHolder holder;
		try {
			Connection connection = dataSource.getConnection();
			holder = new ConnectionHolder(connection);
			connection.setAutoCommit(false);
			Transaction transaction = new SpringJdbcTransaction(this.dbName, holder, dataSource);
			TransactionSynchronizationManager.bindResource(dataSource, holder);
			return transaction;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
