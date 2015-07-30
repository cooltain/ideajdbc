/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideadata.util;

import java.util.Date;

import com.ideamoment.ideadata.annotation.DataItemType;

/**
 * @author Chinakite
 *
 */
public class TypeUtil {
	public static DataItemType javaTypeToDataItemType(Class clazz) {
		if(clazz.isPrimitive()) {
			if(clazz.equals(Integer.TYPE)) {
				return DataItemType.INT;
			}else if(clazz.equals(Short.TYPE)) {
				return DataItemType.SHORT;
			}else if(clazz.equals(Long.TYPE)) {
				return DataItemType.LONG;
			}else if(clazz.equals(Float.TYPE)) {
				return DataItemType.FLOAT;
			}else if(clazz.equals(Double.TYPE)) {
				return DataItemType.DOUBLE;
			}else if(clazz.equals(Character.TYPE)) {
				return DataItemType.CHAR;
			}else if(clazz.equals(Boolean.TYPE)) {
				return DataItemType.BOOLEAN;
			}else if(clazz.equals(Byte.TYPE)) {
				return DataItemType.BYTE;
			}
		}else{
			if(clazz.equals(Integer.class)){
				return DataItemType.INT;
			}else if(clazz.equals(String.class)) {
				return DataItemType.STRING;
			}else if(clazz.equals(Short.class)) {
				return DataItemType.SHORT;
			}else if(clazz.equals(Long.class)) {
				return DataItemType.LONG;
			}else if(clazz.equals(Float.class)) {
				return DataItemType.FLOAT;
			}else if(clazz.equals(Double.class)) {
				return DataItemType.DOUBLE;
			}else if(clazz.equals(Character.class)) {
				return DataItemType.CHAR;
			}else if(clazz.equals(Byte.class)) {
				return DataItemType.BYTE;
			}else if(clazz.equals(Boolean.class)) {
				return DataItemType.BOOLEAN;
			}else if(clazz.equals(Date.class)) {
				return DataItemType.DATETIME;
			}else if(clazz.equals(java.sql.Date.class)) {
				return DataItemType.DATE;
			}else if(clazz.equals(java.sql.Time.class)) {
				return DataItemType.TIME;
			}else if(clazz.equals(java.sql.Timestamp.class)) {
				return DataItemType.TIMESTAMP;
			}else if(clazz.equals(java.sql.Blob.class)) {
			    return DataItemType.BLOB;
			}
		}
		
		return null;
	}
}
