/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideadata.configuration;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * 封装的一个方便使用的读取Properties文件的类；使用LinkedHashMap存储读取的Properties文件中的数据。
 * 配合PropertyMapLoader使用。
 * 
 * @author Chinakite Zhang
 * @version 2010/10/10
 * @since 0.1
 */
public final class PropertyMap {
	private static final long serialVersionUID = 1L;

	private LinkedHashMap<String,String> map = new LinkedHashMap<String,String>();
	
	public String toString() {
		return map.toString();
	}
	
	public synchronized boolean getBoolean(String key, boolean defaultValue){
		String value = get(key);
		if (value == null){
			return defaultValue;
		} else {
			return Boolean.parseBoolean(value);
		}
	}
	
	public synchronized int getInt(String key, int defaultValue){
		String value = get(key);
		if (value == null){
			return defaultValue;
		} else {
			return Integer.parseInt(value);
		}
	}
	
	public synchronized long getLong(String key, long defaultValue){
		String value = get(key);
		if (value == null){
			return defaultValue;
		} else {
			return Long.parseLong(value);
		}
	}
	
	public synchronized String get(String key, String defaultValue){
		String value = map.get(key.toLowerCase());
		return value == null ? defaultValue : value;
	}	
	
	public synchronized String get(String key){
		return map.get(key.toLowerCase());
	}

	synchronized void putAll(Map<String,String> keyValueMap){
		Iterator<Entry<String, String>> it = keyValueMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			put(entry.getKey(), entry.getValue());
		}
	}

	synchronized String put(String key, String value){
		return map.put(key.toLowerCase(), value);
	}

	synchronized String remove(String key){
		return map.remove(key.toLowerCase());
	}

	public Set<String> keySet() {
		return map.keySet();
	}
}
