/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.log;

import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Test;

/**
 * @author Chinakite
 *
 */
public class TestLog {
	@Test
	public void testPreparedStatementLoggingProxy() {
		DriverLoggingProxy proxy = new DriverLoggingProxy();
		Properties pr = new Properties();
		pr.put("user", "ideajdbc");
		pr.put("password", "ideajdbc");
		Connection con;
		try {
			con = proxy.connect("jdbc:ideajdbc:mysql://localhost/ideajdbc?characterEncoding=UTF-8;targetDriver=com.mysql.jdbc.Driver", pr);
			PreparedStatement ps = con.prepareStatement("select * from t_user where c_id = ?");
			ps.setString(1, "1");
			ResultSet rs = ps.executeQuery();
			rs.close();
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			fail();
		}
	} 
}
