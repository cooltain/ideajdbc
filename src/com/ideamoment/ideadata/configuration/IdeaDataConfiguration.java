/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideadata.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Chinakite
 *
 */
public class IdeaDataConfiguration {
	private static PropertyMap globalMap;
	public static boolean inited = false;
	
	protected static void initPropertyMap(String configPath) {
		String fileName = configPath;
		
		globalMap = PropertyMapLoader.load(null, fileName);
		
		if (globalMap == null){
			// rainbow.properties file was not found... but that 
			// is ok because we are likely doing programmatic config
			globalMap = new PropertyMap();
		}
		
		inited = true;
	}
	
	/**
	 * 初始化配置文件路径
	 * 
	 * @param configPath
	 */
	public static synchronized void initConfig(String configPath) {
		if(!inited) {
			initPropertyMap(configPath);
		}
	}
	
	/**
	 * Return the property map loading it if required.
	 */
	public static synchronized PropertyMap getPropertyMap() {
		if (globalMap == null){
			initPropertyMap("ideadata.properties");
		}
		return globalMap;
	}
	
	/**
	 * Return the property map loading it if required.
	 */
	public static synchronized PropertyMap getPropertyMap(String configPath) {
		if (globalMap == null){
			initPropertyMap(configPath);
		}
		return globalMap;
	}
	
	/**
	 * Return a String property with a default value.
	 */
	public static synchronized String get(String key, String defaultValue) {
		return getPropertyMap().get(key, defaultValue);
	}
	
	/**
	 * Return a int property with a default value.
	 */
	public static synchronized int getInt(String key, int defaultValue) {
		return getPropertyMap().getInt(key, defaultValue);
	}
	
	/**
	 * Return a long property with a default value.
	 */
	public static synchronized long getLong(String key, long defaultValue) {
		return getPropertyMap().getLong(key, defaultValue);
	}
	
	/**
	 * Return a boolean property with a default value.
	 */
	public static synchronized boolean getBoolean(String key, boolean defaultValue) {
		return getPropertyMap().getBoolean(key, defaultValue);
	}
	
	/**
	 * Set a property return the previous value.
	 */
	public static synchronized String put(String key, String value) {
		return getPropertyMap().put(key, value);
	}

	/**
	 * Set a Map of key value properties.
	 */
	public static synchronized void putAll(Map<String,String> keyValueMap) {
		getPropertyMap().putAll(keyValueMap);
	}
	
	/**
	 * 根据key模糊查找配置项
	 * 
	 * @param keyPattern key的匹配串，目前只支持以XXX开头的模式，即传入参数为datasource.*
	 * @return
	 */
	public static synchronized List<String[]> getConfigurationItems(String keyPattern) {
		List<String[]> result = new ArrayList<String[]>();
		Set<String> keys = getPropertyMap().keySet();
		if(keyPattern.trim().equals("*")) {
			for(String key : keys) {
				String[] keyValue = new String[2];
				keyValue[0] = key;
				keyValue[1] = getPropertyMap().get(key);
				result.add(keyValue);
			}
		}else{
			int startPos = keyPattern.indexOf('*');
			if(startPos > 0) {
				String startString = keyPattern.substring(0, keyPattern.length() - 1);
				for(String key : keys) {
					if(key.startsWith(startString)) {
						String[] keyValue = new String[2];
						keyValue[0] = key;
						keyValue[1] = getPropertyMap().get(key);
						result.add(keyValue);
					}
				}
			}else{
				String[] keyValue = new String[2];
				keyValue[0] = keyPattern;
				keyValue[1] = getPropertyMap().get(keyPattern);
				result.add(keyValue);
			}
		}
		return result;
	}
}
