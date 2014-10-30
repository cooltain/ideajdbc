/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.action;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import com.ideamoment.ideadata.description.EntityDescription;
import com.ideamoment.ideadata.description.EntityDescriptionFactory;
import com.ideamoment.ideadata.description.IdDescription;
import com.ideamoment.ideajdbc.actionparser.JdbcSql;
import com.ideamoment.ideajdbc.actionparser.JdbcSqlParam;
import com.ideamoment.ideajdbc.executor.SqlExecutor;
import com.ideamoment.ideajdbc.transaction.Transaction;

/**
 * @author Chinakite
 *
 */
public class DeleteAction<T> extends AbstractAction<T> implements Action<T> {
	
	private T entity;		//要删除的实体对象
    private Class entityClass;
	private Transaction transaction;		//事务
	private Object id;
	
	@Override
	public Class getEntityClass() {
		if(entityClass != null) {
			return entityClass;
		}else if(entity != null){
			return this.entity.getClass();
		}else{
			return null;
		}
	}
	
	public DeleteAction(T entity, Transaction transaction) {
		this.entity = entity;
		this.entityClass = entity.getClass();
		this.transaction = transaction;
	}

	public DeleteAction(Class<T> entityClass, Object id, Transaction transaction) {
		this.entityClass = entityClass;
		this.id = id;
		this.transaction = transaction;
	}
	
	public int execute(){
		//检查事务
		checkTransaction(transaction);
		
		if(this.entityClass == null) {
			this.entityClass = this.entity.getClass();
		}
		
		JdbcSql sql = parseEntityToSql();
		
		//执行Sql并返回结果
		SqlExecutor executor = new SqlExecutor();
		return executor.executeUpdate(sql, transaction);
	}
	
	private JdbcSql parseEntityToSql() {
		EntityDescription entityDesc = EntityDescriptionFactory.getInstance().getEntityDescription(this.entityClass);
		IdDescription idDesc = entityDesc.getIdDescription();
		
		StringBuffer sb = new StringBuffer();
		sb.append("delete from ")
		  .append(entityDesc.getDataSet())
		  .append(" where ")
		  .append(idDesc.getDataItem())
		  .append(" = ?");
		
		JdbcSql sql = new JdbcSql();
		sql.setSql(sb.toString());
		
		List<JdbcSqlParam> params = new ArrayList<JdbcSqlParam>();
		JdbcSqlParam param = new JdbcSqlParam();
		param.setType(idDesc.getType());
		
		if(this.entity != null) {
			try {
				this.id = PropertyUtils.getProperty(this.entity, idDesc.getName());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		param.setParamValue(this.id);
		
		params.add(param);
		
		sql.setParams(params);
		
		return sql;
	}
}
