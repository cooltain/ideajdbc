/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideadata.exception;

/**
 * @author Chinakite
 *
 */
public class IdeaDataException extends RuntimeException {
	private static final long serialVersionUID = -3861404150412122019L;
	
	/**
	 * 异常编码
	 */
	protected String code;
	
	/**
	 * 异常消息
	 */
	protected String msg;
	
	public IdeaDataException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public IdeaDataException(String msg, Throwable t) {
		super(msg, t);
		this.msg = msg;
	}
	
	public IdeaDataException(String code, String msg) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public IdeaDataException(String code, String msg, Throwable t) {
		super(msg, t);
		this.msg = msg;
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

}
