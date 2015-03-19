/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.transaction;

import java.util.HashMap;
import java.util.Set;

import com.ideamoment.ideajdbc.exception.IdeaJdbcException;
import com.ideamoment.ideajdbc.exception.IdeaJdbcExceptionCode;

/**
 * Transaction HashMap。
 * 
 * @author Chinakite Zhang
 * @version 2010/10/14
 * @since 0.1
 */
public class TransactionMap {
	/**
     * 使用channel名称为key的Map, 里面存储了TxState
     */
    HashMap<String, TxState> map = new HashMap<String, TxState>();
    
    /**
     * 根据db名称返回TxState.
     */
    public TxState getState(String dbName) {
        
        TxState state = (TxState)map.get(dbName);
        if (state == null){
        	state = new TxState();
            map.put(dbName, state);
        }
        return state;
    }
    
    public Set<String> keySet() {
    	return map.keySet();
    }
    
    /**
     * 事务配合了状态以后的包装类.
     */
    public static class TxState {

    	Transaction transaction;
        
    	/**
    	 * 获取事务。
    	 * 
    	 * @return Transaction 事务
    	 */
        public Transaction get() {
            return transaction;
        }
        
        /**
         * 将当前线程的事务设置为一个新值。
         * 
         * @param trans 事务
         */
        public void set(Transaction trans) {
            
        	if (transaction != null && transaction.isActive()){
        		String m = "The existing transaction is still active?";
                throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_SET_IN_THREAD_ERR, "ThreadLocal set transaction error : "+m);
            }
            transaction = trans;
        }


        /**
         * 提交事务.
         */
        public void commit() {
        	transaction.commit();
        	transaction = null;
        }

        /**
         * 回滚事务.
         */
        public void rollback() {
        	transaction.rollback();
        	transaction = null;
        }
        
        /**
         * 结束事务.
         */
        public void end() {
        	if (transaction != null){
        		transaction.end();
            	transaction = null;
        	}
        }
        
        /**
         * 覆盖事务。
         */
        public void replace(Transaction trans) {
            transaction = trans;
        }
    }
}
