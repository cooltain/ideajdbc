/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ideamoment.ideajdbc.action.Command;
import com.ideamoment.ideajdbc.action.DeleteAction;
import com.ideamoment.ideajdbc.action.Query;
import com.ideamoment.ideajdbc.action.SaveAction;
import com.ideamoment.ideajdbc.action.SqlCommand;
import com.ideamoment.ideajdbc.action.SqlQueryAction;
import com.ideamoment.ideajdbc.action.UpdateAction;
import com.ideamoment.ideajdbc.configuration.DbConfig;
import com.ideamoment.ideajdbc.description.JdbcEntityDescription;
import com.ideamoment.ideajdbc.description.JdbcEntityDescriptionFactory;
import com.ideamoment.ideajdbc.exception.IdeaJdbcException;
import com.ideamoment.ideajdbc.exception.IdeaJdbcExceptionCode;
import com.ideamoment.ideajdbc.transaction.ScopeTransaction;
import com.ideamoment.ideajdbc.transaction.ScopeTransactionManager;
import com.ideamoment.ideajdbc.transaction.ScopeTransactionThreadLocal;
import com.ideamoment.ideajdbc.transaction.Transaction;
import com.ideamoment.ideajdbc.transaction.TxCallable;
import com.ideamoment.ideajdbc.transaction.TxIsolation;
import com.ideamoment.ideajdbc.transaction.TxManager;
import com.ideamoment.ideajdbc.transaction.TxRunnable;
import com.ideamoment.ideajdbc.transaction.TxStrategy;
import com.ideamoment.ideajdbc.transaction.TxStrategyThreadLocal;
import com.ideamoment.ideajdbc.transaction.TxType;

/**
 * 每个Db对应了一个数据源。这里将提供数据操作的所有接口。
 * IdeaJdbc里方便使用的静态方法都是Channel的包装。
 * 
 * @author Chinakite
 *
 */
public class Db {
	
	private static final Logger logger = LoggerFactory.getLogger(Db.class);
	
	/**
	 * 数据库名称
	 * 
	 */
	private String name;
	
	/**
	 * 数据库配置信息
	 */
	private DbConfig dbConfig;
	
	/**
	 * 事务管理器
	 */
	private TxManager txManager;

	/**
	 * 构造函数
	 * @param dbConfig
	 */
	public Db(DbConfig dbConfig) {
		this.dbConfig = dbConfig;
		this.name = dbConfig.getName();
	}
	
	/**
	 * 构造函数
	 * 
	 * @param dbConfig 数据库配置信息
	 * @param txManager 事务管理器
	 */
	public Db(DbConfig dbConfig, TxManager txManager) {
		this.txManager = txManager;
		this.dbConfig = dbConfig;
		this.name = dbConfig.getName();
	}
	
	/**
	 * @return the dbConfig
	 */
	public DbConfig getDbConfig() {
		return dbConfig;
	}
	
	/**
	 * 为当前数据库开启一个事务
	 * 
	 * @return 事务实例
	 */
	public Transaction beginTransaction() {
		TxStrategy strategy = TxStrategyThreadLocal.get();
		if(strategy == null) {
			strategy = TxStrategy.DEFAULT_TX_STRATEGY;
		}
		
		TxType txType = strategy.getType();
		ScopeTransaction curTx = ScopeTransactionThreadLocal.get(this.name);
		Transaction scopeTx = createScopeTransaction(curTx, strategy);
		
		return scopeTx;
	}
	
	/**
	 * 获取当前事务
	 * 
	 * @return
	 */
	public Transaction getCurrentTransaction() {
//		return TransactionThreadLocal.get(this.name);
		return ScopeTransactionThreadLocal.get(this.name);
	}
	
	/**
	 * 提交当前事务
	 */
	public void commitTransaction() {
//		TransactionThreadLocal.get(this.name).commit();
		ScopeTransactionThreadLocal.get(this.name).commit();
	}
	
	/**
	 * 回滚当前事务
	 */
	public void rollbackTransaction() {
		ScopeTransactionThreadLocal.get(this.name).rollback();
	}
	
	/**
	 * 结束一个事务
	 */
	public void endTransaction() {
//		TransactionThreadLocal.get(this.name).end();
		ScopeTransactionThreadLocal.get(this.name).end();
	}
	
