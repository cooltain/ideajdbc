/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.spring;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;

import com.ideamoment.ideadata.configuration.PropertyMap;
import com.ideamoment.ideajdbc.configuration.DbConfig;
import com.ideamoment.ideajdbc.configuration.IdeaJdbcConfiguration;
import com.ideamoment.ideajdbc.server.Db;
import com.ideamoment.ideajdbc.server.DbManager;

/**
 * @author Administrator
 * @version 2012-8-9
 * @since 0.1
 */
public class IdeaJdbcInitializingBean implements InitializingBean {

	
	
	private String config = "ideajdbc.properties";
	
	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		
		
		Set<String> createdDbs = new HashSet<String>();
		
		PropertyMap propertyMap = IdeaJdbcConfiguration.getPropertyMap(config);
		String defaultDbName = IdeaJdbcConfiguration.get("datasource.default", null);
		for(String key : propertyMap.keySet()) {
			if(key.startsWith("datasource.")
					&& key.endsWith(".url")) {
				String dbName = key.substring(11, key.length() - 4);
				if(!dbName.equals(defaultDbName)) {
					if(!createdDbs.contains(dbName)) {
						DbConfig dbConfig = new DbConfig();
						dbConfig.setName(dbName);
						dbConfig.loadSettings();
						Db db = DbManager.getInstance().getDb(dbName);
						createdDbs.add(dbName);
					}
				}
			}
		}
		
	}

}
