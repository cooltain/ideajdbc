/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.dialect;

/**
 * @author Chinakite
 *
 */
public class DialectFactory {
	
	public static Dialect getDialect(String type) {
		if(RdbmsType.MYSQL.equals(type.toLowerCase())) {
			return new MySqlDialect();
		}
		return null;
	}
}
