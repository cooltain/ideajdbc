/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideajdbc.spring;

import static org.junit.Assert.assertEquals;

import org.springframework.stereotype.Service;

import com.ideamoment.ideadata.description.User;
import com.ideamoment.ideajdbc.IdeaJdbc;
import com.ideamoment.ideajdbc.bean.BeanForUpdate;
import com.ideamoment.ideajdbc.server.Db;
import com.ideamoment.ideajdbc.transaction.Transaction;


/**
 * @author Chinakite Zhang
 * @version 2012-8-5
 * @since 0.1
 */
@Service
public class TestDao {
//	@IdeaJdbcTx(readOnly=true)
	public Transaction testQuery() {
		Db db = IdeaJdbc.db("mysql");
		User user = db.find(User.class, "1");
		assertEquals("1", user.getId());
		return db.getCurrentTransaction();
	}

	@IdeaJdbcTx
	public void testRollback() {
		Db db = IdeaJdbc.db("mysql");
		
		BeanForUpdate bfu = new BeanForUpdate();
		bfu.setName("iPad");
		bfu.setPrice(8.77f);
		db.save(bfu);
		
		throw new RuntimeException("Just for test.");
	}
	
	@IdeaJdbcTx
	public void testMultiOperation() {
		BeanForUpdate bfu = IdeaJdbc.find(BeanForUpdate.class, "1");
		bfu.setName("IdeaMoment");
		IdeaJdbc.update(bfu);
		
		throw new RuntimeException("Just for test.");
	}
}
