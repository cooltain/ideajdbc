/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideadata.description;

import com.ideamoment.ideadata.annotation.DataItemType;

/**
 * @author Chinakite
 *
 */
public class PropertyDescription {
	/**
	 * 属性名称
	 */
	private String name;
	
	/**
	 * 数据项类型
	 */
	private DataItemType type;
	
	/**
	 * 数据项名称
	 */
	private String dataItem;
	
	/**
	 * 数据项长度
	 */
	private int length = -1;

	/**
	 * @return the type
	 */
	public DataItemType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(DataItemType type) {
		this.type = type;
	}

	/**
	 * @return the dataItem
	 */
	public String getDataItem() {
		return dataItem;
	}

	/**
	 * @param dataItem the dataItem to set
	 */
	public void setDataItem(String dataItem) {
		this.dataItem = dataItem;
	}

	/**
	 * @return the length
	 */
	public int getLength() {
		return length;
	}

	/**
	 * @param length the length to set
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
