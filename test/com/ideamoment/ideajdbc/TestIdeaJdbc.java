/**
 * 
 */
package com.ideamoment.ideajdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.ideamoment.ideadata.annotation.DataItemType;
import com.ideamoment.ideadata.description.Order;
import com.ideamoment.ideadata.description.TempUser;
import com.ideamoment.ideadata.description.User;
import com.ideamoment.ideajdbc.action.Page;
import com.ideamoment.ideajdbc.bean.BeanForUpdate;
import com.ideamoment.ideajdbc.configuration.IdeaJdbcConfiguration;
import com.ideamoment.ideajdbc.server.Db;
import com.ideamoment.ideajdbc.transaction.Transaction;

/**
 * @author Chinakite
 *
 */
public class TestIdeaJdbc {
	
	@Before
	public void initIdeaJdbcConfig() {
		IdeaJdbcConfiguration.initConfig("ideajdbc.properties");
	}

	public void test(){
		//--IdeaJdbc.query("select * from t_user").listTo(User.class);
		//--IdeaJdbc.find(User.class, "1");
		//--IdeaJdbc.query("select * from t_user u where u.name = :name").setParameter("name", "zzh").list();
		//--IdeaJdbc.query(User.class, "all").list();
		//--IdeaJdbc.db("coredb").query("select * from t_user u where u.name = :name").setParameter("name", "zzh").list();
		//--IdeaJdbc.save(user);
		//--IdeaJdbc.delete(user);
		//--IdeaJdbc.delete(User.class, "1");
		//--IdeaJdbc.sql("update t_user set c_name = 'zzh1' where c_id = :id").setParameter("id", "1").execute();
		//--IdeaJdbc.update(User.class, "1").setProperty("name", "zzh1").execute();
		
		//--
		//IdeaJdbc.query("select u.C_ID u$id, u.NAME, o.C_ID o$id, o.C_CODE o$C_CODE from t_user u, t_order o where u.C_ID = o.C_USERID")
		//        .populate("orders", "o") 
		//        .listTo(User.class, "u");
		
		//--
		//IdeaJdbc.query("select u.C_ID u$id, u.NAME, o.C_ID o$id, o.C_CODE o$C_CODE from t_user u, t_order o where u.C_ID = o.C_USERID")
		//        .populate("user", "u") 
		//        .listTo(Order.class, "o");
	}
	
	@Test
	public void testDb() {
		Db db = IdeaJdbc.db("mysql");
		assertNotNull(db);
		assertEquals("mysql", db.getName());
		assertEquals("mysql", db.getDbConfig().getName());
		assertNotNull(db.getDbConfig().getDataSource());
	}
	
	@Test
	public void testDefaultDb() {
		Db db = IdeaJdbc.defaultDb();
		assertNotNull(db);
		assertEquals("mysql", db.getName());
		assertEquals("mysql", db.getDbConfig().getName());
		assertNotNull(db.getDbConfig().getDataSource());
	}
	
	@Test
	public void testBeginAndEndTransaction() {
		Transaction tx = IdeaJdbc.beginTransaction();
		assertNotNull(tx);
		Connection conn = tx.getConnection();
		try {
			Statement stmt = conn.createStatement();
			stmt.executeQuery("select 1");
			stmt.close();
		} catch (SQLException e) {
			assertEquals(true, false);
		}
		IdeaJdbc.endTransaction();
		try {
			Statement stmt1 = conn.createStatement();
		} catch (SQLException e) {
			assertEquals(true, true);
		}
		
		Transaction tx1 = IdeaJdbc.beginTransaction();
		
		
		IdeaJdbc.endTransaction();
	}
	
	@Test
	public void testListToMap() {
		IdeaJdbc.beginTransaction();
		
		String sql = "SELECT CONSTRAINT_NAME, table_name, column_name, referenced_table_name, referenced_column_name FROM information_schema.key_column_usage WHERE table_schema = 'aa101' AND referenced_table_name IS NOT NULL ";
		
//		List<Map<String, Object>> result = IdeaJdbc.query("SELECT * FROM T_USER").list();
//		assertEquals(2, result.size());
//		assertEquals("1", result.get(0).get("C_ID"));
//		assertEquals("Chinakite", result.get(0).get("NAME"));
		
		List<Map<String, Object>> result = IdeaJdbc.query(sql).list();
//        assertEquals(2, result.size());
//        assertEquals("1", result.get(0).get("C_ID"));
//        assertEquals("Chinakite", result.get(0).get("NAME"));
		
		IdeaJdbc.endTransaction();
	}
	
