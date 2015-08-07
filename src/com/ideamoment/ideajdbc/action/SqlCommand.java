/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.action;

import java.util.HashMap;
import java.util.Map;

import com.ideamoment.ideadata.annotation.DataItemType;
import com.ideamoment.ideadata.util.TypeUtil;
import com.ideamoment.ideajdbc.actionparser.JdbcSql;
import com.ideamoment.ideajdbc.actionparser.SqlCommandParser;
import com.ideamoment.ideajdbc.exception.IdeaJdbcException;
import com.ideamoment.ideajdbc.exception.IdeaJdbcExceptionCode;
import com.ideamoment.ideajdbc.executor.SqlExecutor;
import com.ideamoment.ideajdbc.transaction.Transaction;

/**
 * @author Chinakite
 *
 */
public class SqlCommand implements Command {

	private String sql;		//sql语句
	private Transaction transaction;	//事务对象
	private Map<Object, Parameter> parameters = new HashMap<Object, Parameter>();		//参数列表
	
	/**
	 * 
	 */
	public SqlCommand(){
	}
	
	/**
	 * 
	 * 
	 * @param sql
	 * @param transaction
	 */
	public SqlCommand(String sql, Transaction transaction) {
		this.sql = sql;
		this.transaction = transaction;
	}
	
	/* (non-Javadoc)
	 * @see com.ideamoment.ideajdbc.action.Command#execute()
	 */
	@Override
	public int execute() {
		//检查事务
		checkTransaction(transaction);
		
		SqlCommandParser parser = new SqlCommandParser();
		JdbcSql sql = parser.parse(this);
		
		//执行Sql并返回结果
		SqlExecutor executor = new SqlExecutor();
		return executor.executeUpdate(sql, transaction);
	}

	@Override
	public Command setParameter(int index, Object value) {
		DataItemType type = TypeUtil.javaTypeToDataItemType(value.getClass());
		Parameter param = new Parameter(index, value, type);
		this.parameters.put(index, param);
		return this;
	}
	
	@Override
	public Command setParameter(String paramName, Object value) {
	    if(value == null) {
	        Parameter param = new Parameter(paramName, value, DataItemType.NULL);
	        this.parameters.put(paramName, param);
	    }else{
	        DataItemType type = TypeUtil.javaTypeToDataItemType(value.getClass());
	        Parameter param = new Parameter(paramName, value, type);
	        this.parameters.put(paramName, param);
	    }
		
		return this;
	}
	
	@Override
	public Command setParameter(int index, Object value, DataItemType type) {
		Parameter param = new Parameter(index, value, type);
		this.parameters.put(index, param);
		return this;
	}

	@Override
	public Command setParameter(String paramName, Object value, DataItemType type) {
		Parameter param = new Parameter(paramName, value, type);
		this.parameters.put(paramName, param);
		return this;
	}

	/**
	 * @return the sql
	 */
	public String getSql() {
		return sql;
	}

	/**
	 * @param sql the sql to set
	 */
	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * @param transaction the transaction to set
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	/**
	 * @return the parameters
	 */
	public Map<Object, Parameter> getParameters() {
		return parameters;
	}
	
	/**
	 * 检查事务是否有效，如果无效抛出Rainbow异常。
	 * 
	 * @param transaction 事务
	 */
	public void checkTransaction(Transaction transaction) {
		//检查事务
		if(transaction == null || !transaction.isActive()) {
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.TX_NOTACTIVE_ERR, "The transaction is not alive when sql command executing.");
		}
	}

}
