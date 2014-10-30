/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.logicalcobwebs.proxool.ProxoolException;
import org.logicalcobwebs.proxool.ProxoolFacade;

import com.ideamoment.ideajdbc.configuration.DataSourceConfig;

/**
 * 基于Proxool的连接池实现。
 * 
 * @author Chinakite Zhang
 * @version 2010/10/13
 * @since 0.1
 */
public class ProxoolConnectionPool implements ConnectionPool {

	private String name;	//Channel名称
	private String proxoolUrl;		//proxool连接地址
	
	/**
	 * 构造函数。在构造函数中根据DataSourceConfig中的信息设置Proxool连接池的对应配置。
	 * 
	 * @param channelName Channel名称
	 * @param config 数据源配置信息
	 */
	public ProxoolConnectionPool(String channelName, DataSourceConfig config) {
		name = channelName;
		try {
			Class.forName(config.getDriver());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Properties info = new Properties();
		info.setProperty("proxool.maximum-connection-count", String.valueOf(config.getMaxConnections()));
		info.setProperty("proxool.minimum-connection-count", String.valueOf(config.getMinConnections()));
		info.setProperty("proxool.house-keeping-test-sql", config.getHeartbeatSql());
		info.setProperty("proxool.simultaneous-build-throttle", String.valueOf(config.getMaxConnections()));
		info.setProperty("proxool.maximum-active-time", String.valueOf(config.getMaxActiveTime()));
		info.setProperty("proxool.maximum-connection-lifetime", String.valueOf(config.getMaxConnectionLifetime()));
		info.setProperty("user", config.getUsername());
		info.setProperty("password", config.getPassword());
		String alias = name;
		proxoolUrl = "proxool." + alias + ":" + config.getDriver() + ":" + config.getUrl();
		try {
			ProxoolFacade.registerConnectionPool(proxoolUrl, info);
		} catch (ProxoolException e) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see com.ideamoment.rainbow.datasource.ConnectionPool#getConnection()
	 */
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection("proxool." + name);
	}

	/* (non-Javadoc)
	 * @see com.ideamoment.rainbow.datasource.ConnectionPool#getConnection()
	 */
	public Connection getConnection(String username, String password)
			throws SQLException {
		return DriverManager.getConnection(proxoolUrl, username, password);
	}

}
