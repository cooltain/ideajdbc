/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.exception;

/**
 * @author Chinakite
 *
 */
public final class IdeaJdbcExceptionCode {
	public static final String TX_COMMIT_ERR = "JDBC-10001";   //事务提交错误
	
	public static final String TX_CLOSE_ERR = "JDBC-10002";   //事务关闭错误
	
	public static final String TX_ROLLBACK_ERR = "JDBC-10003";   //事务回滚错误
	
	public static final String TX_SET_IN_THREAD_ERR = "JDBC-10004";   //事务回滚错误
	
	public static final String TX_MGR_INIT_ERR = "JDBC-10005";	//事务管理类初始化错误
	
	public static final String TX_NOTACTIVE_ERR = "JDBC-10006";   //事务没有开启或已关闭
	
	public static final String NAMEDSQL_NOTFOUND = "JDBC-10007";    //命名sql没有找到
	
	public static final String SQL_PARAMETER_PARSER_ERR = "JDBC-10008";    //SQL参数解析错误
	
	public static final String SQL_EXECUTE_ERR = "JDBC-10009";    //SQL执行异常
	
	public static final String RESULT_HANDLE_ERR = "JDBC-10010";    //结果集处理异常
	
	public static final String QUERY_ERR = "JDBC-10011";    //查询方法异常
	
	public static final String GENERATOR_ERR = "JDBC-10012";    //主键生成器异常
	
	public static final String TX_STRATEGY_ERR = "JDBC-10013";    //事务策略异常
	
	public static final String PARTITION_MAPPING_ERR = "JDBC-10014";     //表分区字段映射错误
}
