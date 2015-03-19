/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.transaction;


/**
 * 
 * 
 * @author Chinakite
 *
 */
public class TxStrategyThreadLocal {
	/**
	 * ThreadLocal型的变量，用于存放当前线程使用的事务。
	 */
	private static ThreadLocal<TxStrategy> local = new ThreadLocal<TxStrategy>();
    
    /**
     * 私有构造方法。防止外部调用。
     */
    private TxStrategyThreadLocal() {
    }
    
    public static TxStrategy get() {
    	return local.get();
    }
    
    public static void set(TxStrategy txStrategy) {
    	local.set(txStrategy);
    }
}
