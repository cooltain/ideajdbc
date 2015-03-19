/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.transaction;

import org.junit.Test;

import com.ideamoment.ideadata.description.User;
import com.ideamoment.ideajdbc.IdeaJdbc;

/**
 * @author Chinakite
 *
 */
public class TestTransaction {
	@Test
	public void testTxRunnable() {
		final String str = "123";
		
		IdeaJdbc.tx(new TxRunnable(){
			@Override
			public void run() {
				System.out.println(str);
				
				IdeaJdbc.query(User.class).list();
				
				System.out.println("Inner tx start.");
				IdeaJdbc.tx(new TxRunnable(){
					@Override
					public void run() {
						IdeaJdbc.query(User.class).list();
					}
				});
				System.out.println("Inner tx end.");
				
				IdeaJdbc.db("ideajdbc2").query("select 1 from dual").uniqueValue();
			}
		});
	}
}
