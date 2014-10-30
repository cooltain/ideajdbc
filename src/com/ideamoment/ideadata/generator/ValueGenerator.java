/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideadata.generator;

import java.io.Serializable;

import com.ideamoment.ideajdbc.exception.IdeaJdbcException;

/**
 * 自动生成值的接口类。
 * 
 * @author Chinakite Zhang
 * @version 2011-2-2
 * @since 0.1
 */
public interface ValueGenerator {
	/**
	 * 自动生成字段的值。接口中提供了实体对象做为参数，这样可以根据传入的实体对象来生成值。
	 * 
	 * @param entity 实体对象
	 * @return 生成的值
	 */
	public Object generate(Object entity) throws IdeaJdbcException;
}
