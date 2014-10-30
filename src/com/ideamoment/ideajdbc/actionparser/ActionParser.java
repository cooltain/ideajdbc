/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.actionparser;

import com.ideamoment.ideajdbc.action.Action;

/**
 * Action解析器。
 * 
 * @author Chinakite Zhang
 * @version 2010-10-18
 * @since 0.1
 */
public interface ActionParser {
	/**
	 * 将Action解析为相应的Sql。
	 * 
	 * @param action 增、删、改、查操作
	 * @return JdbcSql Sql语句及一些相关信息
	 */
	public JdbcSql parse(Action action);
}
