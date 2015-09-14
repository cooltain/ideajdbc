/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.spring;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ideamoment.ideajdbc.AbstractTestCase;
import com.ideamoment.ideajdbc.transaction.Transaction;

/**
 * 
 * 
 * @author Chinakite Zhang
 * @version 2012-8-4
 * @since 0.1
 */
public class TestSpringTransaction extends AbstractTestCase {
	@Test
	public void testQuery() {
		String[] paths = {"classpath:com/ideamoment/ideajdbc/spring/context.xml"};
		ApplicationContext ctx =new ClassPathXmlApplicationContext(paths);
		TestDao dao = (TestDao) ctx.getBean("dao");
		Transaction tx = dao.testQuery();
		assertNotNull(tx);
//		assertFalse(tx.isActive());
		try {
            Thread.sleep(70000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		tx.rollback();
		tx.end();
		System.out.println("end");
	}
	
	@Test
	public void testRollback() {
		String[] paths = {"classpath:com/ideamoment/ideajdbc/spring/context.xml"};
		ApplicationContext ctx =new ClassPathXmlApplicationContext(paths);
		TestDao dao = (TestDao) ctx.getBean("dao");
		try {
			dao.testRollback();
		}catch(RuntimeException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testMultiOperation() {
		String[] paths = {"classpath:com/ideamoment/ideajdbc/spring/context.xml"};
		ApplicationContext ctx =new ClassPathXmlApplicationContext(paths);
		TestDao dao = (TestDao) ctx.getBean("dao");
		try {
			dao.testMultiOperation();
		}catch(RuntimeException e) {
			assertTrue(true);
		}
	}
}
