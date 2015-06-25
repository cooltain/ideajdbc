/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.configuration;

import javax.sql.DataSource;

/**
 * @author Chinakite
 *
 */
public class DbConfig {
	public static final String DEFAULT_TXMANAGER = "_default_";
	
	private String name;
	
	private DataSource dataSource;
	
	private String txManagerClazz;
	
	public String getTxManagerClazz() {
		return txManagerClazz;
	}

	public void setTxManagerClazz(String txManagerClazz) {
	    this.txManagerClazz = txManagerClazz;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/** The data source config. */
	private DataSourceConfig dataSourceConfig = new DataSourceConfig();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataSourceConfig getDataSourceConfig() {
		return dataSourceConfig;
	}

	public void setDataSourceConfig(DataSourceConfig dataSourceConfig) {
		this.dataSourceConfig = dataSourceConfig;
	}
	
	/**
	 * Load the configuration settings from the properties file.
	 */
	public void loadSettings() {
		txManagerClazz = IdeaJdbcConfiguration.get("datasource." + name + ".txManager", DEFAULT_TXMANAGER);
		if (dataSourceConfig == null){
			dataSourceConfig = new DataSourceConfig();
		}
		dataSourceConfig.loadSettings(name);
	}
}
