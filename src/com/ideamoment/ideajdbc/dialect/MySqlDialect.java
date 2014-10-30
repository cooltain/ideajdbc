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
public class MySqlDialect implements Dialect {

	/* (non-Javadoc)
	 * @see com.ideamoment.ideajdbc.dialect.Dialect#pageSql(java.lang.String)
	 */
	@Override
	public String pageSql(String sql) {
		return sql + " LIMIT ?, ?";
	}

}
