/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideadata.description;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ideamoment.ideadata.annotation.DataItemType;
import com.ideamoment.ideadata.annotation.Entity;
import com.ideamoment.ideadata.annotation.Id;
import com.ideamoment.ideadata.annotation.Ref;
import com.ideamoment.ideadata.annotation.Transient;
import com.ideamoment.ideajdbc.annotation.Sql;
import com.ideamoment.ideajdbc.annotation.SqlQuery;

/**
 * @author Chinakite
 *
 */
@Sql( 
	queries=
		{
			@SqlQuery(
				name="all", sql="SELECT * FROM T_USER"),
			@SqlQuery(
				name="oldMan", sql="SELECT u.C_ID, u.NAME, EMAIL, AGE FROM T_USER u WHERE AGE > 30"),
		    @SqlQuery(
		    	name="userOrder", sql="SELECT u2.C_ID as id, u2.NAME FROM (SELECT * FROM T_USER u1 WHERE u1.AGE > 30) u2")
		},
	updates=
    	{
		
    	}
)

@Entity(dataSet="T_USER")
public class User implements Serializable{
	@Transient
	private static final long serialVersionUID = 5247563322004258311L;
	
	@Id(dataItem="C_ID", type=DataItemType.VARCHAR2)
	private String id;
	
	private String name;
	
	private String email;
	
	private int age;
	
	@Ref(entityClass=Order.class)
	private List orders = new ArrayList();
	
	/**
	 * @return the orders
	 */
	public List getOrders() {
		return orders;
	}
	/**
	 * @param orders the orders to set
	 */
	public void setOrders(List orders) {
		this.orders = orders;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}
	
}
