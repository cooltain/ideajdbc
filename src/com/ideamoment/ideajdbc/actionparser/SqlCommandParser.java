/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.actionparser;

import com.ideamoment.ideajdbc.action.SqlCommand;

/**
 * @author Chinakite
 *
 */
public class SqlCommandParser extends AbstractActionParser {

	/* (non-Javadoc)
	 * @see com.ideamoment.ideajdbc.actionparser.ActionParser#parse(com.ideamoment.ideajdbc.action.Action)
	 */
	public JdbcSql parse(SqlCommand command) {
		String sql = command.getSql();
		return buildJdbcSql(sql, command.getParameters());
	}

}
