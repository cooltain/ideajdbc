/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * 由于IdeaJdbc的特殊性，事务策略总是对一组事务同时生效，生效范围是代码块。
 * 代码块可以是一段程序，一个方法等。
 * 
 * @author Chinakite
 *
 */
public class TxStrategy {
	
	public static TxStrategy DEFAULT_TX_STRATEGY = new TxStrategy();
	static {
		DEFAULT_TX_STRATEGY.setType(TxType.REQUIRED);
		DEFAULT_TX_STRATEGY.setReadOnly(false);
		DEFAULT_TX_STRATEGY.setIsolation(TxIsolation.READ_COMMITED);
		DEFAULT_TX_STRATEGY.addRollbackFor(Throwable.class);
	}
	
	/**
	 * 事务控制类型
	 */
	private TxType type;
	
	/**
	 * 事务隔离级别
	 */
	private TxIsolation isolation;
	
	/**
	 * 明确声明需要回滚的异常
	 */
	private List<Class<? extends Throwable>> rollbackFor = new ArrayList<Class<? extends Throwable>>();
	
	/**
	 * 明确声明不需要回滚的异常
	 */
	private List<Class<? extends Throwable>> noRollbackFor = new ArrayList<Class<? extends Throwable>>();
	
	/**
	 * 是否是只读事务
	 */
	private boolean readOnly = false;

	/**
	 * @return the type
	 */
	public TxType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(TxType type) {
		this.type = type;
	}

	/**
	 * @return the isolation
	 */
	public TxIsolation getIsolation() {
		return isolation;
	}

	/**
	 * @param isolation the isolation to set
	 */
	public void setIsolation(TxIsolation isolation) {
		this.isolation = isolation;
	}

	/**
	 * @return the rollbackFor
	 */
	public List<Class<? extends Throwable>> getRollbackFor() {
		return rollbackFor;
	}

	/**
	 * @param rollbackFor the rollbackFor to set
	 */
	public void setRollbackFor(List<Class<? extends Throwable>> rollbackFor) {
		this.rollbackFor = rollbackFor;
	}

	/**
	 * @return the noRollbackFor
	 */
	public List<Class<? extends Throwable>> getNoRollbackFor() {
		return noRollbackFor;
	}

	/**
	 * @param noRollbackFor the noRollbackFor to set
	 */
	public void setNoRollbackFor(List<Class<? extends Throwable>> noRollbackFor) {
		this.noRollbackFor = noRollbackFor;
	}

	/**
	 * @return the readOnly
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * @param readOnly the readOnly to set
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}
	
	/**
	 * 添加一个需要回滚的异常到异常列表
	 * 
	 * @param throwableClass
	 */
	public void addRollbackFor(Class throwableClass) {
		if(!this.rollbackFor.contains(throwableClass)) {
			this.rollbackFor.add(throwableClass);
		}
	}
	
	/**
	 * 添加一个不需要回滚的异常到异常列表
	 * 
	 * @param throwableClass
	 */
	public void addNoRollbackFor(Class throwableClass) {
		if(!this.noRollbackFor.contains(throwableClass)) {
			this.noRollbackFor.add(throwableClass);
		}
	}
}
