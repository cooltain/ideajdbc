/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.transaction;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Chinakite
 *
 */
public class ScopeTransactionThreadLocal {
	/**
	 * ThreadLocal型的变量，用于存放当前线程使用的事务。
	 */
	private static ThreadLocal<Map<String, ScopeTransaction>> local = new ThreadLocal<Map<String, ScopeTransaction>>() {
        protected synchronized Map<String, ScopeTransaction> initialValue() {
            return new HashMap<String, ScopeTransaction>();
        }
    };
    
    /**
     * 私有构造方法。防止外部调用。
     */
    private ScopeTransactionThreadLocal() {
    }
    
    /**
     * 取得当前线程中打开的所有事务
     * 
     * @return
     */
    public static Map<String, ScopeTransaction> allTransactions() {
    	return local.get();
    }
    
    /**
     * 根据给定的db名称取得当前的事务
     * 
     * @param dbName db名称
     * 
     * @return Transaction 事务
     */
    public static ScopeTransaction get(String dbName) {
        return local.get().get(dbName);
    }

    /**
     * 设置指定名称的db的事务为一个新的事务。
     * 
     * @param dbName db名称
     * @param trans 新的事务
     */
    public static void set(String dbName, ScopeTransaction trans) {
        local.get().put(dbName, trans);
    }
}
