/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.action;

/**
 * ORM中发出的增、删、改、查的各种操作。
 * 
 * @author Chinakite Zhang
 * @version 2010-10-18
 * @since 0.1
 */
public interface Action<T> {
	/**
	 * 获取当前操作对应的实体类。
	 * 
	 * @return Class 实体类
	 */
	public Class<T> getEntityClass();
}
