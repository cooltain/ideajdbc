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
public class IdDescription extends PropertyDescription {
	/**
	 * 主键生成器
	 * 
	 * @return
	 */
	private String generator;

	/**
	 * @return the generator
	 */
	public String getGenerator() {
		return generator;
	}

	/**
	 * @param generator the generator to set
	 */
	public void setGenerator(String generator) {
		this.generator = generator;
	}
	
}
