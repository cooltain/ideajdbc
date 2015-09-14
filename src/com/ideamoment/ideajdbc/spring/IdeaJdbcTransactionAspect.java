/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.spring;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ideamoment.ideajdbc.transaction.ScopeTransaction;
import com.ideamoment.ideajdbc.transaction.ScopeTransactionManager;
import com.ideamoment.ideajdbc.transaction.ScopeTransactionThreadLocal;
import com.ideamoment.ideajdbc.transaction.TxStrategy;
import com.ideamoment.ideajdbc.transaction.TxStrategyThreadLocal;

/**
 * 
 * 
 * @author Chinakite
 *
 */
@Aspect
public class IdeaJdbcTransactionAspect {
	
	private static final Logger logger = LoggerFactory.getLogger(IdeaJdbcTransactionAspect.class);
	
	@Pointcut("@annotation(com.ideamoment.ideajdbc.spring.IdeaJdbcTx)")  
    public void methodTxPointcut(){
    } 
	
	@Around(value="methodTxPointcut()", argNames="joinPoint")
	public Object txAround(ProceedingJoinPoint joinPoint) throws Throwable {  
		//对已有ScopeTransaction增加引用计数
		ScopeTransactionManager.increaseRefCount();
		
		MethodSignature ms = (MethodSignature)joinPoint.getSignature();
        Method method = ms.getMethod();
        IdeaJdbcTx txAnn = (IdeaJdbcTx)method.getAnnotation(IdeaJdbcTx.class);
        
        logger.debug("Init transaction strategy before method[" + method.getName() + "]");
        
        TxStrategy strategy = new TxStrategy();
        strategy.setType(txAnn.type());
        strategy.setIsolation(txAnn.isolation());
        strategy.setReadOnly(txAnn.readOnly());
        List<Class<? extends Throwable>> rollback = Arrays.asList(txAnn.rollbackFor());
        strategy.setRollbackFor(rollback);
        
        List<Class<? extends Throwable>> noRollback = Arrays.asList(txAnn.noRollbackFor());
        strategy.setNoRollbackFor(noRollback);
        
        TxStrategyThreadLocal.set(strategy);
        
        try {
        	Object retVal = joinPoint.proceed();
        	ScopeTransactionManager.success();
        	return retVal;
        } catch(Throwable e) {
        	logger.debug("Handle transaction after throwing.");
        	ScopeTransactionManager.error(e);
    		throw e;
        } finally {
        	logger.debug("Handle transaction after method[" + method.getName() + "].");
        	ScopeTransactionManager.end();
    		ScopeTransactionManager.reduceRefCount();
        }
	}
}
