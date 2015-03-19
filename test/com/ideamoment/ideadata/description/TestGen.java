/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideadata.description;

import java.io.Serializable;
import java.util.Date;

import com.ideamoment.ideadata.annotation.DataItemType;
import com.ideamoment.ideadata.annotation.Entity;
import com.ideamoment.ideadata.annotation.Id;
import com.ideamoment.ideadata.annotation.Property;
import com.ideamoment.ideadata.annotation.Transient;

/**
 * @author Chinakite
 *
 */
@Entity(dataSet="T_TESTGEN")
public class TestGen implements Serializable {
	@Transient
	private static final long serialVersionUID = 1013614723733094475L;
	
	@Id(dataItem="C_ID", type=DataItemType.VARCHAR, length=32, generator="uuid")
	private String id;
	
	/**
	 * 姓名
	 */
	@Property(dataItem="C_NAME", length=200)
	private String name;
	
	@Property(dataItem="C_BIRTH")
	private Date birthDay;
	
	@Property(dataItem="C_AGE")
	private int age;

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
	 * @return the birthDay
	 */
	public Date getBirthDay() {
		return birthDay;
	}

	/**
	 * @param birthDay the birthDay to set
	 */
	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
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
