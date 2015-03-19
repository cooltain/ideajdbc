/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.transaction;

/**
 * 在事务中执行一段程序并返回结果.
 * 
 * <pre class="code">
 * String email = IdeaJdbc.tx(new TxCallable&lt;String&gt;() {
 * 	public String call() {
 * 		User u1 = IdeaJdbc.find(User.class, 1);
 * 		User u2 = IdeaJdbc.find(User.class, 2);
 * 
 * 		u1.setName(&quot;u1 mod&quot;);
 * 		u2.setName(&quot;u2 mod&quot;);
 * 
 * 		IdeaJdbc.save(u1);
 * 		IdeaJdbc.save(u2);
 * 
 * 		return u1.getEmail();
 * 	}
 * });
 * </pre>
 * @see TxRunnable 
 * 
 * 
 * @author Chinakite
 *
 */
public interface TxCallable<T> {
	/**
	 * 在事务中执行一段程序并返回结果.
	 * <p>
	 * 如果你不想返回结果，则使用TxRunnable更为合适.
	 * </p>
	 */
	public T call();
}
