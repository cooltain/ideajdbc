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

import com.ideamoment.ideadata.annotation.DataItemType;
import com.ideamoment.ideadata.description.EntityDescription;
import com.ideamoment.ideadata.description.EntityDescriptionFactory;
import com.ideamoment.ideadata.description.IdDescription;
import com.ideamoment.ideadata.description.PropertyDescription;
import com.ideamoment.ideadata.util.TypeUtil;
import com.ideamoment.ideajdbc.actionparser.JdbcSql;
import com.ideamoment.ideajdbc.actionparser.JdbcSqlParam;
import com.ideamoment.ideajdbc.description.JdbcEntityDescription;
import com.ideamoment.ideajdbc.description.JdbcEntityDescriptionFactory;
import com.ideamoment.ideajdbc.executor.SqlExecutor;
import com.ideamoment.ideajdbc.transaction.Transaction;

/**
 * @author Chinakite
 *
 */
public class UpdateAction<T> extends AbstractAction<T> implements Action<T> {

	private T entity;		//要删除的实体对象
    private Class entityClass;
	private Transaction transaction;		//事务
	private Object id;
	private List<Parameter> updateProperties = new ArrayList<Parameter>();
	
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

	public UpdateAction(T entity, Transaction transaction) {
		this.entity = entity;
		this.entityClass = entity.getClass();
		this.transaction = transaction;
	}

	public UpdateAction(Class<T> entityClass, Object id, Transaction transaction) {
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
	
	/**
	 * 
	 * @param propName
	 * @param value
	 * @return
	 */
	public UpdateAction setProperty(String propName, Object value) {
		DataItemType type = TypeUtil.javaTypeToDataItemType(value.getClass());
		Parameter p = new Parameter(propName, value, type);
		this.updateProperties.add(p);
		return this;
	}
	
	public UpdateAction setProperty(String propName, Object value, DataItemType type) {
		Parameter p = new Parameter(propName, value, type);
		this.updateProperties.add(p);
		return this;
	}
	
	private JdbcSql parseEntityToSql() {
		try {
		    JdbcEntityDescription entityDesc = JdbcEntityDescriptionFactory.getInstance().getEntityDescription(this.entityClass);
			IdDescription idDesc = entityDesc.getIdDescription();
			
			StringBuffer sb = new StringBuffer();
			sb.append("update ")
			  .append(entityDesc.getDataSet())
			  .append(" set ");
			
			List<JdbcSqlParam> params = new ArrayList<JdbcSqlParam>();
			
			if(this.entity != null) {
				//处理非主键属性
				List<PropertyDescription> properties = entityDesc.getProperties();
				int i=0;
				for(PropertyDescription propDesc : properties) {
					if(i > 0) {
						sb.append(", ");
					}
					sb.append(propDesc.getDataItem())
					  .append(" = ?");
					
					JdbcSqlParam param = new JdbcSqlParam();
					param.setType(propDesc.getType());
					param.setParamValue(PropertyUtils.getProperty(entity, propDesc.getName()));
					
					params.add(param);
					
					i++;
				}
				
				//处理主键
				sb.append(" where ")
				  .append(idDesc.getDataItem())
				  .append(" = ?");
				
				JdbcSqlParam idParam = new JdbcSqlParam();
				idParam.setType(idDesc.getType());
				idParam.setParamValue(PropertyUtils.getProperty(entity, idDesc.getName()));
				
				params.add(idParam);
			}else{
				int i=0;
				for(Parameter p : this.updateProperties) {
					if(i > 0) {
						sb.append(", ");
					}
					
					PropertyDescription propDesc = entityDesc.getPropertyDescription((String)p.getKey());
					sb.append(propDesc.getDataItem())
					  .append(" = ?");
					
					JdbcSqlParam param = new JdbcSqlParam();
					param.setType(p.getType());
					param.setParamValue(p.getValue());
					
					params.add(param);
					
					i++;
				}
				
				//处理主键
				sb.append(" where ")
				  .append(idDesc.getDataItem())
				  .append(" = ?");
				
				JdbcSqlParam idParam = new JdbcSqlParam();
				idParam.setType(idDesc.getType());
				idParam.setParamValue(this.id);
				
				params.add(idParam);
			}
			
			JdbcSql sql = new JdbcSql();
			sql.setSql(sb.toString());
			sql.setParams(params);
			
			return sql;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		return null;
	}
}
