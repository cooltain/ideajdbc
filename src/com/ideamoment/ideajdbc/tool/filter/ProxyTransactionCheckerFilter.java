/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.tool.filter;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.ideamoment.ideajdbc.IdeaJdbc;
import com.ideamoment.ideajdbc.server.Db;
import com.ideamoment.ideajdbc.transaction.Transaction;


/**
 * 如果通过Spring的AOP使用了@IdeaJdbcTx进行事务代理管理，那么有可能因为自己的失误造成
 * 连接泄漏，因此提供此Filter进行检查。
 * 
 * <b>使用此Filter时在web.xml中要配置为第一个Filter.</b>
 * 
 * @author Chinakite
 *
 */
public class ProxyTransactionCheckerFilter implements Filter {

    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse resp,
                         FilterChain chain)
                                 throws IOException, ServletException {

        chain.doFilter(req, resp);
        
        Collection<Db> dbs = IdeaJdbc.getAllDbs();
        StringBuffer buf = new StringBuffer();
        buf.append("Db [");
        int i=0;
        for(Db db : dbs) {
            Transaction tx = db.getCurrentTransaction();
            if(tx != null) {
                if(i > 0) {
                    buf.append(",");
                }
                buf.append(db.getName());
                i++;
            }
        }
        buf.append("]");
        if(i > 0) {
            buf.append(" maybe transaction leaked. Request url is")
               .append(((HttpServletRequest)req).getRequestURI());
            throw new Error(buf.toString());
        }
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig config) throws ServletException {

    }

}
