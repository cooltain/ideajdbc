/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.server;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import com.ideamoment.ideajdbc.configuration.DataSourceConfig;
import com.ideamoment.ideajdbc.configuration.DbConfig;
import com.ideamoment.ideajdbc.configuration.IdeaJdbcConfiguration;
import com.ideamoment.ideajdbc.datasource.DataSourceManager;
import com.ideamoment.ideajdbc.exception.IdeaJdbcException;
import com.ideamoment.ideajdbc.exception.IdeaJdbcExceptionCode;
import com.ideamoment.ideajdbc.transaction.DefaultTxManager;
import com.ideamoment.ideajdbc.transaction.TxManager;

/**
 * @author Chinakite
 *
 */
public class DbManager {
	
	private final ConcurrentHashMap<String, Db> concMap = new ConcurrentHashMap<String, Db>();
	
	private final HashMap<String, Db> syncMap = new HashMap<String, Db>();
	
	private final Object monitor = new Object();
	
	private Db defaultDb;
	
	private static DbManager dbManager = new DbManager();
	
	private DbManager() {
		String dbName = getDefaultDbName();
		if(dbName != null) {
			defaultDb = createDb(dbName);
			synchronized (monitor) {
				concMap.put(dbName, defaultDb);
				syncMap.put(dbName, defaultDb);
			}
		}
	}
	
	/**
	 * 单例模式下获取实例。
	 * 
	 * @return DbManager实例
	 */
	public static DbManager getInstance() {
		return dbManager;
	}
	
	/**
	 * 根据数据库名称获取数据库实例
	 * 
	 * @param dbName 数据库名称
	 * @return 数据库实例
	 */
	public Db getDb(String dbName) {
		if(dbName == null){
			return defaultDb;
		}
		Db db = concMap.get(dbName);
		if(db != null){
			return db;
		}
		return getWithCreate(dbName);
	}
	
	/**
	 * 获取默认数据库实例
	 * 
	 * @return 数据库实例
	 */
	public Db getDefaultDb() {
		return getDb(null); 
	}
	
	/**
	 * 获取默认数据库名称
	 * 
	 * @return 默认数据库
	 */
	private String getDefaultDbName() {
		return IdeaJdbcConfiguration.get("datasource.default", null);
	}
	
	/**
	 * 根据名称创建Db实例，同时初始化连接池
	 * 
	 * @param dbName 数据库名称
	 * @return
	 */
	public Db createDb(String dbName) {
		DbConfig dbConfig = new DbConfig();
		dbConfig.setName(dbName);
		dbConfig.loadSettings();
		
		return createDb(dbConfig);
	}
	
	/**
	 * 根据名称创建Db实例，并指定事务管理器，同时初始化连接池
	 * 
	 * @param dbName 数据库名称
	 * @param txManager 事务管理器
	 * @return
	 */
	public Db createDb(String dbName, TxManager txManager) {
		DbConfig dbConfig = new DbConfig();
		dbConfig.setName(dbName);
		dbConfig.loadSettings();
		
		DataSource ds = getDataSourceFromConfig(dbConfig);
		dbConfig.setDataSource(ds);
		
		Db db = new Db(dbConfig, txManager);
		return db;
	}
	
	private Db createDb(DbConfig dbConfig) {
		DataSource ds = getDataSourceFromConfig(dbConfig);
		dbConfig.setDataSource(ds);
		
		String txManagerName = dbConfig.getTxManagerClazz().trim();
		
		if(DbConfig.DEFAULT_TXMANAGER.equals(txManagerName)) {
			TxManager txManager = new DefaultTxManager(dbConfig);
			Db db = new Db(dbConfig, txManager);
			return db;
		}else{
			try {
				Class txManagerClass = Class.forName(txManagerName);
				Constructor method = txManagerClass.getDeclaredConstructor(DbConfig.class);
				TxManager txManager = (TxManager)(method.newInstance(dbConfig));
				Db channel = new Db(dbConfig, txManager);
				return channel;
			} catch (InstantiationException e) {
				e.printStackTrace();
				throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_MGR_INIT_ERR, "Can not get transaction manager instance of " + txManagerName, e);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_MGR_INIT_ERR, "Can not get transaction manager instance of " + txManagerName, e);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_MGR_INIT_ERR, "ClassNotFoundException of " + txManagerName, e);
			} catch (SecurityException e) {
				e.printStackTrace();
				throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_MGR_INIT_ERR, "Can not get transaction manager instance of " + txManagerName, e);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_MGR_INIT_ERR, "Can not get transaction manager instance of " + txManagerName, e);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_MGR_INIT_ERR, "Can not get transaction manager instance of " + txManagerName, e);
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_MGR_INIT_ERR, "Can not get transaction manager instance of " + txManagerName, e);
			}
		}
	}
	
	private DataSource getDataSourceFromConfig(DbConfig config) {
		DataSourceConfig dsConfig = config.getDataSourceConfig();
		DataSourceManager manager = new DataSourceManager();
		DataSource ds = manager.getDataSource(config.getName(), dsConfig);
		return ds;
	}
	
	private Db getWithCreate(String dbName) {
		synchronized (monitor) {
			Db db = syncMap.get(dbName);
			if(db == null) {
				db = createDb(dbName);
				register(db, false);
			}
			return db;
		}
	}
	
	public void register(Db db, boolean isDefault) {
		synchronized (monitor) {
			concMap.put(db.getName(), db);
			syncMap.put(db.getName(), db);
			if(isDefault) {
				defaultDb = db;
			}
		}
	}
}
