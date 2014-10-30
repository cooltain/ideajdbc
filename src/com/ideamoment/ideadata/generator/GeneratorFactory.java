/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideadata.generator;

import java.util.HashMap;

import com.ideamoment.ideajdbc.exception.IdeaJdbcException;
import com.ideamoment.ideajdbc.exception.IdeaJdbcExceptionCode;

/**
 * 生成器工厂。
 * 使用单例模式实现。
 * 
 * @author Chinakite Zhang
 * @version 2011-2-3
 * @since 0.1
 */
public class GeneratorFactory {
	
	private static GeneratorFactory factory = new GeneratorFactory();
	
	//生成器缓存
	private HashMap<String, ValueGenerator> generators = new HashMap<String, ValueGenerator>();
	
	private GeneratorFactory() {
		generators.put("uuid", new UuidValueGenerator());
	}
	
	/**
	 * 获取生成器工厂实例
	 * 
	 * @return 生成器工厂
	 */
	public static GeneratorFactory getInstance() {
		return factory;
	}
	
	/**
	 * 根据生成器的名称获取对应的生成器。
	 * 
	 * @param name 生成器名称
	 * @return 生成器
	 */
	public ValueGenerator getGenerator(String name) {
		ValueGenerator generator = generators.get(name);
		if(generator != null) {
			return generator;
		}else{
			try {
				generator = (ValueGenerator)Class.forName(name).newInstance();
				generators.put(name, generator);
				return generator;
			} catch (InstantiationException e) {
				e.printStackTrace();
				throw new IdeaJdbcException(IdeaJdbcExceptionCode.GENERATOR_ERR, "Error occured when get value generator by factory. The generator's name is " + name, e);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				throw new IdeaJdbcException(IdeaJdbcExceptionCode.GENERATOR_ERR, "Error occured when get value generator by factory. The generator's name is " + name, e);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				throw new IdeaJdbcException(IdeaJdbcExceptionCode.GENERATOR_ERR, "Error occured when get value generator by factory. The generator's name is " + name, e);
			} catch (ClassCastException e) {
				e.printStackTrace();
				throw new IdeaJdbcException(IdeaJdbcExceptionCode.GENERATOR_ERR, "The generator [" + name + "] is not a ValueGenerator implemention class. ", e);
			}
		}
	}
}
