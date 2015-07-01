/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideadata.description;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ideamoment.ideadata.annotation.DataItemType;
import com.ideamoment.ideadata.annotation.Entity;
import com.ideamoment.ideadata.annotation.Id;
import com.ideamoment.ideadata.annotation.Property;
import com.ideamoment.ideadata.annotation.Ref;
import com.ideamoment.ideadata.annotation.Transient;
import com.ideamoment.ideadata.annotation.UNSPEC_CLASS;
import com.ideamoment.ideadata.exception.IdeaDataException;
import com.ideamoment.ideadata.util.ReflectUtil;
import com.ideamoment.ideadata.util.TypeUtil;
import com.ideamoment.ideajdbc.annotation.Sql;
import com.ideamoment.ideajdbc.annotation.SqlQuery;
import com.ideamoment.ideajdbc.annotation.SqlUpdate;

/**
 * @author Chinakite
 *
 */
public class EntityDescriptionFactory {
	
	private static final EntityDescriptionFactory factory = new EntityDescriptionFactory();
	
	private List<PropertyDescriptionDecoration> propertyDecorations = new ArrayList<PropertyDescriptionDecoration>();
	
	private EntityDescriptionFactory(){
	}
	
	private final  Logger logger  =  LoggerFactory.getLogger(EntityDescriptionCache.class);
	
	public void setupPropertyDecoration(PropertyDescriptionDecoration decoration) {
		propertyDecorations.add(decoration);
	}
	
	public static EntityDescriptionFactory getInstance() {
		return factory;
	}
	
	public EntityDescription getEntityDescription(Class clazz, List<PropertyDescriptionDecoration> decorations) {
		EntityDescription result;
		
		result = EntityDescriptionCache.getInstance().get(clazz.getName());
		if(result != null) {		//如果已缓存，直接取缓存的EntityDescription
			return result;
		}else{			//如果还未缓存，则构建对象
			if(!clazz.isAnnotationPresent(Entity.class)){	//如果clazz没有使用@Entity注解
				throw new IdeaDataException("The class " + clazz.getName() + " is not annotated by @Entity.");
			}else{
				EntityDescription entityDescription = new EntityDescription();
				//获取EntityDescriptor的基本信息
				entityDescription.setEntityClazz(clazz);
				
				Entity entityInfo = (Entity)clazz.getAnnotation(Entity.class);
				String dataSet = entityInfo.dataSet();
				
				if(StringUtils.isEmpty(dataSet)) {	//如果dataSet没有配置，使用类名首字每小写做为数据集名称
					entityDescription.setDataSet(clazz.getSimpleName());
				}else{
					entityDescription.setDataSet(dataSet);
				}
				
				//加载注解的Sql信息
				if(clazz.isAnnotationPresent(Sql.class)) {
					Sql sqlInfo = (Sql)clazz.getAnnotation(Sql.class);
					//注解的查询语句
					SqlQuery[] sqlQueries = sqlInfo.queries();
					for(SqlQuery sqlQuery : sqlQueries) {
						entityDescription.addSqlQuery(sqlQuery.name(), sqlQuery.sql());
					}
					//注解的更新语句
					SqlUpdate[] sqlUpdates = sqlInfo.updates();
					for(SqlUpdate sqlUpdate : sqlUpdates) {
						entityDescription.addSqlUpdate(sqlUpdate.name(), sqlUpdate.sql());
					}
				}
				
				//获取PropertyDescriptor列表信息	
				List<Field> fields = ReflectUtil.getAllFieldsList(clazz);
				assemblePropertyDescriptor(clazz, entityDescription, fields);
				
				EntityDescriptionCache.getInstance().put(clazz.getName(), entityDescription);
				return entityDescription;
			}
		}
	}
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public EntityDescription getEntityDescription(Class clazz) {
		return getEntityDescription(clazz, null);
	}
	
