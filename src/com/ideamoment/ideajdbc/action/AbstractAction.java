/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.action;

import com.ideamoment.ideajdbc.exception.IdeaJdbcException;
import com.ideamoment.ideajdbc.exception.IdeaJdbcExceptionCode;
import com.ideamoment.ideajdbc.transaction.Transaction;

/**
 * Action的抽象类，主要有一些Action公用的方法。
 * 
 * @author Chinakite Zhang
 * @version 2010-10-20
 * @since 0.1
 */
public abstract class AbstractAction<T> implements Action<T>{
	/**
	 * 检查事务是否有效，如果无效抛出Rainbow异常。
	 * 
	 * @param transaction 事务
	 */
	public void checkTransaction(Transaction transaction) {
		//检查事务
		if(transaction == null || !transaction.isActive()) {
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_NOTACTIVE_ERR, "The transaction is not alive when query executing.");
		}
	}
}
