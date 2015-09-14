/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.datasource;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.ideamoment.ideajdbc.configuration.DataSourceConfig;
import com.ideamoment.ideajdbc.configuration.IdeaJdbcConfiguration;

/**
 * Rainbow默认实现的DataSource。
 * 
 * @author Chinakite Zhang
 * @version 2010/10/13
 * @since 0.1
 */
public class DefaultDataSource implements DataSource {

	/**
	 * JDBC连接URL.
	 */
	private final String url;

	/**
	 * JDBC驱动类名.
	 */
	private final String driver;
	
	private ConnectionPool connectionPool;
	
	/**
	 * 构造函数。根据数据源配置信息中的连接池类型调用相应的连接池实现，默认为Proxool。
	 * 
	 * @param name Channel名称
	 * @param config 数据源配置信息
	 */
	public DefaultDataSource(String name, DataSourceConfig config) {
		this.url = config.getUrl();
		this.driver = config.getDriver();
		
		String connectionPoolType = IdeaJdbcConfiguration.get("datasource." + name + ".pool", "default");
		if("default".equals(connectionPoolType.toLowerCase()) || "proxool".equals(connectionPoolType.toLowerCase())) {
			if(connectionPool == null) {
				connectionPool = new ProxoolConnectionPool(name, config);
			}
		}else if("c3p0".equals(connectionPoolType.toLowerCase())){
			
		}else if("dbcp".equals(connectionPoolType.toLowerCase())){
			
		}else if("druid".equals(connectionPoolType.toLowerCase())){
		    if(connectionPool == null) {
                connectionPool = new DruidConnectionPool(name, config);
            }
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.sql.DataSource#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return connectionPool.getConnection();
	}

	/* (non-Javadoc)
	 * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
	 */
	public Connection getConnection(String username, String password)
			throws SQLException {
		return connectionPool.getConnection(username, password);
	}

	/* (non-Javadoc)
	 * @see javax.sql.CommonDataSource#getLogWriter()
	 */
	public PrintWriter getLogWriter() throws SQLException {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.sql.CommonDataSource#getLoginTimeout()
	 */
	public int getLoginTimeout() throws SQLException {
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.sql.CommonDataSource#setLogWriter(java.io.PrintWriter)
	 */
	public void setLogWriter(PrintWriter out) throws SQLException {

	}

	/* (non-Javadoc)
	 * @see javax.sql.CommonDataSource#setLoginTimeout(int)
	 */
	public void setLoginTimeout(int seconds) throws SQLException {

	}

	/* (non-Javadoc)
	 * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
	 */
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	/* (non-Javadoc)
	 * @see java.sql.Wrapper#unwrap(java.lang.Class)
	 */
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return null;
	}

}