	/**
	 * 根据主键查询一个实体对象。
	 * 
	 * @param clazz 实体类
	 * @param id 主键
	 * @return 实体对象
	 */
	public <T> T find(Class<T> clazz, Object id) {
		Transaction transaction = getTransaction();
		
		JdbcEntityDescription entityDesc = JdbcEntityDescriptionFactory.getInstance().getEntityDescription(clazz);
		String idDataItem = entityDesc.getIdDescription().getDataItem();
		
		String sql = "select " + entityDesc.allDataItemString() + " from " + entityDesc.getDataSet() + " where " + idDataItem + " = :id";
		Query<T> sqlQuery = new SqlQueryAction<T>(sql, transaction);
		sqlQuery.setParameter("id", id);
		
		return sqlQuery.uniqueTo(clazz);
	}
	
	/**
	 * 构建一个sql查询
	 * 
	 * @param sql
	 * @return
	 */
	public Query query(String sql) {
		Transaction transaction = getTransaction();
		Query sqlQuery = new SqlQueryAction(sql, transaction);
		return sqlQuery;
	}
	
	/**
	 * 构建一个sql查询
	 * 
	 * @param clazz 实体类
	 * @return
	 */
	public Query query(Class clazz) {
		Transaction transaction = getTransaction();
		Query sqlQuery = new SqlQueryAction(clazz, transaction);
		return sqlQuery;
	}
	
	/**
	 * 分区查询
	 * 
	 * @param clazz
	 * @param partitionValue
	 * @return
	 */
	public Query queryByPartition(Class clazz, Object partitionValue) {
	    Transaction transaction = getTransaction();
        Query sqlQuery = new SqlQueryAction(clazz, transaction);
        sqlQuery.setPartitionQuery(true);
        sqlQuery.setPartitionValue(partitionValue);
        return sqlQuery;
	}
	
	/**
	 * 构建一个预定义名称的sql查询
	 * 
	 * @param entityClass 实体类
	 * @param sqlName SQL名称
	 * @return SQLQuery对象
	 */
	public <T> Query<T> query(Class<T> entityClass, String sqlName) {
		Transaction transaction = getTransaction();
		Query<T> sqlQuery = new SqlQueryAction<T>(entityClass, sqlName, transaction);
		return sqlQuery;
	}
	
	/**
	 * 
	 * @param sql
	 * @return
	 */
	public Command sql(String sql) {
		Transaction transaction = getTransaction();
		Command command = new SqlCommand(sql, transaction);
		return command;
	}
	
	/**
	 * 保存一个实体
	 * 
	 * @param entity
	 * @return
	 */
	public Object save(Object entity) {
		Transaction transaction = getTransaction();
		SaveAction saveAction = new SaveAction(entity, transaction);
		return saveAction.execute();
	}
	
	/**
	 * 删除一个实体
	 * 
	 * @param entity
	 * @return
	 */
	public int delete(Object entity) {
		Transaction transaction = getTransaction();
		DeleteAction deleteAction = new DeleteAction(entity, transaction);
		return deleteAction.execute();
	}
	
	/**
	 * 根据ID删除一个实体
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public int delete(Class entityClass, Object id) {
		Transaction transaction = getTransaction();
		DeleteAction deleteAction = new DeleteAction(entityClass, id, transaction);
		return deleteAction.execute();
	}
	
	/**
	 * 更新一个实体
	 * 
	 * @param entity
	 * @return
	 */
	public int update(Object entity) {
		Transaction transaction = getTransaction();
		UpdateAction updateAction = new UpdateAction(entity, transaction, false);
		return updateAction.execute();
	}
	
	/**
     * 更新一个实体, 可决定是否忽略实体中的空值属性
     * 
     * @param entity
     * @param ignoreNullValue 默认为false
     * @return
     */
    public int update(Object entity, boolean ignoreNullValue) {
        Transaction transaction = getTransaction();
        UpdateAction updateAction = new UpdateAction(entity, transaction, ignoreNullValue);
        return updateAction.execute();
    }
	
	/**
	 * 准备更新某一个实体
	 * 
	 * @param entityClass
	 * @param id
	 * @return
	 */
	public UpdateAction update(Class entityClass, Object id) {
		Transaction transaction = getTransaction();
		UpdateAction updateAction = new UpdateAction(entityClass, id, transaction);
		return updateAction;
	}
	
	public void tx(TxRunnable runnable) {
		tx(TxStrategy.DEFAULT_TX_STRATEGY, runnable);
	}
	
