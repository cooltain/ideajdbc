/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.transaction;

/**
 * @author Chinakite
 *
 */
public class TransactionThreadLocal {
	/**
	 * ThreadLocal型的变量，用于存放当前线程使用的事务。
	 */
	private static ThreadLocal<TransactionMap> local = new ThreadLocal<TransactionMap>() {
        protected synchronized TransactionMap initialValue() {
            return new TransactionMap();
        }
    };
    
    /**
     * 私有构造方法。防止外部调用。
     */
    private TransactionThreadLocal() {
    }
    
    /**
     * 根据给定的db名称取得当前的包含状态的事务（TxState）。
     * 当然，只是当前线程的。
     * 
     * @param dbName db名称
     * 
     * @return TxState 含状态的事务
     */
    private static TransactionMap.TxState getState(String dbName){
        return local.get().getState(dbName);
    }
    
    /**
     * 根据给定的db名称取得当前的事务
     * 
     * @param dbName db名称
     * 
     * @return Transaction 事务
     */
    public static Transaction get(String dbName) {
        return getState(dbName).transaction;
    }

    /**
     * 设置指定名称的db的事务为一个新的事务。
     * 
     * @param dbName db名称
     * @param trans 新的事务
     */
    public static void set(String dbName, Transaction trans) {
        getState(dbName).set(trans);
    }
    
    /**
     * 提交当前的事务。
     * 
     * @param dbName db名称
     */
    public static void commit(String dbName) {
        getState(dbName).commit();
    }

    /**
     * 回滚当前的事务。
     * 
     * @param dbName db名称
     */
    public static void rollback(String dbName) {
        getState(dbName).rollback();
    }

    /**
     * 如果事务还没有被提交，那么回滚。
     * 
     * @param dbName db名称
     */
    public static void end(String dbName) {
        getState(dbName).end();
    }

    /**
     * 用新的事务对象覆盖原事务对象。
     * 
     * @param dbName
     * @param trans
     */
    public static void replace(String dbName, JdbcTransaction trans) {
        getState(dbName).replace(trans);
    }
}
