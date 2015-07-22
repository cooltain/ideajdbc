/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.description;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ideamoment.ideadata.description.EntityDescription;
import com.ideamoment.ideadata.description.EntityDescriptionCache;
import com.ideamoment.ideadata.description.EntityDescriptionFactory;
import com.ideamoment.ideadata.description.PropertyDescriptionDecoration;
import com.ideamoment.ideajdbc.annotation.Partition;
import com.ideamoment.ideajdbc.annotation.Sql;
import com.ideamoment.ideajdbc.annotation.SqlQuery;
import com.ideamoment.ideajdbc.annotation.SqlUpdate;
import com.ideamoment.ideajdbc.exception.IdeaJdbcException;
import com.ideamoment.ideajdbc.exception.IdeaJdbcExceptionCode;


/**
 * @author Chinakite
 *
 */
public class JdbcEntityDescriptionFactory extends EntityDescriptionFactory {
    
    protected static final JdbcEntityDescriptionFactory factory = new JdbcEntityDescriptionFactory();
    
    private final  Logger logger  =  LoggerFactory.getLogger(JdbcEntityDescriptionFactory.class);
    
    protected JdbcEntityDescriptionFactory() {
    }
    
    public static JdbcEntityDescriptionFactory getInstance() {
        return factory;
    }
    
    public JdbcEntityDescription getEntityDescription(Class clazz) {
        return getEntityDescription(clazz, null);
    }
    
    public JdbcEntityDescription getEntityDescription(Class clazz, List<PropertyDescriptionDecoration> decorations) {
        JdbcEntityDescription result;
        
        result = (JdbcEntityDescription)EntityDescriptionCache.getInstance().get(clazz.getName());
        if(result != null) {        //如果已缓存，直接取缓存的EntityDescription
            return result;
        }else{          //如果还未缓存，则构建对象
            result = new JdbcEntityDescription();
            super.assembleEntityDescription(clazz, result);
            if(clazz.isAnnotationPresent(Partition.class)){   //如果clazz使用@Partition注解
                Partition partitionInfo = (Partition)clazz.getAnnotation(Partition.class);
                PartitionDescription partitionDescription = new PartitionDescription();
                String partitionDataItem = partitionInfo.dataItem();
                if(StringUtils.isEmpty(partitionDataItem)) {
                    String partitionProperty = partitionInfo.property();
                    if(StringUtils.isEmpty(partitionProperty)) {
                        throw new IdeaJdbcException(IdeaJdbcExceptionCode.PARTITION_MAPPING_ERR, "Table partition column mapping error.");
                    }else{
                        partitionDescription.setProperty(partitionProperty);
                        partitionDescription.setDataItem(result.getPropertyDescription(partitionProperty).getDataItem());
                    }
                }else{
                    partitionDescription.setDataItem(partitionDataItem.toUpperCase());
                    partitionDescription.setProperty(result.getPropertyDescriptionByDataItem(partitionDataItem.toUpperCase()).getName());
                }
                result.setPartitionDescription(partitionDescription);
            }
            
            //加载注解的Sql信息
            if(clazz.isAnnotationPresent(Sql.class)) {
                Sql sqlInfo = (Sql)clazz.getAnnotation(Sql.class);
                //注解的查询语句
                SqlQuery[] sqlQueries = sqlInfo.queries();
                for(SqlQuery sqlQuery : sqlQueries) {
                    result.addSqlQuery(sqlQuery.name(), sqlQuery.sql());
                }
                //注解的更新语句
                SqlUpdate[] sqlUpdates = sqlInfo.updates();
                for(SqlUpdate sqlUpdate : sqlUpdates) {
                    result.addSqlUpdate(sqlUpdate.name(), sqlUpdate.sql());
                }
            }
            
            EntityDescriptionCache.getInstance().put(clazz.getName(), result);
            return result;
        }
    }
}
