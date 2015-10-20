/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.action;


/**
 * @author Chinakite
 *
 */
public class ColumnNameLowerDecoration implements ColumnNameDecoration {

    /* (non-Javadoc)
     * @see com.ideamoment.ideajdbc.action.ColumnNameDecoration#decorate(java.lang.String)
     */
    @Override
    public String decorate(String columnName) {
        return columnName.toLowerCase();
    }

}
