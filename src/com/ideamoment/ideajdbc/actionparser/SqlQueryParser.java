/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.actionparser;

import com.ideamoment.ideadata.annotation.Entity;
import com.ideamoment.ideajdbc.action.Action;
import com.ideamoment.ideajdbc.action.SqlQueryAction;
import com.ideamoment.ideajdbc.description.JdbcEntityDescription;
import com.ideamoment.ideajdbc.description.JdbcEntityDescriptionFactory;
import com.ideamoment.ideajdbc.description.PartitionDescription;
import com.ideamoment.ideajdbc.exception.IdeaJdbcException;
import com.ideamoment.ideajdbc.exception.IdeaJdbcExceptionCode;

/**
 * 将SQLQuery解析为JdbcQuery对象，此解析器较为简单，由于SQL都已经准备好了，所以主要的工作是将参数做好对应。
 * 
 * @author Chinakite Zhang
 * @version 2010-11-9
 * @since 0.1
 */
public class SqlQueryParser extends AbstractActionParser implements ActionParser {

	/* (non-Javadoc)
	 * @see com.ideamoment.rainbow.actionparser.ActionParser#parse(com.ideamoment.rainbow.action.Action)
	 */
	public JdbcSql parse(Action action) {
		SqlQueryAction sqlQuery = (SqlQueryAction)action;
		Class entityClass = sqlQuery.getEntityClass();
		
		String queryName = sqlQuery.getQueryName();
		String sql = null;
		
		boolean isEntity = entityClass.isAnnotationPresent(Entity.class);
		
		if(entityClass == null || !isEntity) {
			sql = sqlQuery.getSql();
		}else{
			JdbcEntityDescription entityDescription = JdbcEntityDescriptionFactory.getInstance().getEntityDescription(entityClass);
			
			if(queryName != null) {
				sql = entityDescription.getSqlQuery(queryName);
				if(sql == null){
					throw new IdeaJdbcException(IdeaJdbcExceptionCode.NAMEDSQL_NOTFOUND, "Named Query [" + queryName + "] is not defined in entity [" + entityClass.getName() + "].");
				}
			}else{
				sql = sqlQuery.getSql();
				if(sql == null) {
					sql = "select " + entityDescription.allDataItemString() + " from " + entityDescription.getDataSet();
				}
			}
			
			//如果是分区查询
			if(sqlQuery.isPartitionQuery()) {
	            PartitionDescription partDesc = entityDescription.getPartitionDescription();
	            String partCol = partDesc.getDataItem();
	            sql = sql + " where " + partCol + " = :" + partDesc.getProperty();
	            sqlQuery.getParameters().put(partDesc.getProperty(), sqlQuery.getPartitionValue());
	        }
		}
		
		return buildJdbcSql(sql, sqlQuery.getParameters());
	}
}
