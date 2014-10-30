/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.configuration;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Chinakite
 *
 */
public class TestIdeaJdbcConfiguration {
	@Test
	public void testGet() {
		IdeaJdbcConfiguration.initConfig("ideajdbc.properties");
		String p1 = IdeaJdbcConfiguration.get("datasource.default", "none");
		assertEquals("mysql", p1);
		
		String p2 = IdeaJdbcConfiguration.get("datasource.mysql.url", "none");
		assertEquals("jdbc:mysql://localhost/ideajdbc", p2);
		
		boolean p3 = IdeaJdbcConfiguration.getBoolean("ideajdbc.log", true);
		assertEquals(false, p3);
	}
}
