/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.util;

/**
 * @author Chinakite
 *
 */
public class DateUtil {
	
	public static java.sql.Date toSqlDate(java.util.Date date) {
		return new java.sql.Date(date.getTime());
	}
	
	public static java.sql.Time toSqlTime(java.util.Date date) {
		return new java.sql.Time(date.getTime());
	}
	
	public static java.sql.Timestamp toSqlTimestamp(java.util.Date date) {
		return new java.sql.Timestamp(date.getTime());
	}
	
	public static java.util.Date toJavaDate(java.sql.Date date) {
		return new java.util.Date(date.getTime());
	}
	
	public static java.util.Date toJavaDate(java.sql.Time time) {
		return new java.util.Date(time.getTime());
	}
	
	public static java.util.Date toJavaDate(java.sql.Timestamp timestamp) {
		return new java.util.Date(timestamp.getTime());
	}
}
