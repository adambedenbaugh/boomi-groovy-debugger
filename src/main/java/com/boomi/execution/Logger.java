package com.boomi.execution;

/**
 * The Logger class is used to mock Boomi's getBaseLogger() method within the Data process Shape.
 * It is used to write log messages to the standard output.
 */
public class Logger {

	/**
	 * Logs an informational message to standard out.
	 *
	 * @param message the informational message to log to standard out.
	 */
	public void info(String message) { System.out.println(message); }

	/**
	 * Logs a warning message to standard out.
	 *
	 * @param message the warning message to log to standard out.
	 */
	public void warning(String message)
	{
		System.out.println(message);
	}

	/**
	 * Logs a severe error message to standard out.
	 *
	 * @param message the severe error message to log to standard out.
	 */
	public void severe(String message)
	{
		System.out.println(message);
	}

	/**
	 * Logs a fine message to standard out.
	 *
	 * @param message the fine message to log to standard out.
	 */
	public void fine(String message)
	{
		System.out.println(message);
	}
}
