/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.ideamoment.ideajdbc.action.Parameter;


/**
 * @author Chinakite
 *
 */
public class TestDruid {
    @Test
    public void testSqlParser() {
//        String sql = "SELECT id, NAME, AGE FROM USER u, ORDER o WHERE u.ID = @id and o.id in (select id from usergroup where ug.status = :status) and (u.NAME = :name or o.addDate > ?)";
        
//        String sql = "SELECT id, NAME, AGE FROM USER u, ORDER o WHERE u.ID = @id and (u.NAME = :name or o.addDate > ?)";
        
        String sql = "SELECT id, NAME, AGE FROM USER where ID = @id and NAME = @name";
        
        StringBuilder out = new StringBuilder();
        MySqlOutputVisitor visitor = new MySqlOutputVisitor(out);
        MySqlStatementParser parser = new MySqlStatementParser(sql);
        List<SQLStatement> statementList = parser.parseStatementList();
        
        HashMap<Object, Parameter> params = new HashMap<Object, Parameter>();
//        params.put("name", new Parameter("name", "Chinakite"));
        params.put("id", new Parameter("id", "aaa"));
        
        for (SQLStatement statement : statementList) {
            SQLStatement outputStmt = null;
            if(statement instanceof SQLSelectStatement) {
                outputStmt = new SQLSelectStatement();
            }
            
            statement.accept(visitor);
            visitor.println();
            
            DynamicConditionASTVisitor dynamicConditionVisitor = new DynamicConditionASTVisitor(params, outputStmt);
            statement.accept(dynamicConditionVisitor);
            
            outputStmt = dynamicConditionVisitor.getOutputStatement();
            System.out.println(">>>>");
            System.out.println(SQLUtils.toMySqlString(outputStmt));
            System.out.println("<<<<");
        }
        System.out.println(out.toString()); 
        
//        SQLExpr expr = SQLUtils.toSQLExpr(sql, JdbcUtils.MYSQL);
//        HashMap<Object, Parameter> params = new HashMap<Object, Parameter>();
//        params.put("name", new Parameter("name", "Chinakite"));
//        
//        SQLExpr outputExpr = null;
//        
//        expr.accept(new DynamicConditionASTVisitor(params, outputExpr));
//        
//        System.out.println(SQLUtils.toMySqlString(expr));
    }
}