	@Test
	public void testListTo() {
		IdeaJdbc.beginTransaction();
		
		List<User> users = IdeaJdbc.query("select u.C_ID u$id, u.NAME, o.C_ID o$id, o.C_CODE o$C_CODE from t_user u, t_order o where u.C_ID = o.C_USERID")
								   .populate("orders", "o") 
								   .listTo(User.class, "u");
		
		assertEquals(2, users.size());
		assertEquals("1", users.get(0).getId());
		assertEquals("Chinakite", users.get(0).getName());
		assertEquals("2", users.get(1).getId());
		assertEquals("张中华", users.get(1).getName());
		
		assertEquals(2, users.get(0).getOrders().size());
		assertEquals("1", ((Order)users.get(0).getOrders().get(0)).getId());
		assertEquals("O_0001", ((Order)users.get(0).getOrders().get(0)).getCode());
		assertEquals("2", ((Order)users.get(0).getOrders().get(1)).getId());
		assertEquals("O_0002", ((Order)users.get(0).getOrders().get(1)).getCode());
		
		assertEquals(1, users.get(1).getOrders().size());
		assertEquals("3", ((Order)users.get(1).getOrders().get(0)).getId());
		assertEquals("O_0003", ((Order)users.get(1).getOrders().get(0)).getCode());
		
		List<Order> orders = IdeaJdbc.query("select u.C_ID u$id, u.NAME u$name, o.C_ID, o.C_CODE from t_user u, t_order o where u.C_ID = o.C_USERID")
									 .populate("user", "u")
									 .listTo(Order.class, "o");
		
		assertEquals(3, orders.size());
		
		assertEquals("1", orders.get(0).getId());
		assertEquals("O_0001", orders.get(0).getCode());
		assertEquals("2", orders.get(1).getId());
		assertEquals("O_0002", orders.get(1).getCode());
		assertEquals("3", orders.get(2).getId());
		assertEquals("O_0003", orders.get(2).getCode());
		
		assertEquals("1", orders.get(0).getUser().getId());
		assertEquals("Chinakite", orders.get(0).getUser().getName());
		assertEquals("1", orders.get(1).getUser().getId());
		assertEquals("Chinakite", orders.get(1).getUser().getName());
		assertEquals("2", orders.get(2).getUser().getId());
		assertEquals("张中华", orders.get(2).getUser().getName());
		
		assertEquals(orders.get(0).getUser(), orders.get(1).getUser());
		
		IdeaJdbc.endTransaction();
	}
	
	@Test
    public void testListToTempObject() {
        IdeaJdbc.beginTransaction();
        
        List<TempUser> result = IdeaJdbc.query("SELECT C_ID id, NAME name FROM T_USER").listTo(TempUser.class);
        assertEquals(2, result.size());
        assertEquals("Chinakite", result.get(0).getName());
        
        IdeaJdbc.endTransaction();
    }

	@Test
	public void testQueryWithParameter() {
		IdeaJdbc.beginTransaction();
		
		List<User> users = IdeaJdbc.query("select * from t_user where age < :age").setParameter("age", 30, DataItemType.INT).listTo(User.class);
//		assertEquals(1, users.size());
//		assertEquals("2", users.get(0).getId());
//		
//		users = IdeaJdbc.query("select * from t_user where age > :age").setParameter("age", 30).listTo(User.class);
//		assertEquals(1, users.size());
//		assertEquals("1", users.get(0).getId());
//		
//		users = IdeaJdbc.query("select * from t_user where age < ?").setParameter(0, 30).listTo(User.class);
//		assertEquals(1, users.size());
//		assertEquals("2", users.get(0).getId());
		
		users = IdeaJdbc.query("select * from t_user where age > :age and age < ? and age <> :iage and age <> :iage and age <> :iage").setParameter("age", 20).setParameter("iage", 18).setParameter(0, 30, DataItemType.INT).listTo(User.class);
		assertEquals(1, users.size());
		assertEquals("2", users.get(0).getId());
		
		IdeaJdbc.endTransaction();
	}
	
	@Test
	public void testUniqueTo() {
		IdeaJdbc.beginTransaction();
		
		User user = (User)IdeaJdbc.query("select * from t_user where c_id = '1'").uniqueTo(User.class);
		assertEquals("1", user.getId());
		assertEquals("Chinakite", user.getName());
		
		IdeaJdbc.endTransaction();
	}
	
	@Test
	public void testUnique() {
		IdeaJdbc.beginTransaction();
		
		User user = (User)IdeaJdbc.query(User.class, "oldMan").unique();
		assertEquals("1", user.getId());
		assertEquals("Chinakite", user.getName());
		
		IdeaJdbc.endTransaction();
	}
	
	@Test
	public void testFind() {
		IdeaJdbc.beginTransaction();
		
		User user = (User)IdeaJdbc.find(User.class, "1");
		assertEquals("1", user.getId());
		assertEquals("Chinakite", user.getName());
		
		IdeaJdbc.endTransaction();
	}
	
