/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.resulthandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ideamoment.ideadata.annotation.DataItemType;
import com.ideamoment.ideadata.description.EntityDescription;
import com.ideamoment.ideadata.description.PropertyDescription;
import com.ideamoment.ideadata.description.RefDescription;
import com.ideamoment.ideadata.util.ReflectUtil;
import com.ideamoment.ideadata.util.TypeUtil;
import com.ideamoment.ideajdbc.action.Query;
import com.ideamoment.ideajdbc.description.JdbcEntityDescription;
import com.ideamoment.ideajdbc.description.JdbcEntityDescriptionFactory;
import com.ideamoment.ideajdbc.exception.IdeaJdbcException;
import com.ideamoment.ideajdbc.exception.IdeaJdbcExceptionCode;
import com.ideamoment.ideajdbc.util.DateUtil;

/**
 * @author Chinakite
 *
 */
public class ResultHandler<T> {
    private static final Logger logger = LoggerFactory.getLogger(ResultHandler.class);
    
	/**
	 * 将结果集转换为以Map为元素的列表
	 * 
	 * @param rs SQL结果集
	 * @return 结果列表
	 */
	public List<Map<String, Object>> handleResultToMap(ResultSet rs) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		try {
			ResultSetMetaData rsMetaData = rs.getMetaData();
			int columnCount = rsMetaData.getColumnCount();
			
			HashSet<String> tableNameCache = new HashSet<String>();
			for(int i=1; i<=columnCount; i++) {
				String tableLabel = rsMetaData.getTableName(i);
				tableNameCache.add(tableLabel);
			}
			
			boolean isMultiTable = false;
			if(tableNameCache.size() > 1) {
				isMultiTable = true;
			}
			
			while(rs.next()) {
				Map<String, Object> record = new HashMap<String, Object>();
				
				for(int i=1; i<=columnCount; i++) {
					String columnLabel = rsMetaData.getColumnLabel(i);
					if(isMultiTable) {
						String tableLabel = rsMetaData.getTableName(i);
						record.put(tableLabel + "."+ columnLabel, rs.getObject(i));
					}else{
						record.put(columnLabel, rs.getObject(i));
					}
				}
				result.add(record);
			}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.RESULT_HANDLE_ERR, "Handle ResultSet error.", e);
		}finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				rs = null;
				throw new IdeaJdbcException(IdeaJdbcExceptionCode.RESULT_HANDLE_ERR, "Handle ResultSet error.", e);
			}
		}
		
		return result;
	}
	
	public List handleResultToNonEntity(ResultSet rs, Class clazz, Query query, boolean isUnique) {
	    try {
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int columnCount = rsMetaData.getColumnCount();
            
            List result = new ArrayList();
            
            int rowIndex = 0;
            while(rs.next()) {
                if(isUnique && rowIndex > 0) {
                    throw new IdeaJdbcException(IdeaJdbcExceptionCode.QUERY_ERR, "More than one row in Unique Query.");
                }
                
                Object entity = clazz.newInstance();
                
                for(int i=1; i<=columnCount; i++) {
                    String columnName = rsMetaData.getColumnLabel(i);
                    String getterName = ReflectUtil.toGetterMethodName(columnName);
                    try {
                        Method getter = clazz.getMethod(getterName);
                        Class returnType = getter.getReturnType();
                        DataItemType dataItemType = TypeUtil.javaTypeToDataItemType(returnType);
                        Object val = retriveResultSetValue(rs, columnName, dataItemType);
                        PropertyUtils.setProperty(entity, columnName, val);
                    }catch(NoSuchMethodException se) {
                        logger.info("No setter method [{}] of [{}]", getterName, clazz.getName());
                    }
                }
                
                result.add(entity);
                
                rowIndex++;
            }
            
            return result;
	    }catch(SQLException e) {
	        e.printStackTrace();
            throw new IdeaJdbcException(e.getMessage());
	    } catch (InstantiationException e) {
            e.printStackTrace();
            throw new IdeaJdbcException(e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IdeaJdbcException(e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new IdeaJdbcException(e.getMessage());
        }
	}
	
	public List handleResultToEntity(ResultSet rs, Class entityClass, Query query, boolean isUnique) {
	    JdbcEntityDescription entityDescription = JdbcEntityDescriptionFactory.getInstance().getEntityDescription(entityClass);
		Map<Class, Map<Object, Object>> entityCache = new HashMap<Class, Map<Object, Object>>();
		try {
			ResultSetMetaData rsMetaData = rs.getMetaData();
			int columnCount = rsMetaData.getColumnCount();
			
			List<SelectColumnInfo> colInfoes = new ArrayList<SelectColumnInfo>();
			List<Class> refEntityClasses = new ArrayList<Class>();
			
			RowMappingInfo rowMapper = new RowMappingInfo();
			
			for(int i=1; i<=columnCount; i++) {
				String columnName = rsMetaData.getColumnLabel(i);
				SelectColumnInfo colInfo = parseColumnInfo(columnName, entityDescription, query);
				//这里处理的是查出了字段但是实体中没有映射的情况，
				//后面要酌情考虑要不要支持，暂时先直接continue
				if(StringUtils.isEmpty(colInfo.getPropName())){
				    continue;
				}
				colInfoes.add(colInfo);
				
				String colTableName = colInfo.getTableName();
				if(!StringUtils.isEmpty(colTableName) 
						&& !colTableName.equals(query.getEntityAlias())) {
					
					String propName = (String)query.getPopulates().get(colTableName);
					if(propName == null) {
						propName = colTableName;
					}
					
					MapperEntity mapperEntity = rowMapper.getRefEntity(propName);
					EntityDescription refEntityDescription = null;
					if(mapperEntity == null) {
						mapperEntity = new MapperEntity();
						
						RefDescription refDescription = entityDescription.getRefDescription(propName);
						Class refEntityClass = refDescription.getEntityClass();
						refEntityDescription = JdbcEntityDescriptionFactory.getInstance().getEntityDescription(refEntityClass);
						mapperEntity.setEntityDescription(refEntityDescription);
						
						rowMapper.addRefEntity(propName, mapperEntity);
					}else{
						refEntityDescription = mapperEntity.getEntityDescription();
					}
					mapperEntity = mapColumnInfo(colInfo, mapperEntity, refEntityDescription);
				}else{
					MapperEntity mapperEntity = rowMapper.getMainEntity();
					if(mapperEntity == null) {
						mapperEntity = new MapperEntity();
						mapperEntity.setEntityDescription(entityDescription);
						rowMapper.setMainEntity(mapperEntity);
					}
					mapperEntity = mapColumnInfo(colInfo, mapperEntity, entityDescription);
				}
			}
			
			List result = new ArrayList();
			
			int rowIndex = 0;
			while(rs.next()) {
				if(isUnique && rowIndex > 0) {
					throw new IdeaJdbcException(IdeaJdbcExceptionCode.QUERY_ERR, "More than one row in Unique Query.");
				}
				
				MapperEntity mainEntity = rowMapper.getMainEntity();
				Object id = getId(rs, mainEntity);
				Object entity = null;
				if(id != null) {
					entity = getEntityFromCache(entityCache, entityClass, id);
				}else{
					throw new IdeaJdbcException(IdeaJdbcExceptionCode.RESULT_HANDLE_ERR, "Can not find Id column of [" + entityClass.getName() + "] on row <" + rowIndex + ">。");
				}
				
				if(entity == null) {    //如果缓存中没有
					entity = entityClass.newInstance();
					String idPropName = entityDescription.getIdDescription().getName();
					PropertyUtils.setProperty(entity, idPropName, id);
					
					List<SelectColumnInfo> mainColInfoes = mainEntity.getColInfoes();
					for(SelectColumnInfo colInfo : mainColInfoes) {
						DataItemType type = entityDescription.getPropertyDescription(colInfo.getPropName()).getType();
						Object value = retriveResultSetValue(rs, colInfo.getFullAliasName(), type);
						PropertyUtils.setProperty(entity, colInfo.getPropName(), value);
					}
					putEntityToCache(entityCache, entityClass, id, entity);
					
					result.add(entity);
				}
				
				Map<String, MapperEntity> refEntities = rowMapper.getRefEntities();
				for(String key : refEntities.keySet()) {	//循环关联属性
					MapperEntity mapperEntity = refEntities.get(key);
					Object refObjId = getId(rs, mapperEntity);		//取关联实体Id
					if(refObjId != null) {
						RefDescription refDesc = entityDescription.getRefDescription(key);
						Object refEntityObj = getEntityFromCache(entityCache, refDesc.getEntityClass(), refObjId);
						if(refEntityObj != null) {    //如果关联实体已在缓存中
							if(refDesc.getPropClass().isAssignableFrom(List.class)) {
								List refList = (List)PropertyUtils.getProperty(entity, key);
								if(refList == null) {
									refList = new ArrayList();
									PropertyUtils.setProperty(entity, key, refList);
								}
								refList.add(refEntityObj);
							}else if(refDesc.getPropClass().isAssignableFrom(Set.class)) {
								Set refSet = (Set)PropertyUtils.getProperty(entity, key);
								if(refSet == null) {
									refSet = new HashSet();
									PropertyUtils.setProperty(entity, key, refSet);
								}
								refSet.add(refEntityObj);
							}else{
								PropertyUtils.setProperty(entity, key, refEntityObj);
							}
						}else{    //如果关联实体没在缓存中
							Class refEntityClass = refDesc.getEntityClass();
							JdbcEntityDescription refEntityDesc = JdbcEntityDescriptionFactory.getInstance().getEntityDescription(refEntityClass);
							
							refEntityObj = refEntityClass.newInstance();
							PropertyUtils.setProperty(refEntityObj, mapperEntity.getEntityDescription().getIdDescription().getName(), refObjId);
							
							List<SelectColumnInfo> refColInfoes = mapperEntity.getColInfoes();
							for(SelectColumnInfo colInfo : refColInfoes) {
								DataItemType type = refEntityDesc.getPropertyDescription(colInfo.getPropName()).getType();
								Object value = retriveResultSetValue(rs, colInfo.getFullAliasName(), type);
								PropertyUtils.setProperty(refEntityObj, colInfo.getPropName(), value);
							}
							putEntityToCache(entityCache, refEntityClass, refObjId, refEntityObj);
							
							if(refDesc.getPropClass().isAssignableFrom(List.class)) {
								List refList = (List)PropertyUtils.getProperty(entity, key);
								if(refList == null) {
									refList = new ArrayList();
									PropertyUtils.setProperty(entity, key, refList);
								}
								refList.add(refEntityObj);
							}else if(refDesc.getPropClass().isAssignableFrom(Set.class)) {
								Set refSet = (Set)PropertyUtils.getProperty(entity, key);
								if(refSet == null) {
									refSet = new HashSet();
									PropertyUtils.setProperty(entity, key, refSet);
								}
								refSet.add(refEntityObj);
							}else{
								PropertyUtils.setProperty(entity, key, refEntityObj);
							}
						}
					}else{
						throw new IdeaJdbcException(IdeaJdbcExceptionCode.RESULT_HANDLE_ERR, "Can not find Id column of [" + entityClass.getName() + "] on row <" + rowIndex + ">。");
					}
				}
				rowIndex++;
			}
			
			return result;
		}catch (SQLException e) {
			e.printStackTrace();
			throw new IdeaJdbcException(e.getMessage());
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new IdeaJdbcException(e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new IdeaJdbcException(e.getMessage());
		} catch (SecurityException e) {
			e.printStackTrace();
			throw new IdeaJdbcException(e.getMessage());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new IdeaJdbcException(e.getMessage());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new IdeaJdbcException(e.getMessage());
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			throw new IdeaJdbcException(e.getMessage());
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				rs = null;
				throw new IdeaJdbcException(IdeaJdbcExceptionCode.RESULT_HANDLE_ERR, "Handle ResultSet error.", e);
			}
		}
	}

	/**
	 * 返回单值结果
	 * 
	 * @param rs
	 * @return
	 */
	public Object handleResultToUniqueValue(ResultSet rs) {
		Object result = null;
		
		try {
			ResultSetMetaData rsMetaData = rs.getMetaData();
			int columnCount = rsMetaData.getColumnCount();
			
			if(columnCount > 1) {
				throw new IdeaJdbcException(IdeaJdbcExceptionCode.QUERY_ERR, "More than one column in result.");
			}
			
			int rowIndex = 0;
			while(rs.next()) {
				if(rowIndex > 0) {
					throw new IdeaJdbcException(IdeaJdbcExceptionCode.QUERY_ERR, "More than one row in Unique Query.");
				}
				
				result = rs.getObject(1);
				rowIndex++;
			}
		}catch (SQLException e) {
			e.printStackTrace();
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.RESULT_HANDLE_ERR, "Handle ResultSet error.", e);
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				rs = null;
				throw new IdeaJdbcException(IdeaJdbcExceptionCode.RESULT_HANDLE_ERR, "Handle ResultSet error.", e);
			}
		}
		
		return result;
	}
	
	/**
	 * @param colInfo
	 * @param mapperEntity
	 * @param refEntityDescription
	 */
	private MapperEntity mapColumnInfo(SelectColumnInfo colInfo,
			MapperEntity mapperEntity, EntityDescription refEntityDescription) {
		String idProp = refEntityDescription.getIdDescription().getName();
		String idDataItem = refEntityDescription.getIdDescription().getDataItem();
		
		if(colInfo.getColumnName().equals(idProp) || colInfo.getColumnName().toUpperCase().equals(idDataItem)) {
			mapperEntity.setIdCol(colInfo.getFullAliasName());
		}else{
			mapperEntity.addColumnInfo(colInfo);
		}
		return mapperEntity;
	}
	
	/**
	 * @param entityCache
	 * @param entityClass
	 * @param id
	 * @return
	 */
	private Object getEntityFromCache(
			Map<Class, Map<Object, Object>> entityCache, Class entityClass,
			Object id) {
		Object entity;
		if(entityCache.get(entityClass) == null) {
			entityCache.put(entityClass, new HashMap<Object, Object>());
		}
		entity = entityCache.get(entityClass).get(id);
		return entity;
	}
	
	private void putEntityToCache(Map<Class, Map<Object, Object>> entityCache, Class entityClass, Object id, Object entity) {
		if(entityCache.get(entityClass) == null) {
			entityCache.put(entityClass, new HashMap<Object, Object>());
		}
		entityCache.get(entityClass).put(id, entity);
	}
	
	private SelectColumnInfo parseColumnInfo(String fullAliasName, Query query) {
	    int dollarPos = fullAliasName.indexOf("$");
        SelectColumnInfo colInfo = new SelectColumnInfo();
        if(dollarPos < 0) {
            colInfo.setColumnName(fullAliasName);
            colInfo.setFullAliasName(fullAliasName);
            colInfo.setPropName(fullAliasName);
        }else if(dollarPos == 0) {
            colInfo.setColumnName(fullAliasName.substring(1));
            colInfo.setFullAliasName(fullAliasName);
            colInfo.setPropName(fullAliasName.substring(1));
        }else{
            String[] arr = fullAliasName.split("\\$");
            colInfo.setColumnName(arr[1]);
            colInfo.setFullAliasName(fullAliasName);
            colInfo.setPropName(arr[1]);
            if(!arr[0].equals(query.getEntityAlias())) {
                colInfo.setTableName(arr[0]);
            }
        }
        
        return colInfo;
	}
	
	private SelectColumnInfo parseColumnInfo(String fullAliasName, EntityDescription entityDescription, Query query) {
		int dollarPos = fullAliasName.indexOf("$");
		SelectColumnInfo colInfo = new SelectColumnInfo();
		if(dollarPos < 0) {
			colInfo.setColumnName(fullAliasName);
			colInfo.setFullAliasName(fullAliasName);
			colInfo = retriveEntityPropName(entityDescription, colInfo);
		}else if(dollarPos == 0) {
			colInfo.setColumnName(fullAliasName.substring(1));
			colInfo.setFullAliasName(fullAliasName);
			colInfo = retriveEntityPropName(entityDescription, colInfo);
		}else{
			String[] arr = fullAliasName.split("\\$");
			colInfo.setColumnName(arr[1]);
			colInfo.setFullAliasName(fullAliasName);
			if(!arr[0].equals(query.getEntityAlias())) {
				colInfo.setTableName(arr[0]);
				
				String propName = (String)query.getPopulates().get(arr[0]);
				if(propName == null) {
					propName = arr[0];
				}
				RefDescription refDesc = entityDescription.getRefDescription(propName);
				Class refEntity = refDesc.getEntityClass();
				JdbcEntityDescription refEntityDescription = JdbcEntityDescriptionFactory.getInstance().getEntityDescription(refEntity);
				
				colInfo = retriveEntityPropName(refEntityDescription, colInfo);
			}else{
				colInfo = retriveEntityPropName(entityDescription, colInfo);
			}
		}
		
		return colInfo;
	}

	/**
	 * @param entityDescription
	 * @param colInfo
	 */
	private SelectColumnInfo retriveEntityPropName(EntityDescription entityDescription,
			SelectColumnInfo colInfo) {
		String col = colInfo.getColumnName();
		PropertyDescription idDesc=entityDescription.getIdDescription();//判断是不是主键
		if(idDesc.getDataItem().equalsIgnoreCase(col))
			colInfo.setPropName(idDesc.getName());
		else{//不是的话就找propertyDescription
			PropertyDescription propDesc = entityDescription.getPropertyDescription(col);
			
			if(propDesc == null) {
				propDesc = entityDescription.getPropertyDescriptionByDataItem(col.toUpperCase());
			}
			if(propDesc != null) {
				colInfo.setPropName(propDesc.getName());
			}
		}
		
		return colInfo;
	}
	
	private Object retriveResultSetValue(ResultSet rs, String aliasName, DataItemType type) throws SQLException {
		Object result = null;
		switch(type) {
			case VARCHAR:
			case VARCHAR2:
			case CHAR:
			case TEXT:
			case CLOB:
			case STRING:
				result = rs.getString(aliasName);
				break;
			case SMALLINT:
			case SHORT:
				result = rs.getShort(aliasName);
				break;
			case INT:
			case INTEGER:
				result = rs.getInt(aliasName);
				break;
			case LONG:
			case BIGINT:
				result = rs.getLong(aliasName);
				break;
			case FLOAT:
				result = rs.getFloat(aliasName);
				break;
			case DOUBLE:
				result = rs.getDouble(aliasName);
				break;
			case DECIMAL:
				result = rs.getBigDecimal(aliasName);
				break;
			case BOOLEAN:
				result = rs.getBoolean(aliasName);
				break;
			case DATE:
				java.sql.Date sqlDate = rs.getDate(aliasName);
				if(sqlDate != null){
					result = DateUtil.toJavaDate(sqlDate);
				}
				break;
			case TIME:
				java.sql.Time sqlTime = rs.getTime(aliasName);
				if(sqlTime != null){
					result = DateUtil.toJavaDate(sqlTime);
				}
				break;
			case DATETIME:
			case TIMESTAMP:
				java.sql.Timestamp timestamp = rs.getTimestamp(aliasName);
				if(timestamp != null){
					result = DateUtil.toJavaDate(timestamp);
				}
				break;
			case BLOB:
			case BYTE:
				break;
			default:
				result = rs.getObject(aliasName);
				break;
		}
		
		return result;
	}
	
	private Object getId(ResultSet rs, MapperEntity mapperEntity) {
		String idCol = mapperEntity.getIdCol();
		try {
			Object id = rs.getObject(idCol);
			return id;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new IdeaJdbcException(IdeaJdbcExceptionCode.RESULT_HANDLE_ERR, "Optain id error.", e);
		}
	}
	
	private class SelectColumnInfo {
		private String tableName;	//表名
		private String columnName;	//字段名
		private String fullAliasName;
		private String propName;
		
		/**
		 * @return the entityPropName
		 */
		public String getPropName() {
			return propName;
		}
		/**
		 * @param entityPropName the entityPropName to set
		 */
		public void setPropName(String propName) {
			this.propName = propName;
		}
		/**
		 * @return the aliasName
		 */
		public String getFullAliasName() {
			return fullAliasName;
		}
		/**
		 * @param aliasName the aliasName to set
		 */
		public void setFullAliasName(String fullAliasName) {
			this.fullAliasName = fullAliasName;
		}
		/**
		 * @return the tableAlias
		 */
		public String getTableName() {
			return tableName;
		}
		/**
		 * @param tableAlias the tableAlias to set
		 */
		public void setTableName(String tableName) {
			this.tableName = tableName;
		}
		/**
		 * @return the columnName
		 */
		public String getColumnName() {
			return columnName;
		}
		/**
		 * @param columnName the columnName to set
		 */
		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}
	}
	
	private class RowMappingInfo {
		private MapperEntity mainEntity;
		private Map<String, MapperEntity> refEntities = new HashMap<String, MapperEntity>();
		/**
		 * @return the mainEntity
		 */
		public MapperEntity getMainEntity() {
			return mainEntity;
		}
		/**
		 * @param mainEntity the mainEntity to set
		 */
		public void setMainEntity(MapperEntity mainEntity) {
			this.mainEntity = mainEntity;
		}
		/**
		 * @return the refEntities
		 */
		public Map<String, MapperEntity> getRefEntities() {
			return refEntities;
		}
		
		public MapperEntity getRefEntity(String tableAlias) {
			return this.refEntities.get(tableAlias);
		}
		
		public void addRefEntity(String tableAlias, MapperEntity mapperEntity) {
			this.refEntities.put(tableAlias, mapperEntity);
		}
	}
	
	private class NonEntityRowMappingInfo {
        private MapperNonEntity mainEntity;
        private Map<String, MapperNonEntity> refEntities = new HashMap<String, MapperNonEntity>();
        /**
         * @return the mainEntity
         */
        public MapperNonEntity getMainEntity() {
            return mainEntity;
        }
        /**
         * @param mainEntity the mainEntity to set
         */
        public void setMainEntity(MapperNonEntity mainEntity) {
            this.mainEntity = mainEntity;
        }
        /**
         * @return the refEntities
         */
        public Map<String, MapperNonEntity> getRefEntities() {
            return refEntities;
        }
        
        public MapperNonEntity getRefEntity(String tableAlias) {
            return this.refEntities.get(tableAlias);
        }
        
        public void addRefEntity(String tableAlias, MapperNonEntity mapperEntity) {
            this.refEntities.put(tableAlias, mapperEntity);
        }
    }
	
	private class MapperEntity {
		private String idCol;
		private EntityDescription entityDescription;
		//列信息，不包括主键列
		private List<SelectColumnInfo> colInfoes = new ArrayList<SelectColumnInfo>();

		/**
		 * @return the idCol
		 */
		public String getIdCol() {
			return idCol;
		}

		/**
		 * @param idCol the idCol to set
		 */
		public void setIdCol(String idCol) {
			this.idCol = idCol;
		}

		/**
		 * @return the entityDescription
		 */
		public EntityDescription getEntityDescription() {
			return entityDescription;
		}

		/**
		 * @param entityDescription the entityDescription to set
		 */
		public void setEntityDescription(EntityDescription entityDescription) {
			this.entityDescription = entityDescription;
		}

		/**
		 * @return the colInfoes
		 */
		public List<SelectColumnInfo> getColInfoes() {
			return colInfoes;
		}
		
		public void addColumnInfo(SelectColumnInfo colInfo) {
			this.colInfoes.add(colInfo);
		}
	}
	
	private class MapperNonEntity {
        private Class clazz;
        //列信息，不包括主键列
        private List<SelectColumnInfo> colInfoes = new ArrayList<SelectColumnInfo>();

        /**
         * @return the entityDescription
         */
        public Class getClazz() {
            return clazz;
        }

        /**
         * @param entityDescription the entityDescription to set
         */
        public void setClazz(Class clazz) {
            this.clazz = clazz;
        }

        /**
         * @return the colInfoes
         */
        public List<SelectColumnInfo> getColInfoes() {
            return colInfoes;
        }
        
        public void addColumnInfo(SelectColumnInfo colInfo) {
            this.colInfoes.add(colInfo);
        }
    }
}