	/**
	 * 组装propertyDescriptor.
	 * 
	 * @param clazz
	 * @param entityDescriptor
	 * @param fields
	 */
	private void assemblePropertyDescriptor(
							Class clazz,
							EntityDescription entityDescription, 
							List<Field> fields) {
		for(Field field : fields) {
			if(!field.isAnnotationPresent(Transient.class)){	//字段没有使用@Transient注解
				if(field.isAnnotationPresent(Id.class)) {		//使用了@Id注解
					IdDescription idDescription = new IdDescription();
					
					idDescription.setName(field.getName());
					
					Id idInfo = field.getAnnotation(Id.class);
					idDescription = readIdDesc(field, idDescription, idInfo);
					
					String generator = idInfo.generator();
					idDescription.setGenerator(generator);
					
					idDescription.setType(determineDataItemType(field));
					
					if(propertyDecorations != null && propertyDecorations.size() > 0) {
						for(PropertyDescriptionDecoration decoration : propertyDecorations) {
							idDescription = decoration.decorate(idDescription);
						}
					}
					
					entityDescription.setIdDescription(idDescription);
				}else if(field.isAnnotationPresent(Property.class)) {
					PropertyDescription propertyDescription = new PropertyDescription();
					Property propInfo = field.getAnnotation(Property.class);
					readPropertyDesc(field, propertyDescription, propInfo);
					
					propertyDescription.setName(field.getName());
					propertyDescription.setType(determineDataItemType(field));
					propertyDescription.setNullable(propInfo.nullable());
					propertyDescription.setDefaultValue(propInfo.defaultValue());
					
					if(propertyDecorations != null && propertyDecorations.size() > 0) {
						for(PropertyDescriptionDecoration decoration : propertyDecorations) {
							propertyDescription = decoration.decorate(propertyDescription);
						}
					}
					
					entityDescription.addPropertyDescription(propertyDescription);
				}else if(field.isAnnotationPresent(Ref.class)) {
					RefDescription refDescription = new RefDescription();
					refDescription.setName(field.getName());
					
					Ref refInfo = field.getAnnotation(Ref.class);
					if(refInfo.entityClass().equals(UNSPEC_CLASS.class)) {
						refDescription.setEntityClass(field.getType());
					}else{
						refDescription.setEntityClass(refInfo.entityClass());
					}
					
					refDescription.setPropClass(field.getType());
					
					entityDescription.addRefDescription(refDescription);
				}else{
					PropertyDescription propertyDescription = new PropertyDescription();
					propertyDescription.setDataItem(field.getName());
					propertyDescription.setLength(-1);
					propertyDescription.setType(determineDataItemType(field));
					
					propertyDescription.setName(field.getName());
					
					if(propertyDecorations != null && propertyDecorations.size() > 0) {
						for(PropertyDescriptionDecoration decoration : propertyDecorations) {
							propertyDescription = decoration.decorate(propertyDescription);
						}
					}
					
					entityDescription.addPropertyDescription(propertyDescription);
				}
			}
		}
	}

	/**
	 * 决定数据项类型
	 * 
	 * @param field
	 * @return
	 */
	private DataItemType determineDataItemType(Field field) {
		Class clazz = field.getType();
		return TypeUtil.javaTypeToDataItemType(clazz);
	}

	/**
	 * @param field
	 * @param propertyDescription
	 * @param propInfo
	 */
	private PropertyDescription readPropertyDesc(Field field,
			PropertyDescription propertyDescription, Property propInfo) {
		String dataItem = propInfo.dataItem();
		if(StringUtils.isEmpty(dataItem)) {
			propertyDescription.setDataItem(field.getName());
		}else{
			propertyDescription.setDataItem(dataItem);
		}
		
		int length = propInfo.length();
		if(length > -1) {
			propertyDescription.setLength(length);
		}
		
		DataItemType type = propInfo.type();
		propertyDescription.setType(type);
		
		if(propertyDecorations != null && propertyDecorations.size() > 0) {
			for(PropertyDescriptionDecoration decoration : propertyDecorations) {
				propertyDescription = decoration.decorate(propertyDescription);
			}
		}
		
		return propertyDescription;
	}
	
	/**
	 * @param field
	 * @param idDescription
	 * @param idInfo
	 */
	private IdDescription readIdDesc(Field field,
			IdDescription idDescription, Id idInfo) {
		String dataItem = idInfo.dataItem();
		if(StringUtils.isEmpty(dataItem)) {
			idDescription.setDataItem(field.getName());
		}else{
			idDescription.setDataItem(dataItem);
		}
		
		int length = idInfo.length();
		if(length > -1) {
			idDescription.setLength(length);
		}
		
		DataItemType type = idInfo.type();
		idDescription.setType(type);
		
		if(propertyDecorations != null && propertyDecorations.size() > 0) {
			for(PropertyDescriptionDecoration decoration : propertyDecorations) {
				idDescription = decoration.decorate(idDescription);
			}
		}
		
		return idDescription;
	}
}
