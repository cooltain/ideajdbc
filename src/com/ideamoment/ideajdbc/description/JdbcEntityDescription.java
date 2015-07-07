/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.description;

import com.ideamoment.ideadata.description.EntityDescription;


/**
 * @author Chinakite
 *
 */
public class JdbcEntityDescription extends EntityDescription {
    /**
     * 分区信息描述
     */
    protected PartitionDescription partitionDescription;

    /**
     * @return the partitionDescription
     */
    public PartitionDescription getPartitionDescription() {
        return partitionDescription;
    }
    
    /**
     * @param partitionDescription the partitionDescription to set
     */
    public void setPartitionDescription(PartitionDescription partitionDescription) {
        this.partitionDescription = partitionDescription;
    }
    
}
