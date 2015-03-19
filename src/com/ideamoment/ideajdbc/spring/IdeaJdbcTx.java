/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.spring;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ideamoment.ideajdbc.transaction.TxIsolation;
import com.ideamoment.ideajdbc.transaction.TxType;

/**
 * IdeaJdbc与Spring集成时使用的事务注解
 * 
 * @author Chinakite
 *
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface IdeaJdbcTx {
	/**
	 * 事务控制类型
	 * @return
	 */
	public TxType type() default TxType.REQUIRED;
	
	/**
	 * 事务隔离级别
	 * 
	 * @return
	 */
	public TxIsolation isolation() default TxIsolation.READ_COMMITED;
	
	/**
	 * 要回滚的异常列表
	 * 
	 * @return
	 */
	public Class<? extends Throwable>[] rollbackFor() default Throwable.class;
	
	/**
	 * 不要回滚的异常列表
	 * 
	 * @return
	 */
	public Class<? extends Throwable>[] noRollbackFor() default Throwable.class;
	
	/**
	 * 是否只读
	 * @return
	 */
	public boolean readOnly() default false;
}
