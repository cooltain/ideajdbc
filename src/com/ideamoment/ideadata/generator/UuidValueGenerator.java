/*
 * Rainbow is a ORM implement of IDEAEE platform.
 * Copyright @ www.ideamoment.com
 * All right reserved.
 *  
 */
package com.ideamoment.ideadata.generator;

import java.io.Serializable;
import java.net.InetAddress;

import com.ideamoment.ideajdbc.exception.IdeaJdbcException;

/**
 * UUID生成器。
 * 
 * @author Chinakite Zhang
 * @version 2011-2-2
 * @since 0.1
 */
public class UuidValueGenerator implements ValueGenerator {

	private static final int IP;
	
	private static final int JVM = (int) ( System.currentTimeMillis() >>> 8 );
	
	private static short counter = (short) 0;
	
	private String sep = "";
	
	static {
		int ipadd;
		try {
			ipadd = toInt(InetAddress.getLocalHost().getAddress());
		}
		catch (Exception e) {
			ipadd = 0;
		}
		IP = ipadd;
	}
	
	/* (non-Javadoc)
	 * @see com.ideamoment.rainbow.generator.ValueGenerator#generate(java.lang.Object)
	 */
	public Object generate(Object entity) throws IdeaJdbcException {
		return new StringBuffer(36)
			.append( format( getIP() ) ).append(sep)
			.append( format( getJVM() ) ).append(sep)
			.append( format( getHiTime() ) ).append(sep)
			.append( format( getLoTime() ) ).append(sep)
			.append( format( getCount() ) )
			.toString();
	}
	
	private String format(int intval) {
		String formatted = Integer.toHexString(intval);
		StringBuffer buf = new StringBuffer("00000000");
		buf.replace( 8-formatted.length(), 8, formatted );
		return buf.toString();
	}

	private String format(short shortval) {
		String formatted = Integer.toHexString(shortval);
		StringBuffer buf = new StringBuffer("0000");
		buf.replace( 4-formatted.length(), 4, formatted );
		return buf.toString();
	}
	
	/**
	 * Unique in a local network
	 */
	private int getIP() {
		return IP;
	}
	
	/**
	 * Unique across JVMs on this machine (unless they load this class
	 * in the same quater second - very unlikely)
	 */
	private int getJVM() {
		return JVM;
	}
	
	/**
	 * Unique down to millisecond
	 */
	private short getHiTime() {
		return (short) ( System.currentTimeMillis() >>> 32 );
	}
	
	private int getLoTime() {
		return (int) System.currentTimeMillis();
	}
	
	/**
	 * Unique in a millisecond for this JVM instance (unless there
	 * are > Short.MAX_VALUE instances created in a millisecond)
	 */
	protected short getCount() {
		synchronized(UuidValueGenerator.class) {
			if (counter<0) counter=0;
			return counter++;
		}
	}
	
	private static int toInt( byte[] bytes ) {
		int result = 0;
		for (int i=0; i<4; i++) {
			result = ( result << 8 ) - Byte.MIN_VALUE + (int) bytes[i];
		}
		return result;
	}

}
