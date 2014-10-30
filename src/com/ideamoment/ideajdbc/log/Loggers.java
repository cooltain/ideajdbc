package com.ideamoment.ideajdbc.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志记录器常量类
 *
 * @author Chinakite Zhang
 */
public class Loggers {
	/**
	 * 慢查询记录器
	 */
    public static final Logger slowQueryLogger = LoggerFactory.getLogger("ideajdbc.SlowQueryLogger");
    
    /**
     * 连接信息记录器
     */
    public static final Logger connectionLogger = LoggerFactory.getLogger("ideajdbc.ConnectionLogger");
    
    /**
     * 结果集记录器
     */
    public static final Logger resultSetLogger = LoggerFactory.getLogger("ideajdbc.ResultSetLogger");
    
    /**
     * 查询语句记录器
     */
    public static final Logger statementLogger = LoggerFactory.getLogger("ideajdbc.StatementLogger");
}
