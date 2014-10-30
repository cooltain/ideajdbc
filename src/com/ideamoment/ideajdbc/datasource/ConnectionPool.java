/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.datasource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Rainbow连接池接口。
 * 
 * @author Chinakite Zhang
 * @version 2010/10/13
 * @since 0.1
 */
public interface ConnectionPool {
	/**
	 * 从连接池中获取Connection
	 * 
	 * @return Connection 数据库连接
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException;
	
	/**
	 * 使用用户名和密码从连接池中获取连接
	 * 
	 * @param username 用户名
	 * @param password 密码
	 * @return Connection 数据库连接
	 * @throws SQLException
	 */
	public Connection getConnection(String username, String password) throws SQLException;
}
