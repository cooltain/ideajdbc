/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.action;

import com.ideamoment.ideadata.annotation.DataItemType;

/**
 * @author Chinakite
 * @version 2012-8-22
 * @since 0.1
 */
public class Parameter {
	
	private Object value;
	private DataItemType type;
	private Object key;
	
	public Parameter(Object key, Object value){
		this.key = key;
		this.value = value;
		this.type = null;
	}
	
	public Parameter(Object key, Object value, DataItemType type){
		this.key = key;
		this.value = value;
		this.type = type;
	}
	
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public DataItemType getType() {
		return type;
	}
	public void setType(DataItemType type) {
		this.type = type;
	}
	public Object getKey() {
		return key;
	}
	public void setKey(Object key) {
		this.key = key;
	}
}
