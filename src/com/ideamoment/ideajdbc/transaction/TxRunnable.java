/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.transaction;

/**
 * 在事务中执行TxRunnable接口.
 * <p>
 * 在IdeaJdbc#tx(TxRunnable)}方法中使用.
 * </p>
 * 
 * <pre class="code">
 * 
 * 
 * IdeaJdbc.tx(new TxRunnable() {
 * 	public void run() {
 * 		User u1 = IdeaJdbc.find(User.class, 1);
 * 		User u2 = IdeaJdbc.find(User.class, 2);
 * 
 * 		u1.setName(&quot;u1 mod&quot;);
 * 		u2.setName(&quot;u2 mod&quot;);
 * 
 * 		IdeaJdbc.save(u1);
 * 		IdeaJdbc.save(u2);
 * 	}
 * });
 * </pre>
 * 
 * @see TxCallable
 */
public interface TxRunnable {

	/**
	 * 在事务中运行一段程序.
	 */
	public void run();
}