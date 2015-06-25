/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc;

import com.ideamoment.ideadata.description.EntityDescriptionFactory;
import com.ideamoment.ideadata.description.PropertyDescriptionDecoration;
import com.ideamoment.ideajdbc.action.Command;
import com.ideamoment.ideajdbc.action.Query;
import com.ideamoment.ideajdbc.action.UpdateAction;
import com.ideamoment.ideajdbc.configuration.DbConfig;
import com.ideamoment.ideajdbc.configuration.IdeaJdbcConfiguration;
import com.ideamoment.ideajdbc.description.DataItemUpperDecoration;
import com.ideamoment.ideajdbc.log.DriverLoggingProxy;
import com.ideamoment.ideajdbc.server.Db;
import com.ideamoment.ideajdbc.server.DbManager;
import com.ideamoment.ideajdbc.transaction.Transaction;
import com.ideamoment.ideajdbc.transaction.TxCallable;
import com.ideamoment.ideajdbc.transaction.TxRunnable;
import com.ideamoment.ideajdbc.transaction.TxStrategy;

/**
 * @author Chinakite
 *
 */
public class IdeaJdbc {
	
	static {
		PropertyDescriptionDecoration dataItemUpper = new DataItemUpperDecoration();
		EntityDescriptionFactory.getInstance().setupPropertyDecoration(dataItemUpper);
		if(!IdeaJdbcConfiguration.inited) {
			IdeaJdbcConfiguration.initConfig("ideajdbc.properties");
		}
		if(IdeaJdbcConfiguration.getBoolean("ideajdbc.log", false)) {
			DriverLoggingProxy proxy = new DriverLoggingProxy();
		}
	}
	
	/**
	 * 根据主键查找一个对象。
	 * <ul>
	 *     <li>如果使用了HandlerSocket或Memcached API，则直接使用id做为key去查询</li>
	 *     <li>如果没有使用key-value方案，则生成主键查询语句去查询。</li>
	 * </ul>
	 * 
	 * @param clazz 实体类
	 * @param id 主键
	 * @return 如果存在返回对象，如果不存在返回null。
	 */
	public static <T> T find(Class<T> clazz, Object id) {
		return defaultDb().find(clazz, id);
	}
	
	
	/**
	 * 在默认数据库上构建一个SQL查询
	 * 
	 * @param sql SQL语句
	 * @return SQL查询
	 */
	public static Query query(String sql) {
		return defaultDb().query(sql);
	}
	
	/**
	 * 在默认数据库上构建一个预定义名称的sql查询
	 * 
	 * @param entityClass 实体类
	 * @return 查询对象
	 */
	public static <T> Query<T> query(Class<T> entityClass) {
		return defaultDb().query(entityClass);
	}
	
	/**
	 * 在默认数据库上构建一个预定义名称的sql查询
	 * 
	 * @param entityClass 实体类
	 * @param sqlName SQL名称
	 * @return 查询对象
	 */
	public static <T> Query<T> query(Class<T> entityClass, String sqlName) {
		return defaultDb().query(entityClass, sqlName);
	}
	
	/**
	 * 
	 * 
	 * @param sql
	 * @return
	 */
	public static Command sql(String sql){
		return defaultDb().sql(sql);
	}

	/**
	 * 保存一个实体对象
	 * 
	 * @param entity
	 * @return
	 */
	public static Object save(Object entity) {
		return defaultDb().save(entity);
	}
	
	/**
	 * 保存一个实体对象
	 * 
	 * @param entity
	 * @return
	 */
	public static Object delete(Object entity) {
		return defaultDb().delete(entity);
	}
	
	/**
	 * 保存一个实体对象
	 * 
	 * @param entity
	 * @return
	 */
	public static Object delete(Class entityClass, Object id) {
		return defaultDb().delete(entityClass, id);
	}
	
	/**
	 * 更新实体
	 * 
	 * @param entity
	 * @return
	 */
	public static int update(Object entity) {
		return defaultDb().update(entity);
	}
	
	/**
	 * 构建更新动作
	 * 
	 * @param entityClass  实体类
	 * @param id  主键
	 * @return
	 */
	public static UpdateAction update(Class entityClass, Object id) {
		return defaultDb().update(entityClass, id);
	}
	
	/**
	 * 根据数据库名称获取数据库实例
	 * 
	 * @param dbName 数据库名称
	 * @return 数据库实例
	 */
	public static Db db(String dbName) {
		return DbManager.getInstance().getDb(dbName);
	}
	
	/**
	 * 获取默认数据库实例
	 * 
	 * @return 默认数据库实例
	 */
	public static Db defaultDb() {
		return DbManager.getInstance().getDefaultDb();
	}
	
	/**
	 * 为默认数据库开启一个事务,放在当前线程中
	 * 
	 * @return 事务实例
	 */
	public static Transaction beginTransaction() {
		return defaultDb().beginTransaction();
	}
	
	/**
	 * 提交事务
	 */
	public static void commitTransaction() {
		defaultDb().commitTransaction();
	}
	
	/**
	 * 结束默认数据库当前线程的事务
	 */
	public static void endTransaction() {
		defaultDb().endTransaction();
	}
	
	/**
	 * 执行一段事务代码块
	 * 
	 * @param r
	 */
	public static void tx(TxRunnable r) {
		defaultDb().tx(TxStrategy.DEFAULT_TX_STRATEGY, r);
	}
	
	/**
	 * 执行一段事务代码块
	 * 
	 * @param r
	 */
	public static void tx(TxStrategy strategy, TxRunnable r) {
		defaultDb().tx(strategy, r);
	}
	
	/**
	 * 执行一段事务代码块
	 * 
	 * @param r
	 */
	public static Object tx(TxCallable c) {
		return defaultDb().tx(TxStrategy.DEFAULT_TX_STRATEGY, c);
	}
	
	/**
	 * 执行一段事务代码块
	 * 
	 * @param r
	 */
	public static Object tx(TxStrategy strategy, TxCallable c) {
		return defaultDb().tx(strategy, c);
	}
	
	/**
	 * 根据传入的DbConfig增加数据库.
	 * 使用方法如下:
	 * <pre>
	 * DbConfig dbConfig = new DbConfig();
	 * dbConfig.setName("newDb");
	 * dbConfig.setTxManagerClazz("com.ideamoment.ideajdbc.transaction.DefaultTxManager");
	 * 
	 * DataSourceConfig dsConfig = new DataSourceConfig();
	 * dsConfig.setUsername("newuser");
	 * dsConfig.setPassword("newpwd");
	 * dsConfig.setUrl("dbURL");
	 * dsConfig.setDriver("com.mysql.driver.Driver");
	 * dsConfig.setMinConnections(1);
	 * dsConfig.setMaxConnections(100);
	 * dsConfig.setMaxConnectionLifetime(1440000);
	 * dsConfig.setMaxActiveTime(300);
	 * dsConfig.setHeartbeatSql("select 1");
	 * 
	 * dbConfig.setDataSourceConfig(dsConfig);
	 * 
	 * IdeaJdbc.addDb(dbConfig);
	 * </pre>
	 * 
	 * @param dbConfig 数据库配置
	 */
	public static Db addDb(DbConfig dbConfig) {
	    return DbManager.getInstance().getWithCreate(dbConfig);
	}
}
