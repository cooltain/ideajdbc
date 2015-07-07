/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.description;


/**
 * @author Chinakite
 *
 */
public class PartitionDescription {
    /**
     * 分区字段
     */
    protected String dataItem;
    
    /**
     * 分区属性
     */
    protected String property;

    /**
     * @return the dataItem
     */
    public String getDataItem() {
        return dataItem;
    }

    
    /**
     * @param dataItem the dataItem to set
     */
    public void setDataItem(String dataItem) {
        this.dataItem = dataItem;
    }
    
    /**
     * @return the property
     */
    public String getProperty() {
        return property;
    }
    
    /**
     * @param property the property to set
     */
    public void setProperty(String property) {
        this.property = property;
    }
    
}
