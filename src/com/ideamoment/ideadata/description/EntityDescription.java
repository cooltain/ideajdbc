/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideadata.description;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Chinakite
 *
 */
public class EntityDescription {
	private final  Logger logger  =  LoggerFactory.getLogger(EntityDescriptionCache.class);
	
	/**
	 * 实体类
	 */
	private Class entityClazz;
	
	/**
	 * 数据集名称
	 */
	private String dataSet;
	
	/**
	 * 主键描述
	 */
	private IdDescription idDescription;
	

	/**
	 * 实体的所有字段描述信息列表
	 */
	private List<PropertyDescription> properties = new ArrayList<PropertyDescription>();
	
	/**
	 * 使用Java字段名称索引的实体所有字段描述信息
	 */
	private Map<String, PropertyDescription> propertyIndexes = new HashMap<String, PropertyDescription>();
	
	/**
	 * 使用数据库字段名称索引的实体所有字段描述信息
	 */
	private Map<String, PropertyDescription> dataItemIndexes = new HashMap<String, PropertyDescription>();

	/**
	 * 在实体上声明的SQL查询语句列表
	 */
	private Map<String, String> sqlQueries = new HashMap<String, String>();
	
	/**
	 * 在实体上声明的SQL更新语句列表
	 */
	private Map<String, String> sqlUpdates = new HashMap<String, String>();

	/**
	 * 引用关系描述信息
	 */
	private Map<String, RefDescription> refDescriptions = new HashMap<String, RefDescription>();
	
	/**
	 * 
	 * @param propertyDescription
	 */
	public void addPropertyDescription(PropertyDescription propertyDescription) {
		String propName = propertyDescription.getName();
		if(!propertyIndexes.containsKey(propName)) {
			properties.add(propertyDescription);
		}
		propertyIndexes.put(propName, propertyDescription);
		dataItemIndexes.put(propertyDescription.getDataItem(), propertyDescription);
	}
	
	/**
	 * 根据属性名获取属性描述信息
	 * 
	 * @param propName 属性名
	 * @return 属性描述信息
	 */
	public PropertyDescription getPropertyDescription(String propName) {
		return propertyIndexes.get(propName);
	}
	
	/**
	 * 根据数据项名称获取属性描述信息
	 * 
	 * @param dataItem 数据项名称
	 * @return 属性描述信息
	 */
	public PropertyDescription getPropertyDescriptionByDataItem(String dataItem) {
		return dataItemIndexes.get(dataItem);
	}
	
	/**
	 * 添加一条SQL Query注解记录
	 * 
	 * @param name SQL Query的名称 
	 * @param sql SQL语句
	 */
	public void addSqlQuery(String name, String sql) {
		sqlQueries.put(name, sql);
	}
	
	/**
	 * 根据名称获取sql语句
	 * 
	 * @param name sql语句名称
	 * @return sql语句
	 */
	public String getSqlQuery(String name) {
		return sqlQueries.get(name);
	}
	
	/**
	 * 添加一条SQL Update注解记录
	 * 
	 * @param name SQL Update的名称 
	 * @param sql SQL语句
	 */
	public void addSqlUpdate(String name, String sql) {
		sqlUpdates.put(name, sql);
	}
	
	/**
	 * 增加一条关联描述信息
	 * 
	 * @param refDescription
	 */
	public void addRefDescription(RefDescription refDescription) {
		String propName = refDescription.getName();
		if(!refDescriptions.containsKey(propName)) {
			refDescriptions.put(propName, refDescription);
		}
	}
	
	/**
	 * 获取关联描述信息
	 * 
	 * @param propName 属性名称
	 * @return RefDescription
	 */
	public RefDescription getRefDescription(String propName) {
		return refDescriptions.get(propName);
	}
	
	/**
	 * @return the entityClazz
	 */
	public Class getEntityClazz() {
		return entityClazz;
	}

	/**
	 * @param entityClazz the entityClazz to set
	 */
	public void setEntityClazz(Class entityClazz) {
		this.entityClazz = entityClazz;
	}

	/**
	 * @return the dataSet
	 */
	public String getDataSet() {
		return dataSet;
	}

	/**
	 * @param dataSet the dataSet to set
	 */
	public void setDataSet(String dataSet) {
		this.dataSet = dataSet;
	}
	
	/**
	 * @return the idDescriptor
	 */
	public IdDescription getIdDescription() {
		return idDescription;
	}

	/**
	 * @param idDescriptor the idDescriptor to set
	 */
	public void setIdDescription(IdDescription idDescription) {
		this.idDescription = idDescription;
	}

	/**
	 * 获取属性个数
	 * 
	 * @return
	 */
	public int getPropertySize() {
		return properties.size();
	}
	
	/**
	 * @return the properties
	 */
	public List<PropertyDescription> getProperties() {
		return properties;
	}
	
	/**
	 * 当前实体的所有字段，以逗号分隔，用于select场景
	 * 
	 * @return
	 */
	public String allDataItemString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.idDescription.getDataItem());
		for(PropertyDescription propDesc : properties) {
			sb.append(",").append(propDesc.getDataItem()).append(" ");
		}
		return sb.toString();
	}
}
