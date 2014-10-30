/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideadata.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

/**
 * PropertyMap加载器，主要功能为根据properties文件名将各配置信息加载到一个PropertyMap实例中。
 * 
 * @author Chinakite Zhang
 * @version 2010/10/10
 * @since 0.1
 */
public final class PropertyMapLoader {
	private static Logger logger = Logger.getLogger(PropertyMapLoader.class.getName());
	
	private static ServletContext servletContext;

	/**
	 * Return the servlet context when in a web environment.
	 */
	public static ServletContext getServletContext() {
		return servletContext;
	}
	
	/**
	 * Set the ServletContext for when rainbow.properties is in WEB-INF
	 * in a web application environment.
	 */
	public static void setServletContext(ServletContext servletContext) {
		PropertyMapLoader.servletContext = servletContext;
	}	
	
	public static PropertyMap load(PropertyMap propertyMap, String fileName){
		InputStream is = findInputStream(fileName);
		if (is == null){
			logger.severe(fileName+" not found");
			return propertyMap;
		} else {
			return load(propertyMap, is);
		}
	}
	
	private static InputStream findInputStream(String fileName) {
		if (fileName == null) {
			throw new NullPointerException("fileName is null?");
		}

		if (servletContext == null){
			logger.fine("No servletContext so not looking in WEB-INF for "+fileName);
			
		} else {
			// first look in WEB-INF ...
			InputStream in = servletContext.getResourceAsStream("/WEB-INF/" + fileName);
			if (in != null){
				logger.fine(fileName+" found in WEB-INF");
				return in;
			}
		}

		try {
			File f = new File(fileName);

			if (f.exists()) {
				logger.fine(fileName+" found in file system");
				return new FileInputStream(f);
			} else {
				InputStream in = findInClassPath(fileName);
				if (in != null){
					logger.fine(fileName+" found in classpath");	
				}
				return in;
			}

		} catch (FileNotFoundException ex) {
			// already made the check so this 
			// should never be thrown
			throw new RuntimeException(ex);
		} 
	}
	
	private static InputStream findInClassPath(String fileName) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
	}
	
	/**
	 * Load the inputstream returning the property map.
	 * @param propertyMap an existing property map to load into.
	 * @param in the InputStream of the properties file to load.
	 */
	private static PropertyMap load(PropertyMap propertyMap, InputStream in){
		Properties props = new Properties();
		try {
			props.load(in);
			in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		if (propertyMap == null){
			propertyMap = new PropertyMap();
		}
		
		Iterator<Entry<Object, Object>> it = props.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Object, Object> entry = it.next();
			String key = ((String)entry.getKey()).toLowerCase();
			String val = ((String)entry.getValue());
			//TODO 需要支持表达式。
			//val = PropertyExpression.eval(val);
			
			logger.finer("... loading "+key+" = "+val);
			propertyMap.put(key, val);
		}
		
		return propertyMap;
	}
}
