/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.configuration;

import com.ideamoment.ideadata.configuration.IdeaDataConfiguration;

/**
 * 数据源配置信息.
 * 
 * @author Chinakite Zhang
 * @version 2010/10/11
 * @since 0.1
 */
public class DataSourceConfig {
	String url;		
	
	String username;		
	
	String password;
	
	String driver;
	
	int minConnections = 2;
	
	int maxConnections = 15;
	
	String heartbeatSql;
	
	int maxConnectionLifetime = 1440000;
	
	int maxActiveTime = 300;

	/**
	 * Return the connection URL.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Set the connection URL.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Return the database username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Set the database username.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Return the database password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Set the database password.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Return the database driver.
	 */
	public String getDriver() {
		return driver;
	}

	/**
	 * Set the database driver.
	 */
	public void setDriver(String driver) {
		this.driver = driver;
	}

	/**
	 * Return the minimum number of connections the pool should maintain.
	 */
	public int getMinConnections() {
		return minConnections;
	}

	/**
	 * Set the minimum number of connections the pool should maintain.
	 */
	public void setMinConnections(int minConnections) {
		this.minConnections = minConnections;
	}

	/**
	 * Return the maximum number of connections the pool can reach.
	 */
	public int getMaxConnections() {
		return maxConnections;
	}

	/**
	 * Set the maximum number of connections the pool can reach.
	 */
	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	/**
	 * Return a SQL statement used to test the database is accessible.
	 * <p>
	 * Note that if this is not set then it can get defaulted from the DatabasePlatform.
	 * </p>
	 */
	public String getHeartbeatSql() {
		return heartbeatSql;
	}

	/**
	 * Set a SQL statement used to test the database is accessible.
	 * <p>
	 * Note that if this is not set then it can get defaulted from the DatabasePlatform.
	 * </p>
	 */
	public void setHeartbeatSql(String heartbeatSql) {
		this.heartbeatSql = heartbeatSql;
	}

	/**
	 * Return the time in minutes after which a connection could
	 * be considered to have leaked.
	 */
	public int getMaxConnectionLifetime() {
		return maxConnectionLifetime;
	}

	/**
	 * Set the time in minutes after which a connection could
	 * be considered to have leaked.
	 */
	public void setMaxConnectionLifetime(int maxConnectionLifetime) {
		this.maxConnectionLifetime = maxConnectionLifetime;
	}

		/**
	 * Return the time in seconds a connection can be idle after
	 * which it can be trimmed from the pool.
	 * <p>
	 * This is so that the pool after a busy period can trend over time 
	 * back towards the minimum connections.
	 * </p>
	 */
	public int getMaxActiveTime() {
		return maxActiveTime;
	}

	/**
	 * Set the time in seconds a connection can be idle after
	 * which it can be trimmed from the pool.
	 * <p>
	 * This is so that the pool after a busy period can trend over time 
	 * back towards the minimum connections.
	 * </p>
	 */
	public void setMaxActiveTime(int maxActiveTime) {
		this.maxActiveTime = maxActiveTime;
	}

	/**
	 * Load the settings from ebean.properties.
	 */
	public void loadSettings(String dbName){
		
		String prefix = "datasource."+dbName+".";
		
		username = IdeaDataConfiguration.get(prefix+"username", null);
		password = IdeaDataConfiguration.get(prefix+"password", null);
		
		String v;
		
		v = IdeaDataConfiguration.get(prefix+"databaseDriver", null);
		driver = IdeaDataConfiguration.get(prefix+"driver", v);
		
		
		v = IdeaDataConfiguration.get(prefix+"databaseUrl", null);
		url = IdeaDataConfiguration.get(prefix+"url", v);
		
		//处理日志
		if(IdeaDataConfiguration.getBoolean("ideajdbc.log", false)) {
			String[] tmp = url.split(":");
			url = tmp[0] + ":ideajdbc:" + tmp[1] + ":" + tmp[2];
			if(url.indexOf('?') > 0) {
				url = url + "&";
			}else{
				url = url + "?";
			}
			url = url + "targetDriver=com.ideamoment.ideajdbc.datasource.DefaultDataSource";
		}
		//处理日志结束
		
		maxConnectionLifetime = IdeaDataConfiguration.getInt(prefix+"maxConnectionLifetime", 1440000);
		maxActiveTime = IdeaDataConfiguration.getInt(prefix+"maxActiveTime", 300);

		minConnections = IdeaDataConfiguration.getInt(prefix+"minConnections", 2);
		maxConnections = IdeaDataConfiguration.getInt(prefix+"maxConnections", 15);
		
		
		heartbeatSql = IdeaDataConfiguration.get(prefix+"heartbeatSql", null);
	}
}
