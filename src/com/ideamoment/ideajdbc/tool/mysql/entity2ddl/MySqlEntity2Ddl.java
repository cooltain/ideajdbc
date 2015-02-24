/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.tool.mysql.entity2ddl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ideamoment.ideadata.annotation.DataItemType;
import com.ideamoment.ideadata.description.EntityDescription;
import com.ideamoment.ideadata.description.EntityDescriptionFactory;
import com.ideamoment.ideadata.description.IdDescription;
import com.ideamoment.ideadata.description.PropertyDescription;
import com.ideamoment.ideadata.description.TestGen;
import com.ideamoment.ideajdbc.IdeaJdbc;
import com.ideamoment.ideajdbc.tool.mysql.MySqlToolMapper;

/**
 * 根据Entity配置生成DDL语句
 * 
 * @author Chinakite
 *
 */
public class MySqlEntity2Ddl {
	
	private static Logger logger = LoggerFactory.getLogger(MySqlEntity2Ddl.class);
	
	private static String ENGINE = "InnoDB";
	private static String CHARSET = "utf8";
	private static String COLLATE = "utf8_unicode_ci";
	
	public void syncTable(String dbName, String catalog, String schemaPattern, Class entityClass, boolean dropWhenExist) {
		EntityDescription entityDesc = EntityDescriptionFactory.getInstance().getEntityDescription(entityClass);
		Connection conn = IdeaJdbc.db(dbName).beginTransaction().getConnection();
		
		try {
			DatabaseMetaData dbMeta = conn.getMetaData();
			ResultSet tableRs = dbMeta.getTables(catalog, schemaPattern, entityDesc.getDataSet(), new String[]{"TABLE"});
			
			if(tableRs.next()) {
				if(dropWhenExist) {		//删除重建
					logger.info("Table [{}] is exists, it will be droped.", new Object[]{entityDesc.getDataSet()});
					dropTable(entityDesc, conn);
					logger.info("Table [{}] is droped.", new Object[]{entityDesc.getDataSet()});
					createTable(entityDesc, conn);
					logger.info("Table [{}] is created.", new Object[]{entityDesc.getDataSet()});
				}else{		//修改表，目前只支持增加列，不支持修改和删除列，因为有许多不可控的数据因素要处理，TODO.
					logger.info("Table [{}] is exists, it will be altered.", new Object[]{entityDesc.getDataSet()});
					alterTable(entityDesc, conn, dbMeta, catalog, schemaPattern);
					logger.info("Table [{}] is altered.", new Object[]{entityDesc.getDataSet()});
				}
			}else{
				logger.info("Table [{}] is not exists, it will be created.", new Object[]{entityDesc.getDataSet()});
				createTable(entityDesc, conn);
				logger.info("Table [{}] is created.", new Object[]{entityDesc.getDataSet()});
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			IdeaJdbc.db(dbName).endTransaction();
			conn = null;
		}
		
	}
	
	private void alterTable(EntityDescription entityDesc, Connection conn, DatabaseMetaData dbMeta, String catalog, String schemaPattern) throws SQLException {
		HashSet<String> existsColumns = getAllExistsColumns(entityDesc, conn, dbMeta, catalog, schemaPattern);
		
		List<PropertyDescription> addPropertyDescriptions = new ArrayList<PropertyDescription>();
		boolean isAddId = false;
		if(entityDesc.hasId() || entityDesc.hasProperty()) {
			IdDescription idDescription = entityDesc.getIdDescription();
			
			if(!(existsColumns.contains(idDescription.getDataItem().toUpperCase()))) {
				addPropertyDescriptions.add(idDescription);
				isAddId = true;
			}
			
			List<PropertyDescription> propertyDescriptions = entityDesc.getProperties();
			for(PropertyDescription propDesc : propertyDescriptions) {
				if(!(existsColumns.contains(propDesc.getDataItem().toUpperCase()))) {
					addPropertyDescriptions.add(propDesc);
				}
			}
			
			if(addPropertyDescriptions.size() > 0) {
				StringBuffer sb = new StringBuffer();
				sb.append("ALTER TABLE " + entityDesc.getDataSet());
				int i=0;
				for(PropertyDescription propDesc : addPropertyDescriptions) {
					if(i > 0) {
						sb.append(",");
					}
					sb.append(" ADD COLUMN ");
					if(propDesc instanceof IdDescription) {
						sb.append(generateIdColumnSql((IdDescription)propDesc));
					}else{
						sb.append(generateColumnSql(propDesc));
					}
					i++;
				}
				sb.append(";");
				conn.createStatement().execute(sb.toString());
				
				if(isAddId) {
					conn.createStatement().execute(generateAddPKSql(entityDesc));
				}
			}
		}
	}
	
	private void createTable(EntityDescription entityDesc, Connection conn) throws SQLException{
		String sql = generateCreateTableSql(entityDesc);
		conn.createStatement().execute(sql);
	}
	
	private void dropTable(EntityDescription entityDesc, Connection conn) throws SQLException{
		String sql = generateDropTableSql(entityDesc);
		conn.createStatement().execute(sql);
	}
	
	private HashSet<String> getAllExistsColumns(EntityDescription entityDesc, Connection conn, DatabaseMetaData dbMeta, String catalog, String schemaPattern) throws SQLException {
		HashSet<String> existsColumns = new HashSet<String>();
		
		ResultSet rs = dbMeta.getColumns(catalog, schemaPattern, entityDesc.getDataSet(), "%");
		while(rs.next()) {
			String columnName = rs.getString("COLUMN_NAME");
			existsColumns.add(columnName.toUpperCase());
		}
		
		return existsColumns;
	}
	
	private String generateDropTableSql(EntityDescription entityDescription) {
		StringBuffer sb = new StringBuffer();
		sb.append("DROP TABLE ").append(entityDescription.getDataSet()).append(";");
		return sb.toString();
	} 
	
	private String generateCreateTableSql(EntityDescription entityDescription) {
		assert entityDescription  != null;
		
		if(entityDescription.hasId() || entityDescription.hasProperty()) {
			StringBuffer sb = new StringBuffer();
			sb.append("CREATE TABLE ")
			  .append(entityDescription.getDataSet())
			  .append(" (");
			
			IdDescription idDescription = entityDescription.getIdDescription();
			List<PropertyDescription> propertyDescriptions = entityDescription.getProperties();
			
			int i=0;
			if(entityDescription.hasId()) {
				sb.append(generateIdColumnSql(idDescription));
				i++;
			}
			
			if(entityDescription.hasProperty()) {
				for(PropertyDescription propertyDesc : propertyDescriptions) {
					if(i > 0) {
						sb.append(",");
					}
					sb.append(generateColumnSql(propertyDesc));
					i++;
				}
			}
			
			if(entityDescription.hasId()) {
				if(i > 0) {
					sb.append(",");
				}
				sb.append(generatePKSql(idDescription));
			}
			
			sb.append(" )");
			sb.append(" ENGINE=").append(ENGINE)
			  .append(" DEFAULT CHARSET=").append(CHARSET)
			  .append(" COLLATE=").append(COLLATE)
			  .append(";");
			
			return sb.toString();
		}
		
		return null;
	}
	
	private String generateAddPKSql(EntityDescription entityDesc) {
		StringBuffer sb = new StringBuffer();
		sb.append("ALTER TABLE ").append(entityDesc.getDataSet()).append(" ADD PRIMARY KEY (").append(entityDesc.getIdDescription().getDataItem()).append(");");
		return sb.toString();
	}
	
	private String generateIdColumnSql(IdDescription idDesc) {
		StringBuffer sb = new StringBuffer();
		
		String dataItem = idDesc.getDataItem();
		DataItemType dataItemType = idDesc.getType();
		
		int length = idDesc.getLength();
		if(length == -1) {
			length = MySqlToolMapper.lengthMapper.get(dataItemType);
		}
		
		sb.append(" ").append(dataItem)
		  .append(" ").append(MySqlToolMapper.typeMapper.get(dataItemType));
		
		if(length != -1) {
			sb.append("(").append(length).append(")");
		}
		
		sb.append(" NOT NULL");
		
		return sb.toString();
	}
	
	private String generatePKSql(IdDescription idDesc) {
		StringBuffer sb = new StringBuffer();
		sb.append("PRIMARY KEY (").append(idDesc.getDataItem()).append(")");
		return sb.toString();
	}
	
	private String generateColumnSql(PropertyDescription propertyDesc) {
		StringBuffer sb = new StringBuffer();
		
		String dataItem = propertyDesc.getDataItem();
		DataItemType dataItemType = propertyDesc.getType();
		
		int length = propertyDesc.getLength();
		
		if(length == -1) {
			length = MySqlToolMapper.lengthMapper.get(dataItemType);
		}
		
		sb.append(" ").append(dataItem)
		  .append(" ").append(MySqlToolMapper.typeMapper.get(dataItemType));
		
		if(length != -1) {
			sb.append("(").append(length).append(")");
		}
		
		if(!propertyDesc.isNullable()) {
			sb.append(" NOT NULL");
		}
		
		String defaultValue = propertyDesc.getDefaultValue();
		if(defaultValue != null && defaultValue.trim().length() > 0) {
			if(dataItemType == DataItemType.BIGINT 
					|| dataItemType == DataItemType.DECIMAL
					|| dataItemType == DataItemType.DOUBLE
					|| dataItemType == DataItemType.FLOAT
					|| dataItemType == DataItemType.INT
					|| dataItemType == DataItemType.INTEGER
					|| dataItemType == DataItemType.LONG
					|| dataItemType == DataItemType.NUMERIC
					|| dataItemType == DataItemType.SHORT
					|| dataItemType == DataItemType.SMALLINT
					) {
				sb.append(" DEFAULT ").append(defaultValue);
			}else if(dataItemType == DataItemType.BOOLEAN) {
				sb.append(" DEFAULT ").append(defaultValue);
			}else{
				sb.append(" DEFAULT ").append("'").append(defaultValue).append("'");
			}
		}else{
			if(propertyDesc.isNullable()) {
				sb.append(" DEFAULT NULL");
			}
		}
		
		return sb.toString();
	}
	
	
	public static void main(String[] args) {
		MySqlEntity2Ddl m = new MySqlEntity2Ddl();
		m.syncTable("mysql", "ideajdbc", null, TestGen.class, false);
	}
}
