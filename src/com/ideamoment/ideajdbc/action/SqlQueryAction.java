/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.action;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ideamoment.ideadata.annotation.DataItemType;
import com.ideamoment.ideadata.util.TypeUtil;
import com.ideamoment.ideajdbc.actionparser.ActionParser;
import com.ideamoment.ideajdbc.actionparser.JdbcSql;
import com.ideamoment.ideajdbc.actionparser.JdbcSqlParam;
import com.ideamoment.ideajdbc.actionparser.SqlQueryParser;
import com.ideamoment.ideajdbc.configuration.IdeaJdbcConfiguration;
import com.ideamoment.ideajdbc.dialect.Dialect;
import com.ideamoment.ideajdbc.dialect.DialectFactory;
import com.ideamoment.ideajdbc.dialect.RdbmsType;
import com.ideamoment.ideajdbc.exception.IdeaJdbcException;
import com.ideamoment.ideajdbc.exception.IdeaJdbcExceptionCode;
import com.ideamoment.ideajdbc.executor.SqlExecutor;
import com.ideamoment.ideajdbc.resulthandler.ResultHandler;
import com.ideamoment.ideajdbc.transaction.Transaction;

/**
 * @author Chinakite
 *
 */
public class SqlQueryAction<T> extends AbstractAction<T> implements Query<T> {

	private Class<T> entityClass;	//实体类
	private String entityAlias;		//实体别名
	private String sql;		//sql语句
	private Transaction transaction;	//事务对象
	private String queryName;		//要使用的预定义sql名称
	private Map<Object, Parameter> parameters = new HashMap<Object, Parameter>();		//参数列表
	private Map<String, String> populates = new HashMap<String, String>();		//要自动组装关联的别名映射表，key为查询结果的别名，value为字段名称
	
	public SqlQueryAction() {
	}
	
	/**
	 * 构造函数。
	 * 
	 * @param entityClass 操作的实体类
	 * @param queryName 查询名称
	 * @param transaction 事务
	 */
	public SqlQueryAction(Class<T> entityClass, String queryName, Transaction transaction) {
		this.entityClass = entityClass;
		this.transaction = transaction;
		this.queryName = queryName;
	}
	
	/**
	 * 构造函数。
	 * 
	 * @param sql 查询语句
	 * @param transaction 事务
	 */
	public SqlQueryAction(String sql, Transaction transaction) {
		this.transaction = transaction;
		this.sql = sql;
	}
	
	@Override
	public Class<T> getEntityClass() {
		return entityClass;
	}

	@Override
	public List list() {
		checkTransaction(transaction);
		
		//使用ActionParser解析
		ActionParser parser = new SqlQueryParser();
		JdbcSql sql = parser.parse(this);
		
		//执行Sql并返回结果
		SqlExecutor executor = new SqlExecutor();
		ResultSet rs = executor.executeQuery(sql, transaction);
		
		ResultHandler handler = new ResultHandler();
		if(this.entityClass != null) {
			return handler.handleResultToEntity(rs, entityClass, this, false);
		}else{
			return handler.handleResultToMap(rs);
		}
	}

