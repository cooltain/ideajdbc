package com.ideamoment.ideajdbc;


import static org.junit.Assert.assertEquals;

import java.util.Set;

import com.ideamoment.ideadata.description.Order;
import com.ideamoment.ideadata.description.TestGen;
import com.ideamoment.ideadata.description.User;
import com.ideamoment.ideajdbc.bean.BeanForUpdate;
import com.ideamoment.ideajdbc.tool.mysql.entity2ddl.MySqlEntity2Ddl;
import org.junit.Test;
public class TestUtil {

	@Test
	public void getEntityClasses(){
		MySqlEntity2Ddl ddl=new MySqlEntity2Ddl();
		Set<Class<?>> classes=ddl.getAllEntityClasses("com.**.*data.description");
		assertEquals(classes.contains(Order.class),true);
		assertEquals(classes.contains(TestGen.class),true);
		assertEquals(classes.contains(User.class),true);
		assertEquals(classes.size(), 3);
		
		classes=ddl.getAllEntityClasses("com.**.bean");
		assertEquals(classes.contains(BeanForUpdate.class),true);
		assertEquals(classes.size(), 1);
		
		classes=ddl.getAllEntityClasses("com.**.bean");
		assertEquals(classes.contains(BeanForUpdate.class),true);
		assertEquals(classes.size(), 1);
		
	}
	
}