	/**
	 * 执行一段事务代码，没有返回值
	 * 
	 * @param runnable
	 */
	public void tx(TxStrategy strategy, TxRunnable runnable) {
		logger.debug("Init transaction strategy before method.");
		
		//对已有ScopeTransaction增加引用计数
		ScopeTransactionManager.increaseRefCount();
		
		if(strategy == null) {
			strategy = TxStrategy.DEFAULT_TX_STRATEGY;
		}
		TxStrategyThreadLocal.set(strategy);
		
		try {
			runnable.run();
			ScopeTransactionManager.success();
		}catch(Throwable e) {
			logger.debug("Handle transaction after throwing.");
			ScopeTransactionManager.error(e);
		}finally{
			logger.debug("Handle transaction after method.");
			ScopeTransactionManager.end();
    		ScopeTransactionManager.reduceRefCount();
		}
	}
	
	public Object tx(TxCallable callable) {
		return tx(TxStrategy.DEFAULT_TX_STRATEGY, callable);
	}
	
	/**
	 * 执行一段事务代码，没有返回值
	 * 
	 * @param runnable
	 */
	public Object tx(TxStrategy strategy, TxCallable callable) {
		logger.debug("Init transaction strategy before method.");
		
		//对已有ScopeTransaction增加引用计数
		ScopeTransactionManager.increaseRefCount();
		
		if(strategy == null) {
			strategy = TxStrategy.DEFAULT_TX_STRATEGY;
		}
		TxStrategyThreadLocal.set(strategy);
		
		try {
			Object r = callable.call();
			ScopeTransactionManager.success();
			return r;
		}catch(Throwable e) {
			logger.debug("Handle transaction after throwing.");
			ScopeTransactionManager.error(e);
			return null;
		}finally{
			logger.debug("Handle transaction after method.");
			ScopeTransactionManager.end();
    		ScopeTransactionManager.reduceRefCount();
		}
	}
	
	private Transaction getTransaction() {
		Transaction transaction = getCurrentTransaction();
		if(transaction == null || !transaction.isActive()) {
			transaction = beginTransaction();
		}
		return transaction;
	}
	
	/**
	 * 是否需要创建新的Transaction.
	 * 
	 * @param t
	 * @param strategy
	 * @return
	 */
	private boolean needCreateNewTransaction(Transaction t, TxStrategy strategy) {

		TxType type = strategy.getType();
		switch (type) {
		case REQUIRED:
			return t == null;

		case REQUIRES_NEW:
			return true;

		case MANDATORY:
			if (t == null)
				throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_STRATEGY_ERR, "Transaction is missing or not active for MANDATORY strategy.");
			return true;

		case NEVER:
			if (t != null)
				throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_STRATEGY_ERR, "Transaction is exists on NEVER strategy.");
			return false;

		case SUPPORTS:
			return false;

		case NOT_SUPPORTED:
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_STRATEGY_ERR, "NOT_SUPPORTED should already be handled?");

		default:
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_STRATEGY_ERR, "Should never get here?");
		}
	}
	
	public ScopeTransaction createScopeTransaction(ScopeTransaction scopeTx, TxStrategy strategy) {

		if (strategy == null) {
			strategy = TxStrategy.DEFAULT_TX_STRATEGY;
		}
		
		boolean needNewTx = needCreateNewTransaction(scopeTx, strategy);
        ScopeTransaction newScopeTx = new ScopeTransaction();
        newScopeTx.setDbName(this.name);
        newScopeTx.setStrategy(strategy);
		if(needNewTx) {
			int isoLevel = 2;
			TxIsolation isolation = strategy.getIsolation();
			if (isolation != null) {
				isoLevel = isolation.getLevel();
			}
			Transaction newTx = txManager.createTransaction(strategy.isReadOnly(), strategy.getIsolation());
			
			newScopeTx.setNested(false);
			newScopeTx.setSuspendedTransaction(scopeTx);
			newScopeTx.setTransaction(newTx);
			newScopeTx.setNewtx(true);
        }else{
        	newScopeTx.setNested(true);
        	newScopeTx.setNewtx(false);
        	newScopeTx.setSuspendedTransaction(null);
			newScopeTx.setTransaction(scopeTx.getTransaction());
        }
		ScopeTransactionThreadLocal.set(this.name, newScopeTx);
		return newScopeTx;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
