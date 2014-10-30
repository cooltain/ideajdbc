/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sql对实体进行操作。
 * 
 * @author Chinakite Zhang
 * @version 2010-11-8
 * @since 0.1
 */
@Target(ElementType.TYPE)   
@Retention(RetentionPolicy.RUNTIME)   
@Documented
public @interface Sql {
	/**
	 * 所有预配置的查询。
	 * 
	 * @return SqlQuery数组
	 */
	SqlQuery[] queries();
	
	/**
	 * 所有预配置的SQL DML语句
	 * 
	 * @return
	 */
	SqlUpdate[] updates();
}

