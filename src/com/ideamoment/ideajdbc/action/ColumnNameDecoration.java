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
public interface ColumnNameDecoration {
    
    public static final ColumnNameDecoration UPPER = new ColumnNameUpperDecoration();
    
    public static final ColumnNameDecoration LOWER = new ColumnNameLowerDecoration();
    
    public String decorate(String columnName);
    
}
