/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.log;

import com.ideamoment.ideajdbc.configuration.IdeaJdbcConfiguration;

/**
 * @author Chinakite
 *
 */
public class IdeaJdbcLogConfiguration {
	public static long slowQueryThreshold = Long.MAX_VALUE;
	public static Boolean showTime = false;
	public static boolean printStackTrace = false;
	public static boolean printFullStackTrace = false;
	public static String printStackTracePattern = null;
	public static boolean inlineQueryParams = true;
	public static RdbmsSpecifics rdbmsSpecifics = new MySqlRdbmsSpecifics(); // oracle is default db.
	public static boolean logBeforeStatement = false;
	public static boolean logDetailAfterStatement = true;
	public static boolean logAddBatchDetail = true;
	public static boolean logExecuteBatchDetail =true;

    static {
    	if(!IdeaJdbcConfiguration.inited) {
    		IdeaJdbcConfiguration.initConfig("ideajdbc.properties");
    	}
    	slowQueryThreshold = IdeaJdbcConfiguration.getLong("ideajdbc.log.showQueryThreshold", Long.MAX_VALUE);
    	showTime = IdeaJdbcConfiguration.getBoolean("ideajdbc.log.showTime", false);
    	printStackTrace = IdeaJdbcConfiguration.getBoolean("ideajdbc.log.printStackTrace", false);
    	printFullStackTrace = IdeaJdbcConfiguration.getBoolean("ideajdbc.log.printFullStackTrace", false);
    	printStackTracePattern = IdeaJdbcConfiguration.get("ideajdbc.log.printStackTracePattern", null);
    	inlineQueryParams = IdeaJdbcConfiguration.getBoolean("ideajdbc.log.inlineQueryParams", true);
    	logBeforeStatement = IdeaJdbcConfiguration.getBoolean("ideajdbc.log.logBeforeStatement", false);
    	logDetailAfterStatement = IdeaJdbcConfiguration.getBoolean("ideajdbc.log.logDetailAfterStatement", true);
    	logAddBatchDetail = IdeaJdbcConfiguration.getBoolean("ideajdbc.log.logAddBatchDetail", true);
    	logExecuteBatchDetail = IdeaJdbcConfiguration.getBoolean("ideajdbc.log.logExecuteBatchDetail", true);
    }
    
}
