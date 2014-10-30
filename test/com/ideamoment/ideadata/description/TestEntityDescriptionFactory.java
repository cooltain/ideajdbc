/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideadata.description;

import org.junit.Test;

import com.ideamoment.ideajdbc.configuration.IdeaJdbcConfiguration;

import static org.junit.Assert.*;

/**
 * @author Chinakite
 *
 */
public class TestEntityDescriptionFactory {
	@Test
	public void testGetEntityDescription() {
		EntityDescription entityDesc = EntityDescriptionFactory.getInstance().getEntityDescription(User.class);
		
		assertEquals("T_USER", entityDesc.getDataSet());
		assertEquals(4, entityDesc.getPropertySize());
	}
}
