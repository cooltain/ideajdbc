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
public class ColumnNameUpperDecoration implements ColumnNameDecoration {

    @Override
    public String decorate(String columnName) {
        return columnName.toUpperCase();
    }

}
