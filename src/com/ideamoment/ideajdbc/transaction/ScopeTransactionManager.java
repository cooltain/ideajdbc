/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.transaction;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ideamoment.ideajdbc.spring.IdeaJdbcTransactionAspect;

/**
 * @author Chinakite
 *
 */
public class ScopeTransactionManager {
	
    private static final Logger logger = LoggerFactory.getLogger(ScopeTransactionManager.class);
    
	public static void increaseRefCount() {
		Map<String, ScopeTransaction> existsTxMap = ScopeTransactionThreadLocal.allTransactions();
		if(existsTxMap != null) {
			for(String dbName : existsTxMap.keySet()) {
				ScopeTransaction scopeTx = ScopeTransactionThreadLocal.get(dbName);
				if(scopeTx != null) {
					scopeTx.increaseCount();
					logger.debug("Increase [" + dbName + "] RefCount to " + scopeTx.getRefCount());
				}
			}
		}
	}
	
	public static void reduceRefCount() {
		Map<String, ScopeTransaction> existsTxMap = ScopeTransactionThreadLocal.allTransactions();
		if(existsTxMap != null) {
			for(String dbName : existsTxMap.keySet()) {
				ScopeTransaction scopeTx = ScopeTransactionThreadLocal.get(dbName);
				if(scopeTx != null) {
				    if(scopeTx.getRefCount() > 0) {
    					scopeTx.reduceCount();
    					logger.debug("Reduce [" + dbName + "] RefCount to " + scopeTx.getRefCount());
				    }
				}
			}
		}
	}
	
	public static void success() {
		Map<String, ScopeTransaction> map = ScopeTransactionThreadLocal.allTransactions();
		if(map != null) {
			for(String dbName : map.keySet()) {
				ScopeTransaction scopeTx = ScopeTransactionThreadLocal.get(dbName);
				if(scopeTx != null && !scopeTx.getStrategy().isReadOnly()) {
					scopeTx.onSuccess();
				}
			}
		}
	}
	
	public static void error(Throwable e) {
		Map<String, ScopeTransaction> map = ScopeTransactionThreadLocal.allTransactions();
		if(map != null) {
			for(String dbName : map.keySet()) {
				ScopeTransaction scopeTx = ScopeTransactionThreadLocal.get(dbName);
				if(scopeTx != null) {
				    scopeTx.onError(e);
				}
			}
		}
	}
	
	public static void end() {
		Map<String, ScopeTransaction> map = ScopeTransactionThreadLocal.allTransactions();
		if(map != null) {
			for(String dbName : map.keySet()) {
				ScopeTransaction scopeTx = ScopeTransactionThreadLocal.get(dbName);
				if(scopeTx != null) {
					scopeTx.onFinally();
				}
			}
		}
	}
}
