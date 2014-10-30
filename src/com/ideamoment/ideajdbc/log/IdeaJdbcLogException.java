/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.log;

import com.ideamoment.ideadata.exception.IdeaDataException;

/**
 * @author Chinakite
 *
 */
public class IdeaJdbcLogException extends IdeaDataException {

	private static final long serialVersionUID = -8415011414089229318L;

	public IdeaJdbcLogException(String msg) {
		super(msg);
	}
	
	public IdeaJdbcLogException(String code, String msg) {
		super(code, msg);
	}
	
	public IdeaJdbcLogException(String code, String msg, Throwable t) {
		super(code, msg, t);
	}

}
