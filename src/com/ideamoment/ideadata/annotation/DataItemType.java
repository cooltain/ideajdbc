/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideadata.annotation;

/**
 * 数据项类型
 * 
 * @author Chinakite
 *
 */
public enum DataItemType {
	CHAR,VARCHAR,VARCHAR2,TEXT,CLOB,STRING,
	SMALLINT,SHORT,INT,INTEGER,BIGINT,LONG,FLOAT,DOUBLE,NUMERIC,DECIMAL,
	DATE,TIME,DATETIME,TIMESTAMP,
	BLOB,BINARY,BYTE,
	ARRAY,
	BOOLEAN,
	UNKNOWN
}
