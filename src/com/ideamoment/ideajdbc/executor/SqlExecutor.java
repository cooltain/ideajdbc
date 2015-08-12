/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.executor;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.ideamoment.ideadata.annotation.DataItemType;
import com.ideamoment.ideajdbc.actionparser.JdbcSql;
import com.ideamoment.ideajdbc.actionparser.JdbcSqlParam;
import com.ideamoment.ideajdbc.exception.IdeaJdbcException;
import com.ideamoment.ideajdbc.exception.IdeaJdbcExceptionCode;
import com.ideamoment.ideajdbc.transaction.Transaction;
import com.ideamoment.ideajdbc.util.DateUtil;

/**
 * @author Chinakite
 *
 */
/**
 * Sql执行器。使用JDBC执行具体的SQL语句。
 * 
 * @author Chinakite Zhang
 * @version 2010-10-18
 * @since 0.1
 */
public class SqlExecutor {
	/**
	 * 执行查询的SQL语句
	 * 
	 * @param sql SQL语句
	 * @param tx 事务
	 * @return ResultSet 结果集
	 */
	public ResultSet executeQuery(JdbcSql sql, Transaction tx) {
		Connection conn = tx.getConnection();
		if(conn == null){
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_NOTACTIVE_ERR, "Can not execute query because of getting null connection from transaction.");
		}

		try {
			PreparedStatement pstmt = conn.prepareStatement(sql.getSql());
			List<JdbcSqlParam> params = sql.getParams();
			
			setPreparedStatementParams(pstmt, params);
			ResultSet rs = pstmt.executeQuery();
			return rs;
		} catch (SQLException e) {
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.SQL_EXECUTE_ERR, "Can not execute query, the nested exception is " + e.getMessage(), e);
		}
	}
	
	/**
	 * 执行sql更新语句
	 * 
	 * @param sql SQL语句
	 * @param tx 事务
	 * @return 返回受影响的结果条数
	 */
	public int executeUpdate(JdbcSql sql, Transaction tx) {
		Connection conn = tx.getConnection();
		if(conn == null){
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_NOTACTIVE_ERR, "Can not execute sqlUpdate because of getting null connection from transaction.");
		}
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql.getSql());
			List<JdbcSqlParam> params = sql.getParams();
			
			setPreparedStatementParams(pstmt, params);
			int rowEffected = pstmt.executeUpdate();
			return rowEffected;
		} catch (SQLException e) {
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.SQL_EXECUTE_ERR, "Can not execute query, the nested exception is " + e.getMessage(), e);
		}
	}

	/**
	 * @param pstmt
	 * @param params
	 * @throws SQLException
	 */
	private void setPreparedStatementParams(PreparedStatement pstmt,
			List<JdbcSqlParam> params) throws SQLException {
		if(params != null && params.size() > 0) {
			for(int i = 1; i <= params.size(); i++) {
				JdbcSqlParam sp = params.get(i-1);
				DataItemType paramType = sp.getType();
				Object paramValue = sp.getParamValue();
				
				if(paramType == null||paramValue==null) {
					pstmt.setObject(i, paramValue);
					continue;
				}
				
				switch (paramType) {
					case VARCHAR:
					case VARCHAR2:
					case CHAR:
					case TEXT:
					case CLOB:
					case STRING:
						pstmt.setString(i, (String)paramValue);
						break;
					case SMALLINT:
					case SHORT:
						pstmt.setShort(i, (Short)paramValue);
						break;
					case INT:
					case INTEGER:
						pstmt.setInt(i, (Integer)paramValue);
						break;
					case LONG:
					case BIGINT:
						pstmt.setLong(i, (Long)paramValue);
						break;
					case FLOAT:
						pstmt.setFloat(i, (Float)paramValue);
						break;
					case DOUBLE:
						pstmt.setDouble(i, (Double)paramValue);
						break;
					case DECIMAL:
						pstmt.setBigDecimal(i, (BigDecimal)paramValue);
						break;
					case BOOLEAN:
						pstmt.setBoolean(i, (Boolean)paramValue);
						break;
					case DATE:
						if(paramValue instanceof java.util.Date) {
							pstmt.setDate(i, DateUtil.toSqlDate((java.util.Date)paramValue));
						}else{
							pstmt.setDate(i, (java.sql.Date)paramValue);
						}
						break;
					case TIME:
						if(paramValue instanceof java.util.Date) {
							pstmt.setTime(i, DateUtil.toSqlTime((java.util.Date)paramValue));
						}else{
							pstmt.setTime(i, (java.sql.Time)paramValue);
						}
						break;
					case DATETIME:
					case TIMESTAMP:
						if(paramValue instanceof java.util.Date) {
							pstmt.setTimestamp(i, DateUtil.toSqlTimestamp((java.util.Date)paramValue));
						}else{
							pstmt.setTimestamp(i, (java.sql.Timestamp)paramValue);
						}
						break;
					case BLOB:
					    pstmt.setBlob(i, (Blob)paramValue);
					case BYTE:
						break;
					default:
						pstmt.setObject(i, paramValue);
						break;
				}
			}
		}
	}
}
