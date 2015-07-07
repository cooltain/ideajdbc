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
 * 表分区信息。
 * 
 * @author Chinakite Zhang
 * @version 2015-07-06
 * @since 0.1
 */
@Target(ElementType.TYPE)   
@Retention(RetentionPolicy.RUNTIME)   
@Documented
public @interface Partition {
    /**
     * 分区属性，优先级低于dataItem
     * 
     * @return
     */
    String property() default "";
    
    /**
     * 分区字段名，优先级高于property
     * 
     * @return
     */
    String dataItem() default "";
}
