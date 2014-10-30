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
public interface PropertyDescriptionDecoration {
	/**
	 * 修饰PropertyDescription
	 * 
	 * @param propertyDescription
	 * @return
	 */
	public PropertyDescription decorate(PropertyDescription propertyDescription);
	
	/**
	 * 修饰PropertyDescription
	 * 
	 * @param propertyDescription
	 * @return
	 */
	public IdDescription decorate(IdDescription idDescription);
}
