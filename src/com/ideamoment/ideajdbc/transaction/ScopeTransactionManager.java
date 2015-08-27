/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.transaction;

import java.util.Map;

/**
 * @author Chinakite
 *
 */
public class ScopeTransactionManager {
	
	public static void increaseRefCount() {
		Map<String, ScopeTransaction> existsTxMap = ScopeTransactionThreadLocal.allTransactions();
		if(existsTxMap != null) {
			for(String dbName : existsTxMap.keySet()) {
				ScopeTransaction scopeTx = ScopeTransactionThreadLocal.get(dbName);
				if(scopeTx != null) {
					scopeTx.increaseCount();
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
					scopeTx.reduceCount();
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
