/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.actionparser;

import com.ideamoment.ideadata.annotation.DataItemType;

/**
 * 
 * 
 * @author Chinakite Zhang
 * @version 2012-8-16
 * @since 0.1
 */
public class JdbcSqlParam {
	
	private Object paramValue;
	private DataItemType type;
	
	public JdbcSqlParam(){
	}
	
	public JdbcSqlParam(Object paramValue, DataItemType type){
		this.paramValue = paramValue;
		this.type = type;
	}
	
	public Object getParamValue() {
		return paramValue;
	}
	public void setParamValue(Object paramValue) {
		this.paramValue = paramValue;
	}
	
	public DataItemType getType() {
		if(type == null) {
			return null;
		}
		return type;
	}
	public void setType(DataItemType type) {
		this.type = type;
	}
}
