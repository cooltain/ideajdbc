/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideadata.description;

/**
 * @author Chinakite
 *
 */
public class RefDescription {
	/**
	 * 属性名称
	 */
	private String name;
	
	/**
	 * 关联的实体类，如果是集合类则必须要指定具体的关联类，如果是一对一或多对一则无需指定
	 */
	private Class entityClass;
	
	/**
	 * 属性的类型
	 */
	private Class propClass;

	/**
	 * @return the propClass
	 */
	public Class getPropClass() {
		return propClass;
	}

	/**
	 * @param propClass the propClass to set
	 */
	public void setPropClass(Class propClass) {
		this.propClass = propClass;
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

	/**
	 * @return the entityClass
	 */
	public Class getEntityClass() {
		return entityClass;
	}

	/**
	 * @param entityClass the entityClass to set
	 */
	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}
	
	
}
