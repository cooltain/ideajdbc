/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideadata.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Chinakite
 * 
 */
public class ReflectUtil {
	/**
	 * 递归获得所有一个类的所有Field
	 * 
	 * @param cls
	 * @return 所有field列表
	 */
	public static List<Field> getAllFieldsList(Class<?> cls) {
		final List<Field> allFields = new ArrayList<Field>();
		final Set<String> fieldNames = new HashSet<String>();
		Class<?> currentClass = cls;
		while (currentClass != null) {
			final Field[] declaredFields = currentClass.getDeclaredFields();
			for (Field field : declaredFields) {
			    if(fieldNames.contains(field.getName())) {
			       continue; 
			    }
				allFields.add(field);
				fieldNames.add(field.getName());
			}
			currentClass = currentClass.getSuperclass();
		}
		return allFields;
	}
	
	/**
	 * 将属性名转换为getter方法名
	 * 
	 * @param propName
	 * @return
	 */
	public static String toGetterMethodName(String propName) {
	    if(propName.charAt(0) <= 122 && propName.charAt(0) >= 97) {
	        propName = (char)(propName.charAt(0) - 32) + propName.substring(1);
	    }
	    return "get" + propName;
	}
	
	
}
