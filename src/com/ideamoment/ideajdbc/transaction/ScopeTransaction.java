/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.transaction;

import java.sql.Connection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Chinakite
 *
 */
public class ScopeTransaction implements Transaction{
	
	private static final Logger logger = LoggerFactory.getLogger(ScopeTransaction.class);
	
	private ScopeTransaction suspendedTransaction;
	
	private Transaction transaction;
	
	private TxStrategy strategy;
	
	private String dbName;
	
	/**
	 * @return the strategy
	 */
	public TxStrategy getStrategy() {
		return strategy;
	}

	/**
	 * @param strategy the strategy to set
	 */
	public void setStrategy(TxStrategy strategy) {
		this.strategy = strategy;
	}

	/**
	 * @return the suspendedTransaction
	 */
	public ScopeTransaction getSuspendedTransaction() {
		return suspendedTransaction;
	}

	/**
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * 是否嵌套事务
	 */
	private boolean nested = false;
	
	/**
	 * 是否是新创建的事务
	 */
	private boolean newtx = true;
	
	/**
	 * 引用次数
	 */
	private int refCount = 0;
	
	public ScopeTransaction(){
	}
	
	public ScopeTransaction(Transaction transaction, ScopeTransaction suspendedTransaction, TxStrategy strategy, String dbName) {
		this.transaction = transaction;
		this.suspendedTransaction = suspendedTransaction;
		this.strategy = strategy;
		this.dbName = dbName;
	}
	
	public void onError(Throwable e) {
		if(isRollbackThrowable(e)) {
			transaction.rollback();
			logger.debug("Transaction rollback on error.");
		}else{
			transaction.commit();
			logger.debug("Transaction commit on error.");
		}
	}
	
	public void commit() {
		if(isActive()){
			transaction.commit();
		}
	}
	
	public void onFinally() {
		TxType txType = strategy.getType();
		if(txType == TxType.REQUIRED || txType == TxType.REQUIRES_NEW) {
			if(!this.nested && this.refCount == 0) {
				this.transaction.end();
				if(suspendedTransaction != null) {
					ScopeTransactionThreadLocal.set(this.dbName, this.suspendedTransaction);
				}else{
				    ScopeTransactionThreadLocal.set(this.dbName, null);
				}
				logger.debug("Transaction end on final.");
			}
		}
	}
	
	public void onSuccess() {
		TxType txType = strategy.getType();
		if(txType == TxType.REQUIRED || txType == TxType.REQUIRES_NEW) {
			if(!this.nested && this.refCount == 0) {
				this.transaction.commit();
				logger.debug("Transaction commit on success.");
			}
		}
	}
	
	/**
	 * Return true if this throwable should cause a rollback to occur.
	 */
	private boolean isRollbackThrowable(Throwable e) {

		if (e instanceof Error){
			return true;
		}
		
		List noRollbackFor = strategy.getNoRollbackFor();
																																																																																																																																																																																																																																																																																																																																																													
		if (noRollbackFor != null){
			for (int i = 0; i < noRollbackFor.size(); i++) {
				if (noRollbackFor.get(i).equals(e.getClass())) {
					return false;
				}
			}
		}

		List rollbackFor = strategy.getRollbackFor();
		
		if (rollbackFor != null){
			for (int i = 0; i < rollbackFor.size(); i++) {
				if (rollbackFor.get(i).equals(e.getClass())) {
					return true;
				}
			}
		}
		
		return true;
		
		//TODO: 这里想区分RuntimeException与Checked Exception，但是还没想好
//		if (e instanceof RuntimeException) {
//			return true;
//		} else {
//			return true;
//		}
	}

	/**
	 * @return the nested
	 */
	public boolean isNested() {
		return nested;
	}

	/**
	 * @param nested the nested to set
	 */
	public void setNested(boolean nested) {
		this.nested = nested;
	}

	@Override
	public Connection getConnection() {
		return this.transaction.getConnection();
	}

	@Override
	public void rollback() {
		if(isActive()) {
			this.transaction.rollback();
		}
	}

	@Override
	public void end() {
		if(isActive()) {
			this.transaction.end();
			TransactionThreadLocal.replace(dbName, this.suspendedTransaction);
		}
	}

	@Override
	public boolean isActive() {
		return this.transaction.isActive();
	}

	@Override
	public String getDbName() {
		return this.dbName;
	}

	/**
	 * @param suspendedTransaction the suspendedTransaction to set
	 */
	public void setSuspendedTransaction(ScopeTransaction suspendedTransaction) {
		this.suspendedTransaction = suspendedTransaction;
	}

	/**
	 * @param transaction the transaction to set
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	/**
	 * @param dbName the dbName to set
	 */
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	/**
	 * @return the newtx
	 */
	public boolean isNewtx() {
		return newtx;
	}

	/**
	 * @param newtx the newtx to set
	 */
	public void setNewtx(boolean newtx) {
		this.newtx = newtx;
	}

	/**
	 * @return the refCount
	 */
	public int getRefCount() {
		return refCount;
	}

	/**
	 * @param refCount the refCount to set
	 */
	public void setRefCount(int refCount) {
		this.refCount = refCount;
	}
	
	/**
	 * 增加引用次数
	 */
	public void increaseCount() {
		this.refCount++;
	}
	
	/**
	 * 减少引用次数
	 */
	public void reduceCount() {
		this.refCount--;
	}
}
