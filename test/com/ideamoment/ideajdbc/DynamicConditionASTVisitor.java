/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc;

import java.util.Map;
import java.util.Stack;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;
import com.ideamoment.ideajdbc.action.Parameter;


/**
 * @author Chinakite
 *
 */
public class DynamicConditionASTVisitor extends SQLASTVisitorAdapter implements
        SQLASTVisitor {

    protected Map<Object, Parameter> parameters;
    
    protected SQLStatement outputStatement;
    
    protected Stack syntaxStack = new Stack();
    protected Stack exprStack = new Stack();
    
    private int needDel = 0;
    
    public DynamicConditionASTVisitor(Map<Object, Parameter> params, SQLStatement outputStatement) {
        this.parameters = params;
        this.outputStatement = outputStatement;
    }

    public SQLStatement getOutputStatement() {
        return this.outputStatement;
    }
    
    public boolean visit(SQLSelect x) {
        SQLSelect select = new SQLSelect();
        select.setParent(outputStatement);
        ((SQLSelectStatement)outputStatement).setSelect(select);
        
        syntaxStack.push(select);
        
        return true;
    }
    
    public void endVisit(SQLSelect x) {
        syntaxStack.pop();
    }
    
    public boolean visit(SQLSelectQueryBlock x) {
        SQLSelect select = (SQLSelect)syntaxStack.peek();
        SQLSelectQueryBlock queryBlock = new SQLSelectQueryBlock();
        queryBlock.setParent(select);
        select.setQuery(queryBlock);
        syntaxStack.push(queryBlock);
        return true;
    }
    
    public void endVisit(SQLSelectQueryBlock x) {
        if(exprStack.size() > 0) {
            SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)syntaxStack.peek();
            SQLBinaryOpExpr binExpr = (SQLBinaryOpExpr)exprStack.pop();
            queryBlock.setWhere(binExpr);
        }
        
//        while(exprStack.size() > 0) {
//            Object curObj = exprStack.pop();
//            if(curObj instanceof SQLBinaryOpExpr) {
//                SQLBinaryOpExpr binExpr = new SQLBinaryOpExpr();
//                if(binExpr.getLeft() != null && binExpr.getRight() != null) {
//                    exprStack.pop();
//                }
//            }
//        }
         
        
//        Object curObj = syntaxStack.peek();
//        if(curObj instanceof SQLBinaryOpExpr) {
//            syntaxStack.pop();
//            SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)syntaxStack.peek();
//            SQLBinaryOpExpr binExpr = (SQLBinaryOpExpr)curObj;
//            queryBlock.setWhere(binExpr);
//        }
        
//        syntaxStack.pop();
    }
    
    /**
     * Select的字段
     */
    public boolean visit(SQLSelectItem x) {
        SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)syntaxStack.peek();
        queryBlock.addSelectItem(x);
        return false;
    }
    
    public boolean visit(SQLExprTableSource x) {
        SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)syntaxStack.peek();
        queryBlock.setFrom(x);
        return false;
    }
    
    public boolean visit(SQLJoinTableSource x) {
        SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)syntaxStack.peek();
        queryBlock.setFrom(x);
        return false;
    }
    
    public boolean visit(SQLBinaryOpExpr x) {
        SQLBinaryOpExpr binExpr = new SQLBinaryOpExpr();
        binExpr.setOperator(x.getOperator());
        
        SQLExpr left = x.getLeft();
        SQLExpr right = x.getRight();
        
        if(right instanceof SQLVariantRefExpr) {
            SQLVariantRefExpr varExpr = (SQLVariantRefExpr)x.getRight();
            if(varExpr.getName().trim().startsWith("@")) {
                String keyName = varExpr.getName().substring(1);
                Parameter p = parameters.get(keyName);
                if(p == null || p.getValue() == null) {
//                    if(exprStack.size() > 0) {
//                        exprStack.pop();
//                    }
                    needDel = 1;
                    return false;
                }else{
                    binExpr.setLeft(left);
                    binExpr.setRight(right);
                    if(exprStack.size() == 0) {
                        exprStack.push(binExpr);
                    }else{
                        Object peekObj = exprStack.peek();
                        if(peekObj instanceof SQLBinaryOpExpr) {
                            SQLBinaryOpExpr parentExpr = (SQLBinaryOpExpr)peekObj;
                            if(parentExpr.getLeft() == null) {
                                parentExpr.setLeft(binExpr);
                            }else{
                                parentExpr.setRight(binExpr);
                            }
                        }
                    }
                }
            }else{
                binExpr.setLeft(left);
                binExpr.setRight(right);
                if(exprStack.size() == 0) {
                    exprStack.push(binExpr);
                }else{
                    Object peekObj = exprStack.peek();
                    if(peekObj instanceof SQLBinaryOpExpr) {
                        SQLBinaryOpExpr parentExpr = (SQLBinaryOpExpr)peekObj;
                        if(parentExpr.getLeft() == null) {
                            parentExpr.setLeft(binExpr);
                        }else{
                            parentExpr.setRight(binExpr);
                        }
                    }
                }
            }
        }else{
            exprStack.push(binExpr);
        }
        
        return true;
    }
    
    public void endVisit(SQLBinaryOpExpr x) {
        if(needDel == 1) {
            
        }
    }

}
