/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.spring;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.ideamoment.ideajdbc.server.Db;
import com.ideamoment.ideajdbc.server.DbManager;
import com.ideamoment.ideajdbc.transaction.Transaction;
import com.ideamoment.ideajdbc.transaction.TransactionThreadLocal;

/**
 * @author Administrator
 * @version 2012-8-6
 * @since 0.1
 */
public class IdeaJdbcTransactionManager extends
		AbstractPlatformTransactionManager {

	/* (non-Javadoc)
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#doBegin(java.lang.Object, org.springframework.transaction.TransactionDefinition)
	 */
	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition)
			throws TransactionException {
		SpringJdbcTransaction springTx = (SpringJdbcTransaction)transaction;
		Connection con = null;
		try {
			System.out.println("doBegin ... ");
			
			//如果springTx中的holder为空
			if (springTx.getHolder() == null ||
					springTx.getHolder().isSynchronizedWithTransaction()) {
				DataSource ds = getDefaultDataSource();
				Connection newCon;
					newCon = ds.getConnection();
				if (logger.isDebugEnabled()) {
					logger.debug("Acquired Connection [" + newCon + "] for JDBC transaction");
				}
				ConnectionHolder holder = new ConnectionHolder(newCon);
				springTx.setHolder(holder, true);
				TransactionSynchronizationManager.bindResource(ds, holder);
			}
			
			springTx.getHolder().setSynchronizedWithTransaction(true);
			con = springTx.getHolder().getConnection();

			Integer previousIsolationLevel = DataSourceUtils.prepareConnectionForTransaction(con, definition);
//			springTx.setPreviousIsolationLevel(previousIsolationLevel);

			// Switch to manual commit if necessary. This is very expensive in some JDBC drivers,
			// so we don't want to do it unnecessarily (for example if we've explicitly
			// configured the connection pool to set it already).
			if (con.getAutoCommit()) {
//				springTx.setMustRestoreAutoCommit(true);
				if (logger.isDebugEnabled()) {
					logger.debug("Switching JDBC Connection [" + con + "] to manual commit");
				}
				con.setAutoCommit(false);
			}
//			springTx.getHolder().setTransactionActive(true);

			int timeout = determineTimeout(definition);
			if (timeout != TransactionDefinition.TIMEOUT_DEFAULT) {
				springTx.getHolder().setTimeoutInSeconds(timeout);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#doCommit(org.springframework.transaction.support.DefaultTransactionStatus)
	 */
	@Override
	protected void doCommit(DefaultTransactionStatus status)
			throws TransactionException {
		System.out.println("doCommit ...");
		SpringJdbcTransaction springTx = (SpringJdbcTransaction)status.getTransaction();
		String channelName = springTx.getDbName();
		TransactionThreadLocal.commit(channelName);
	}

	/* (non-Javadoc)
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#doGetTransaction()
	 */
	@Override
	protected Object doGetTransaction() throws TransactionException {
		System.out.println("Get Transaction ...");
		Db defaultDb = DbManager.getInstance().getDefaultDb();
		Transaction springTx = defaultDb.beginTransaction();
		return springTx;
	}

	/* (non-Javadoc)
	 * @see org.springframework.transaction.support.AbstractPlatformTransactionManager#doRollback(org.springframework.transaction.support.DefaultTransactionStatus)
	 */
	@Override
	protected void doRollback(DefaultTransactionStatus status)
			throws TransactionException {
		System.out.println("doRollback ...");
		DataSource ds = getDefaultDataSource();
		SpringJdbcTransaction springTx = (SpringJdbcTransaction)status.getTransaction();
		String dbName = springTx.getDbName();
		TransactionThreadLocal.rollback(dbName);
	}
	
	private DataSource getDefaultDataSource() {
		return DbManager.getInstance().getDefaultDb().getDbConfig().getDataSource();
	}

}
