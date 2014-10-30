/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.exception;

import com.ideamoment.ideadata.exception.IdeaDataException;

/**
 * @author Chinakite
 *
 */
public class IdeaJdbcException extends IdeaDataException {

	private static final long serialVersionUID = 4614628199256056929L;

	public IdeaJdbcException(String msg) {
		super(msg);
	}

	public IdeaJdbcException(String code, String msg) {
		super(code, msg);
	}
	
	public IdeaJdbcException(String code, String msg, Throwable t) {
		super(code, msg, t);
	}
}