	@Test
	public void testSql() {
		IdeaJdbc.beginTransaction();
		
		int r = IdeaJdbc.sql("insert into T_BEANFORUPDATE (C_ID, C_NAME, C_PRICE)values(?, ?, :price)")
		        .setParameter(0, "2")
		        .setParameter(1, "A1")
		        .setParameter("price", 2.56f)
		        .execute();

		assertEquals(1, r);
		IdeaJdbc.commitTransaction();
	}
	
	@Test
	public void testSave() {
		IdeaJdbc.beginTransaction();
		
		BeanForUpdate bfu = new BeanForUpdate();
		bfu.setName("B2");
		bfu.setPrice(3.14f);
		IdeaJdbc.save(bfu);
		
		IdeaJdbc.commitTransaction();
	}
	
	@Test
	public void testDelete() {
		IdeaJdbc.beginTransaction();
		
		IdeaJdbc.delete(BeanForUpdate.class, "4028f181494d571501494d5715530000");
		
		IdeaJdbc.commitTransaction();
	}
	
	@Test
	public void testUpdate() {
		IdeaJdbc.beginTransaction();
		
		IdeaJdbc.update(BeanForUpdate.class, "1").setProperty("name", "C3").execute();
		
		BeanForUpdate bfu = IdeaJdbc.find(BeanForUpdate.class, "2");
		bfu.setPrice(3.14f);
		IdeaJdbc.update(bfu);
		
		IdeaJdbc.commitTransaction();
	}
	
	@Test
    public void testUpdateIgnoreNullValue() {
        IdeaJdbc.beginTransaction();
        
        IdeaJdbc.update(BeanForUpdate.class, "1").setProperty("name", "C3").execute();
        
        
        BeanForUpdate bfu = new BeanForUpdate();
        bfu.setId("2");
        bfu.setPrice(3.14f);
        IdeaJdbc.update(bfu, true);
        
        IdeaJdbc.commitTransaction();
    }
	
	@Test
	public void testUniqueValue() {
		IdeaJdbc.beginTransaction();
		
		long count = (Long)IdeaJdbc.query("select count(*) from t_user").uniqueValue();
		assertEquals(2, count);
		
		IdeaJdbc.endTransaction();
	}
	
	@Test
	public void testPage() {
		IdeaJdbc.beginTransaction();
		
		Page page1 = IdeaJdbc.query("select * from t_user").page(1, 10);
		assertEquals(2, page1.getTotalRecord());
		assertEquals(10, page1.getPageSize());
		assertEquals(1, page1.getCurrentPage());
		assertEquals(1, page1.getTotalPage());
		assertEquals(2, page1.getData().size());
		
		IdeaJdbc.endTransaction();
	}
	
	@Test
	public void testPageTo() {
		IdeaJdbc.beginTransaction();
		
		Page page1 = IdeaJdbc.query("select * from t_user order by c_id").pageTo(User.class, 1, 10);
		assertEquals(2, page1.getTotalRecord());
		assertEquals(10, page1.getPageSize());
		assertEquals(1, page1.getCurrentPage());
		assertEquals(1, page1.getTotalPage());
		assertEquals(2, page1.getData().size());
		assertEquals("1", ((User)(page1.getData().get(0))).getId());
		assertEquals("Chinakite", ((User)(page1.getData().get(0))).getName());
		
		IdeaJdbc.endTransaction();
	}
	
	@Test
	public void testRangeList() {
		IdeaJdbc.beginTransaction();
		
		List result = IdeaJdbc.query("select * from t_user").rangeList(1, 2);
		assertEquals(2, result.size());
		
		IdeaJdbc.endTransaction();
	}
	
	@Test
	public void testRangeListTo() {
		IdeaJdbc.beginTransaction();
		
		List result = IdeaJdbc.query("select * from t_user").rangeListTo(User.class, 1, 2);
		assertEquals(2, result.size());
		assertEquals("1", ((User)(result.get(0))).getId());
		assertEquals("Chinakite", ((User)(result.get(0))).getName());
		
		IdeaJdbc.endTransaction();
	}
	
	@Test
	public void testInListParameter() {
		IdeaJdbc.beginTransaction();
		
		List<String> ids = new ArrayList<String>();
		ids.add("1");
		ids.add("2");
		ids.add("3");
		List list = IdeaJdbc.query("select * from t_order where c_id in (:ids)").setParameter("ids", ids).listTo(Order.class);
		assertEquals(3, list.size());
		
		IdeaJdbc.endTransaction();
	}
	
	
	@Test
	public void testInArrayParameter() {
		IdeaJdbc.beginTransaction();
		
		List<String> ids = new ArrayList<String>();
		ids.add("1");
		ids.add("2");
		ids.add("3");
		
		String[] userIds = new String[]{"1", "2"};
		List list = IdeaJdbc.query("select * from t_order where c_id in (:ids) and c_userid in (:userIds)")
							.setParameter("ids", ids)
							.setParameter("userIds", userIds)
							.listTo(Order.class);
		assertEquals(3, list.size());
		
		IdeaJdbc.endTransaction();
	}
}
