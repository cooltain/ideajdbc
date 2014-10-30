/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.actionparser;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.ideamoment.ideadata.annotation.DataItemType;
import com.ideamoment.ideadata.description.EntityDescription;
import com.ideamoment.ideadata.description.EntityDescriptionFactory;
import com.ideamoment.ideajdbc.action.Action;
import com.ideamoment.ideajdbc.action.Parameter;
import com.ideamoment.ideajdbc.action.SqlQueryAction;
import com.ideamoment.ideajdbc.configuration.IdeaJdbcConfiguration;
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
		
		if(entityClass == null) {
			sql = sqlQuery.getSql();
		}else{
			EntityDescription entityDescription = EntityDescriptionFactory.getInstance().getEntityDescription(entityClass);
			if(queryName != null) {
				sql = entityDescription.getSqlQuery(queryName);
			}else{
				sql = sqlQuery.getSql();
			}
			
			if(sql == null){
				throw new IdeaJdbcException(IdeaJdbcExceptionCode.NAMEDSQL_NOTFOUND, "Named Query [" + queryName + "] is not defined in entity [" + entityClass.getName() + "].");
			}
		}
		
		return buildJdbcSql(sql, sqlQuery.getParameters());
	}
}
