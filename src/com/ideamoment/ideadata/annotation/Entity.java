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
 * 实体。
 * 
 * @author Chinakite Zhang
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)   
@Documented
public @interface Entity {
	/**
	 * 数据集名称
	 * 
	 * @return
	 */
	public String dataSet() default "";
}
