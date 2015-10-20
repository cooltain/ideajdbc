/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;


/**
 * @author Chinakite
 *
 */
public class DynamicConditionFilter {
    /**
     * 
     * @return
     */
    public SQLStatement filter(SQLStatement sqlStmt) {
        if(sqlStmt instanceof SQLSelectStatement) {
            SQLSelectStatement outputStmt = new SQLSelectStatement();
            
            SQLSelectStatement selectStmt = (SQLSelectStatement)sqlStmt;
            SQLSelect select = selectStmt.getSelect();
            MySqlSelectQueryBlock query = (MySqlSelectQueryBlock)select.getQuery();
            SQLExpr whereExpr = query.getWhere();

            if(whereExpr instanceof SQLBinaryOpExpr) {
                SQLBinaryOpExpr binaryExpr = (SQLBinaryOpExpr)whereExpr;
                SQLExpr leftExpr = binaryExpr.getLeft();
                SQLExpr rightExpr = binaryExpr.getRight();
                
//                if(leftExpr instanceof )
                
            }
            
        }
        
        return null;
    }
    
}
