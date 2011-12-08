package com.brightcove.mediaapi.exceptions;

/**
 * <p>
 *    Allows users to specify their own logic on what to do when an exception
 *    is thrown by the Media API or the Java wraper.
 * </p>
 * 
 * @author <a href="https://github.com/three4clavin">three4clavin</a>
 *
 */
public interface BrightcoveExceptionHandler {
	/**
	 * <p>
	 *    Called whenever the Media API wrapper encounters an exception
	 * </p>
	 * 
	 * @param be Exception thrown by the Media API or the Java wrapper
	 * @return True if the wrapper method should be retries, False if the exception should be re-thrown
	 * @throws BrightcoveException If query shouldn't be retried
	 */
	public Boolean handleException(BrightcoveException be, String methodName) throws BrightcoveException;
}
