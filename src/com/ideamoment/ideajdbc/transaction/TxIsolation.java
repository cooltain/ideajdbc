/*
 * IdeaJdbc is a JDBC Data Access Framework. It depends on IdeaData Framework.
 *
 * copyright © www.ideamoment.com
 */
package com.ideamoment.ideajdbc.transaction;

import java.sql.Connection;

/**
 * 事务隔离级别, 所有选项都与JDBC Connection相同
 * 
 * @author Chinakite
 */
public enum TxIsolation {
	/**
	 * Read Committed Isolation level. This is typically the default for most
	 * configurations.
	 */
	READ_COMMITED(Connection.TRANSACTION_READ_COMMITTED),

	/**
	 * Read uncommitted Isolation level.
	 */
	READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),

	/**
	 * Repeatable Read Isolation level.
	 */
	REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),

	/**
	 * Serializable Isolation level.
	 */
	SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE),

	/**
	 * No Isolation level.
	 */
	NONE(Connection.TRANSACTION_NONE),

	/**
	 * The default isolation level. This typically means the default that the
	 * DataSource is using or configured to use.
	 */
	DEFAULT(-1);

	final int level;

	private TxIsolation(int level) {
		this.level = level;
	}

	/**
	 * Return the level as per java.sql.Connection.
	 * <p>
	 * Note that -1 denotes the default isolation level.
	 * </p>
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Return the TxIsolation given the java.sql.Connection isolation level.
	 * <p>
	 * Note that -1 denotes the default isolation level.
	 * </p>
	 */
	public static TxIsolation fromLevel(int connectionIsolationLevel) {

		switch (connectionIsolationLevel) {
		case Connection.TRANSACTION_READ_UNCOMMITTED:
			return TxIsolation.READ_UNCOMMITTED;

		case Connection.TRANSACTION_READ_COMMITTED:
			return TxIsolation.READ_COMMITED;

		case Connection.TRANSACTION_REPEATABLE_READ:
			return TxIsolation.REPEATABLE_READ;

		case Connection.TRANSACTION_SERIALIZABLE:
			return TxIsolation.SERIALIZABLE;

		case Connection.TRANSACTION_NONE:
			return TxIsolation.NONE;

		case -1:
			return TxIsolation.DEFAULT;

		default:
			throw new RuntimeException("Unknown isolation level " + connectionIsolationLevel);
		}

	}
}
