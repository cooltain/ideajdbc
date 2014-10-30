/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideadata.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
		Class<?> currentClass = cls;
		while (currentClass != null) {
			final Field[] declaredFields = currentClass.getDeclaredFields();
			for (Field field : declaredFields) {
				allFields.add(field);
			}
			currentClass = currentClass.getSuperclass();
		}
		return allFields;
	}
}
