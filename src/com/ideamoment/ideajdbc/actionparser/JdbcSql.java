/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.actionparser;

import java.util.List;

/**
 * SQL语句的封装类。
 * 
 * @author Chinakite Zhang
 * @version 2010-10-18
 * @since 0.1
 */
public class JdbcSql {
	
	private String sql;		//SQL语句
	private List<JdbcSqlParam> params;    //参数列表

	/**
	 * 无参构造函数
	 */
	public JdbcSql(){
		this.sql = null;
	}
	
	/**
	 * 构造函数
	 * 
	 * @param sql SQL语句
	 */
	public JdbcSql(String sql) {
		this.sql = sql;
	}
	
	/**
	 * 构造函数
	 * 
	 * @param sql SQL语句
	 * @param params 参数列表
	 */
	public JdbcSql(String sql, List<JdbcSqlParam> params) {
		this.sql = sql;
		this.params = params;
	}
	
	//-----------------------------------------------------------------
	// Getter & Setter
	//-----------------------------------------------------------------
	
	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<JdbcSqlParam> getParams() {
		return params;
	}

	public void setParams(List<JdbcSqlParam> params) {
		this.params = params;
	}

//	public JdbcSqlParam[] getParams() {
//		return params;
//	}
//
//	public void setParams(JdbcSqlParam[] params) {
//		this.params = params;
//	}
	
}
