/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideadata.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Chinakite
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)   
@Documented
public @interface Id{
	/**
	 * 数据项名称
	 * 
	 * @return
	 */
	public String dataItem() default "";
	
	/**
	 * 数据项类型
	 * 
	 * @return
	 */
	public DataItemType type() default DataItemType.UNKNOWN;
	
	/**
	 * 长度
	 * 
	 * @return
	 */
	public int length() default -1;
	
	/**
	 * 主键生成器
	 * 
	 * @return
	 */
	public String generator() default "assigned";
}
