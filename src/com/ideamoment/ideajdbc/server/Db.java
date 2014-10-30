/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.server;

import com.ideamoment.ideadata.description.EntityDescription;
import com.ideamoment.ideadata.description.EntityDescriptionFactory;
import com.ideamoment.ideajdbc.action.Command;
import com.ideamoment.ideajdbc.action.DeleteAction;
import com.ideamoment.ideajdbc.action.Query;
import com.ideamoment.ideajdbc.action.SaveAction;
import com.ideamoment.ideajdbc.action.SqlCommand;
import com.ideamoment.ideajdbc.action.SqlQueryAction;
import com.ideamoment.ideajdbc.action.UpdateAction;
import com.ideamoment.ideajdbc.configuration.DbConfig;
import com.ideamoment.ideajdbc.transaction.Transaction;
import com.ideamoment.ideajdbc.transaction.TransactionThreadLocal;
import com.ideamoment.ideajdbc.transaction.TxManager;

/**
 * 每个Db对应了一个数据源。这里将提供数据操作的所有接口。
 * IdeaJdbc里方便使用的静态方法都是Channel的包装。
 * 
 * @author Chinakite
 *
 */
public class Db {
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
		Transaction tx = this.txManager.createTransaction();
		TransactionThreadLocal.set(this.name, tx);
		return tx;
	}
	
	/**
	 * 获取当前事务
	 * 
	 * @return
	 */
	public Transaction getCurrentTransaction() {
		return TransactionThreadLocal.get(this.name);
	}
	
	/**
	 * 提交当前事务
	 */
	public void commitTransaction() {
		TransactionThreadLocal.get(this.name).commit();
	}
	
	/**
	 * 结束一个事务
	 */
	public void endTransaction() {
		TransactionThreadLocal.get(this.name).end();
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
		
		EntityDescription entityDesc = EntityDescriptionFactory.getInstance().getEntityDescription(clazz);
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
	
	public Object save(Object entity) {
		Transaction transaction = getTransaction();
		SaveAction saveAction = new SaveAction(entity, transaction);
		return saveAction.execute();
	}
	
	public int delete(Object entity) {
		Transaction transaction = getTransaction();
		DeleteAction deleteAction = new DeleteAction(entity, transaction);
		return deleteAction.execute();
	}
	
	public int delete(Class entityClass, Object id) {
		Transaction transaction = getTransaction();
		DeleteAction deleteAction = new DeleteAction(entityClass, id, transaction);
		return deleteAction.execute();
	}
	
	public int update(Object entity) {
		Transaction transaction = getTransaction();
		UpdateAction updateAction = new UpdateAction(entity, transaction);
		return updateAction.execute();
	}
	
	public UpdateAction update(Class entityClass, Object id) {
		Transaction transaction = getTransaction();
		UpdateAction updateAction = new UpdateAction(entityClass, id, transaction);
		return updateAction;
	}
	
	private Transaction getTransaction() {
		Transaction transaction = getCurrentTransaction();
		if(transaction == null || !transaction.isActive()) {
			transaction = beginTransaction();
		}
		return transaction;
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
