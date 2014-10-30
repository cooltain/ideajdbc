/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.datasource;

import java.util.Hashtable;

import javax.sql.DataSource;

import com.ideamoment.ideajdbc.configuration.DataSourceConfig;

/**
 * 数据源管理器。
 * 
 * @author Chinakite Zhang
 * @version 2010/10/13
 * @since 0.1
 */
public class DataSourceManager {
	
	/** 
     * DataSource缓存. 
     */
    private final Hashtable<String, DataSource> dsMap = new Hashtable<String, DataSource>();
	
    /**
     * 创建dataSource时同步锁对象.
     */
    private final Object monitor = new Object();
    
    /**
     * 获取数据源。出于性能的考量，数据源被缓存在一个Hashtable中。
     * 
     * @param name Channel名称
     * @param config 数据源配置信息
     * @return DataSource 数据源
     */
	public DataSource getDataSource(String name, DataSourceConfig config) {
		if (name == null){
			throw new RuntimeException("name not defined");
		}
		
		synchronized(monitor){
			DataSource ds = dsMap.get(name);
			if (ds == null){
		    	if (config == null){
					config = new DataSourceConfig();
					config.loadSettings(name);
				}
		    	ds = new DefaultDataSource(name, config);
		        dsMap.put(name, ds); 
		    }
		    return ds;
		}
	}
}