	@Override
	public T uniqueTo(Class<T> entityClass) {
		this.entityClass = entityClass;
		
		checkTransaction(transaction);
		//使用ActionParser解析
		ActionParser parser = new SqlQueryParser();
		JdbcSql sql = parser.parse(this);
		
		//执行Sql并返回结果
		SqlExecutor executor = new SqlExecutor();
		ResultSet rs = executor.executeQuery(sql, transaction);
		
		ResultHandler handler = new ResultHandler();
		if(this.entityClass != null) {
			List<T> result = handler.handleResultToEntity(rs, entityClass, this, true);
			if(result != null && result.size() == 1) {
				return result.get(0);
			}else{
				return null;
			}
		}else{
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.QUERY_ERR, "No special entityClass in unique method.");
		}
	}
	
	@Override
	public List<T> listTo(Class<T> entityClass) {
		return listTo(entityClass, null);
	}

	@Override
	public List<T> listTo(Class<T> entityClass, String entityAlias) {
		if(entityAlias != null) {
			this.entityAlias = entityAlias;
		}
		this.entityClass = entityClass;
		
		checkTransaction(transaction);
		
		//使用ActionParser解析
		ActionParser parser = new SqlQueryParser();
		JdbcSql sql = parser.parse(this);
		
		//执行Sql并返回结果
		SqlExecutor executor = new SqlExecutor();
		ResultSet rs = executor.executeQuery(sql, transaction);
		
		ResultHandler handler = new ResultHandler();
		return handler.handleResultToEntity(rs, entityClass, this, false);
	}
	
	@Override
	public List<T> rangeList(int start, int end) {
		checkTransaction(transaction);
		
		//使用ActionParser解析
		ActionParser parser = new SqlQueryParser();
		JdbcSql sql = parser.parse(this);
		
		int offset = start - 1;
		int length = end - start + 1;
		
		sql.getParams().add(new JdbcSqlParam(offset, DataItemType.INT));
		sql.getParams().add(new JdbcSqlParam(length, DataItemType.INT));
		sql.setSql(DialectFactory.getDialect(RdbmsType.MYSQL).pageSql(sql.getSql()));
		
		//执行Sql并返回结果
		SqlExecutor executor = new SqlExecutor();
		ResultSet rs = executor.executeQuery(sql, transaction);
		
		ResultHandler handler = new ResultHandler();
		if(this.entityClass != null) {
			return handler.handleResultToEntity(rs, entityClass, this, false);
		}else{
			return handler.handleResultToMap(rs);
		}
	}
	
	

	@Override
	public Object uniqueValue() {
		checkTransaction(transaction);
		
		//使用ActionParser解析
		ActionParser parser = new SqlQueryParser();
		JdbcSql sql = parser.parse(this);
		
		//执行Sql并返回结果
		SqlExecutor executor = new SqlExecutor();
		ResultSet rs = executor.executeQuery(sql, transaction);
		
		ResultHandler handler = new ResultHandler();
		return handler.handleResultToUniqueValue(rs);
	}

	/**
	 * 将返回结果的别名为tableAlias表的字段映射到propName属性中去
	 * 
	 * @param propName 属性名
	 * @param tableAlias	查询结果的表别名
	 */
	public Query populate(String propName, String tableAlias) {
		if(!this.populates.containsKey(tableAlias)) {
			this.populates.put(tableAlias, propName);
		}
		return this;
	}
	
	@Override
	public Query<T> setParameter(int index, Object value) {
		DataItemType type = TypeUtil.javaTypeToDataItemType(value.getClass());
		Parameter param = new Parameter(index, value, type);
		this.parameters.put(index, param);
		return this;
	}
	
	@Override
	public Query<T> setParameter(String paramName, Object value) {
		DataItemType type = TypeUtil.javaTypeToDataItemType(value.getClass());
		Parameter param = new Parameter(paramName, value, type);
		this.parameters.put(paramName, param);
		return this;
	}
	
	@Override
	public Query<T> setParameter(int index, Object value, DataItemType type) {
		Parameter param = new Parameter(index, value, type);
		this.parameters.put(index, param);
		return this;
	}

	@Override
	public Query<T> setParameter(String paramName, Object value, DataItemType type) {
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
	 * @return the queryName
	 */
	public String getQueryName() {
		return queryName;
	}
	
	/**
	 * @return the parameters
	 */
	public Map<Object, Parameter> getParameters() {
		return parameters;
	}
	
	/**
	 * @return the entityAlias
	 */
	public String getEntityAlias() {
		return entityAlias;
	}

	/**
	 * @param entityAlias the entityAlias to set
	 */
	public void setEntityAlias(String entityAlias) {
		this.entityAlias = entityAlias;
	}

	@Override
	public Map<String, String> getPopulates() {
		return this.populates;
	}

	@Override
	public Page page(int currentPage, int pageSize) {
		checkTransaction(transaction);
		
		//使用ActionParser解析
		ActionParser parser = new SqlQueryParser();
		JdbcSql sql = parser.parse(this);
		
		JdbcSql countSql = new JdbcSql();
		String sqlStr = sql.getSql();
		String countSqlStr = "select count(*) c from (" + sqlStr + ") __c__";
		countSql.setSql(countSqlStr);
		countSql.setParams(sql.getParams());
		
		//执行Sql并返回结果
		SqlExecutor executor = new SqlExecutor();
		ResultSet countRs = executor.executeQuery(countSql, transaction);
		
		int offset = (currentPage-1) * pageSize;
		sql.getParams().add(new JdbcSqlParam(offset, DataItemType.INT));
		sql.getParams().add(new JdbcSqlParam(pageSize, DataItemType.INT));
		
		sql.setSql(DialectFactory.getDialect(RdbmsType.MYSQL).pageSql(sqlStr));
		ResultSet rs = executor.executeQuery(sql, transaction);
		
		ResultHandler handler = new ResultHandler();
		long count = (Long)handler.handleResultToUniqueValue(countRs);
		
		List datas = new ArrayList();
		if(this.entityClass != null) {
			datas = handler.handleResultToEntity(rs, entityClass, this, false);
		}else{
			datas = handler.handleResultToMap(rs);
		}
		
		Page page = new Page();
		page.setCurrentPage(currentPage);
		page.setData(datas);
		page.setPageSize(pageSize);
		page.setTotalRecord(count);
		
		return page;
		
	}

	@Override
	public Page<T> pageTo(Class<T> entityClass, int currentPage, int pageSize) {
		return pageTo(entityClass, null, currentPage, pageSize);
	}
	
	@Override
	public Page<T> pageTo(Class<T> entityClass, String entityAlias, int currentPage, int pageSize) {
		if(entityAlias != null) {
			this.entityAlias = entityAlias;
		}
		this.entityClass = entityClass;
		
		checkTransaction(transaction);
		
		//使用ActionParser解析
		ActionParser parser = new SqlQueryParser();
		JdbcSql sql = parser.parse(this);
		
		JdbcSql countSql = new JdbcSql();
		String sqlStr = sql.getSql();
		String countSqlStr = "select count(*) c from (" + sqlStr + ") __c__";
		countSql.setSql(countSqlStr);
		countSql.setParams(sql.getParams());
		
		//执行Sql并返回结果
		SqlExecutor executor = new SqlExecutor();
		ResultSet countRs = executor.executeQuery(countSql, transaction);
		
		int offset = (currentPage-1) * pageSize;
		sql.getParams().add(new JdbcSqlParam(offset, DataItemType.INT));
		sql.getParams().add(new JdbcSqlParam(pageSize, DataItemType.INT));
		sql.setSql(DialectFactory.getDialect(RdbmsType.MYSQL).pageSql(sqlStr));
		ResultSet rs = executor.executeQuery(sql, transaction);
		
		ResultHandler handler = new ResultHandler();
		long count = (Long)handler.handleResultToUniqueValue(countRs);
		
		List datas = handler.handleResultToEntity(rs, entityClass, this, false);
		
		Page page = new Page();
		page.setCurrentPage(currentPage);
		page.setData(datas);
		page.setPageSize(pageSize);
		page.setTotalRecord(count);
		
		return page;
	}

	@Override
	public List<T> rangeListTo(Class<T> entityClass, int start, int end) {
		return rangeListTo(entityClass, null, start, end);
	}
	
	@Override
	public List<T> rangeListTo(Class<T> entityClass, String entityAlias, int start, int end) {
		if(entityAlias != null) {
			this.entityAlias = entityAlias;
		}
		this.entityClass = entityClass;
		
		checkTransaction(transaction);
		
		//使用ActionParser解析
		ActionParser parser = new SqlQueryParser();
		JdbcSql sql = parser.parse(this);
		
		int offset = start - 1;
		int length = end - start + 1;
		
		sql.getParams().add(new JdbcSqlParam(offset, DataItemType.INT));
		sql.getParams().add(new JdbcSqlParam(length, DataItemType.INT));
		sql.setSql(DialectFactory.getDialect(RdbmsType.MYSQL).pageSql(sql.getSql()));
		
		//执行Sql并返回结果
		SqlExecutor executor = new SqlExecutor();
		ResultSet rs = executor.executeQuery(sql, transaction);
		
		ResultHandler handler = new ResultHandler();
		return handler.handleResultToEntity(rs, entityClass, this, false);
	}
}
