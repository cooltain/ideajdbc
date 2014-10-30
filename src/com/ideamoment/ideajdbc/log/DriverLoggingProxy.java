/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.log;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.util.Properties;
import java.util.StringTokenizer;

import static com.ideamoment.ideajdbc.log.Loggers.*;

/**
 * @author Chinakite
 *
 */
public class DriverLoggingProxy implements Driver {

	static final String urlPrefix = "jdbc:ideajdbc:";
	
	static final String targetDriverParameter = "targetDriver";

	static {
        try {
            DriverManager.registerDriver(new DriverLoggingProxy());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	
	/* (non-Javadoc)
	 * @see java.sql.Driver#acceptsURL(java.lang.String)
	 */
	@Override
	public boolean acceptsURL(String url) throws SQLException {
		return url != null && url.regionMatches(true, 0, urlPrefix, 0, urlPrefix.length());
	}

	/* (non-Javadoc)
	 * @see java.sql.Driver#connect(java.lang.String, java.util.Properties)
	 */
	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		 if (connectionLogger.isInfoEnabled()) {
	            String message = LogUtils.appendStackTrace("connect to URL {} with properties: {}");
	            connectionLogger.info(message, url, info);
	        }
	        if (!acceptsURL(url)) {
	            throw new SQLException("Invalid URL" + url);
	        }
	        url = "jdbc:" + url.substring(urlPrefix.length());
	        StringTokenizer ts = new StringTokenizer(url, ":/;=&?", false);
	        String targetDriver = null;
	        while (ts.hasMoreTokens()) {
	            String s = ts.nextToken();
	            if (targetDriverParameter.equals(s) && ts.hasMoreTokens()) {
	                targetDriver = ts.nextToken();
	                break;
	            }
	        }
	        if (targetDriver == null) {
	            throw new SQLException("Can't find targetDriver parameter in URL: " + url);
	        }
	        url = url.substring(0, url.length() - targetDriver.length() - targetDriverParameter.length() - 2);
	        try {
	            Class.forName(targetDriver);
	            return ProxyUtils.wrapByConnectionProxy(DriverManager.getConnection(url, info));
	        } catch (Exception e) {
	            throw new SQLException(e.getMessage());
	        }

	}

	/* (non-Javadoc)
	 * @see java.sql.Driver#getMajorVersion()
	 */
	@Override
	public int getMajorVersion() {
		return 1;
	}

	/* (non-Javadoc)
	 * @see java.sql.Driver#getMinorVersion()
	 */
	@Override
	public int getMinorVersion() {
		return 8;
	}

	/* (non-Javadoc)
	 * @see java.sql.Driver#getPropertyInfo(java.lang.String, java.util.Properties)
	 */
	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties properties)
			throws SQLException {
		String as[] = { "true", "false" };
        DriverPropertyInfo adriverpropertyinfo[] = new DriverPropertyInfo[6];
        DriverPropertyInfo driverpropertyinfo = new DriverPropertyInfo("user", null);
        driverpropertyinfo.value = properties.getProperty("user");
        driverpropertyinfo.required = true;
        adriverpropertyinfo[0] = driverpropertyinfo;
        driverpropertyinfo = new DriverPropertyInfo("password", null);
        driverpropertyinfo.value = properties.getProperty("password");
        driverpropertyinfo.required = true;
        adriverpropertyinfo[1] = driverpropertyinfo;
        driverpropertyinfo = new DriverPropertyInfo("get_column_name", null);
        driverpropertyinfo.value = properties.getProperty("get_column_name", "true");
        driverpropertyinfo.required = false;
        driverpropertyinfo.choices = as;
        adriverpropertyinfo[2] = driverpropertyinfo;
        driverpropertyinfo = new DriverPropertyInfo("ifexists", null);
        driverpropertyinfo.value = properties.getProperty("ifexists");
        driverpropertyinfo.required = false;
        driverpropertyinfo.choices = as;
        adriverpropertyinfo[3] = driverpropertyinfo;
        driverpropertyinfo = new DriverPropertyInfo("default_schema", null);
        driverpropertyinfo.value = properties.getProperty("default_schema");
        driverpropertyinfo.required = false;
        driverpropertyinfo.choices = as;
        adriverpropertyinfo[4] = driverpropertyinfo;
        driverpropertyinfo = new DriverPropertyInfo("shutdown", null);
        driverpropertyinfo.value = properties.getProperty("shutdown");
        driverpropertyinfo.required = false;
        driverpropertyinfo.choices = as;
        adriverpropertyinfo[5] = driverpropertyinfo;
        return adriverpropertyinfo;
	}

	/* (non-Javadoc)
	 * @see java.sql.Driver#jdbcCompliant()
	 */
	@Override
	public boolean jdbcCompliant() {
		return false;
	}

}
