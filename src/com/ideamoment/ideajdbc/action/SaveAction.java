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
import org.apache.commons.lang3.StringUtils;

import com.ideamoment.ideadata.description.EntityDescription;
import com.ideamoment.ideadata.description.EntityDescriptionFactory;
import com.ideamoment.ideadata.description.PropertyDescription;
import com.ideamoment.ideadata.generator.GeneratorFactory;
import com.ideamoment.ideajdbc.actionparser.JdbcSql;
import com.ideamoment.ideajdbc.actionparser.JdbcSqlParam;
import com.ideamoment.ideajdbc.executor.SqlExecutor;
import com.ideamoment.ideajdbc.transaction.Transaction;

/**
 * @author Chinakite
 *
 */
public class SaveAction<T> extends AbstractAction<T> implements Action<T> {

	private T entity;		//要保存的实体对象

	private Transaction transaction;		//事务
	
	@Override
	public Class getEntityClass() {
		return this.entity.getClass();
	}

	public SaveAction(T entity, Transaction transaction) {
		this.entity = entity;
		this.transaction = transaction;
	}
	
	public T execute(){
		//检查事务
		checkTransaction(transaction);
		
		JdbcSql sql = parseEntityToSql();
		
		//执行Sql并返回结果
		SqlExecutor executor = new SqlExecutor();
		executor.executeUpdate(sql, transaction);
		
		return entity;
	}

	private JdbcSql parseEntityToSql() {
		EntityDescription entityDescription = EntityDescriptionFactory.getInstance().getEntityDescription(entity.getClass());
		
		List<JdbcSqlParam> params = new ArrayList<JdbcSqlParam>();
		
		StringBuffer sb = new StringBuffer();
		sb.append("insert into ")
		  .append(entityDescription.getDataSet())
		  .append(" (")
		  .append(entityDescription.getIdDescription().getDataItem());
		
		JdbcSqlParam idParam = new JdbcSqlParam();
		if(StringUtils.isEmpty(entityDescription.getIdDescription().getGenerator())) {
			Object id;
			try {
				id = PropertyUtils.getProperty(entity, entityDescription.getIdDescription().getName());
				idParam.setParamValue(id);
				PropertyUtils.setProperty(entity, entityDescription.getIdDescription().getName(), id);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}else{
			if("uuid".equals(entityDescription.getIdDescription().getGenerator())) {
				String id = (String)GeneratorFactory.getInstance().getGenerator("uuid").generate(entity);
				idParam.setParamValue(id);
				try {
					PropertyUtils.setProperty(entity, entityDescription.getIdDescription().getName(), id);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
		idParam.setType(entityDescription.getIdDescription().getType());
		params.add(idParam);
		
		
		List<PropertyDescription> properties = entityDescription.getProperties();
		int paramLength = 1;
		if(properties != null) {
			for(PropertyDescription propDesc : properties) {
				sb.append(", ").append(propDesc.getDataItem());
				
				//处理参数值
				JdbcSqlParam param = new JdbcSqlParam();
				Object paramValue;
				try {
					paramValue = PropertyUtils.getProperty(entity, propDesc.getName());
					param.setParamValue(paramValue);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
				param.setType(propDesc.getType());
				params.add(param);
			}
			paramLength = paramLength + properties.size();
		}
		sb.append(")values(");
		for(int i=0; i<paramLength; i++) {
			if(i > 0) {
				sb.append(", ");
			}
			sb.append("?");
		}
		sb.append(")");
		
		JdbcSql sql = new JdbcSql();
		sql.setSql(sb.toString());
		sql.setParams(params);
		
		return sql;
	}
}
