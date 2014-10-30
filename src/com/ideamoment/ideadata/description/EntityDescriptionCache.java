/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideadata.description;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Chinakite
 *
 */
public class EntityDescriptionCache {
	
	private final  Logger logger  =  LoggerFactory.getLogger(EntityDescriptionCache.class);
	
	private static EntityDescriptionCache instance = new EntityDescriptionCache();
	
	//基于Clazz为key的EntityDescription缓存
	private Map<String, EntityDescription> cacheBasedClazz = new ConcurrentHashMap<String, EntityDescription>();
	
	/**
	 * 根据类名从缓存中取得实体描述。
	 * 
	 * @param className 类名
	 * @return EntityDescription 实体描述信息
	 */
	public EntityDescription get(String className) {
		return cacheBasedClazz.get(className);
	}
	
	/**
	 * 将实体描述信息放入缓存中。
	 * 
	 * @param className 类名
	 * @param EntityDescription 实体描述信息
	 */
	public void put(String className, EntityDescription entityDescription) {
		if(this.cacheBasedClazz.containsKey(className)) {
			logger.debug("EntityDescription {0} is exists, it will be updated.", className);
		}
		this.cacheBasedClazz.put(className, entityDescription);
	}
	
	/**
	 * 私有构造函数，防止生成其他实例
	 */
	private EntityDescriptionCache() {
	}
	
	/**
	 * 获取实体描述的单例缓存对象。
	 * 
	 * @return EntityDescriptorCache 缓存对象
	 */
	public static EntityDescriptionCache getInstance() {
		return instance;
	}
	
	public int size() {
		return cacheBasedClazz.size();
	}
}
