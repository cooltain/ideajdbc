/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.tool.mysql;

import java.util.HashMap;
import java.util.Map;

import com.ideamoment.ideadata.annotation.DataItemType;

/**
 * @author Chinakite
 *
 */
public class MySqlToolMapper {
	/**
	 * 长度映射
	 */
	public static final Map<DataItemType, Integer> lengthMapper = new HashMap<DataItemType, Integer>();

	static {
		lengthMapper.put(DataItemType.INT, 11);
		lengthMapper.put(DataItemType.INTEGER, 11);
		lengthMapper.put(DataItemType.VARCHAR2, 256);
		lengthMapper.put(DataItemType.VARCHAR, 256);
		lengthMapper.put(DataItemType.STRING, 256);
		lengthMapper.put(DataItemType.DATE, -1);
		lengthMapper.put(DataItemType.TIME, -1);
		lengthMapper.put(DataItemType.DATETIME, -1);
		lengthMapper.put(DataItemType.TIMESTAMP, -1);
	}
	
	/**
	 * 长度映射
	 */
	public static final Map<DataItemType, String> typeMapper = new HashMap<DataItemType, String>();

	static {
		typeMapper.put(DataItemType.INT, "int");
		typeMapper.put(DataItemType.INTEGER, "int");
		typeMapper.put(DataItemType.VARCHAR2, "varchar");
		typeMapper.put(DataItemType.VARCHAR, "varchar");
		typeMapper.put(DataItemType.STRING, "varchar");
		typeMapper.put(DataItemType.DATE, "date");
		typeMapper.put(DataItemType.TIME, "Time");
		typeMapper.put(DataItemType.DATETIME, "datetime");
		typeMapper.put(DataItemType.TIMESTAMP, "timestamp");
	}
}
