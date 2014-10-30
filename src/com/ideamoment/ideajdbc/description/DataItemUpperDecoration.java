/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.description;

import com.ideamoment.ideadata.description.IdDescription;
import com.ideamoment.ideadata.description.PropertyDescription;
import com.ideamoment.ideadata.description.PropertyDescriptionDecoration;

/**
 * @author Chinakite
 *
 */
public class DataItemUpperDecoration implements PropertyDescriptionDecoration {

	/* (non-Javadoc)
	 * @see com.ideamoment.ideadata.description.PropertyDescriptionDecoration#decorate(com.ideamoment.ideadata.description.PropertyDescription)
	 */
	@Override
	public PropertyDescription decorate(PropertyDescription propertyDescription) {
		propertyDescription.setDataItem(propertyDescription.getDataItem().toUpperCase());
		return propertyDescription;
	}

	/* (non-Javadoc)
	 * @see com.ideamoment.ideadata.description.PropertyDescriptionDecoration#decorate(com.ideamoment.ideadata.description.IdDescription)
	 */
	@Override
	public IdDescription decorate(IdDescription idDescription) {
		idDescription.setDataItem(idDescription.getDataItem().toUpperCase());
		return idDescription;
	}

}
