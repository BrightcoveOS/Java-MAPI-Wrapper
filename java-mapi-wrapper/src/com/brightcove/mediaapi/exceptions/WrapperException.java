package com.brightcove.mediaapi.exceptions;

/**
 * <p>
 *    A Media API Wrapper exception - e.g. couldn't communicate with server,
 *    couldn't parse response from server, etc.
 * </p>
 * 
 * @author <a href="https://github.com/three4clavin">three4clavin</a>
 *
 */
public class WrapperException extends BrightcoveException {
	private static final long serialVersionUID = -4695861147571405702L;
	
	private WrapperExceptionCode code;
	private String               message;
	
	/**
	 * <p>
	 *    Creates a Media API Wrapper exception - this usually indicates that
	 *    the server couldn't be contacted, or the response couldn't be
	 *    parsed.  This is different than a Media API exception (where all
	 *    of the communication was fine, but the Media API server reported
	 *    an error with the request).
	 * </p>
	 * 
	 * @param code Error code reported by the wrapper
	 * @param message Detailed message reported by the wrapper
	 */
	public WrapperException(WrapperExceptionCode code, String message) {
		super();
		this.code    = code;
		this.message = message;
		this.type    = ExceptionType.WRAPPER_EXCEPTION;
	}
	
	/**
	 * <p>
	 *    Gets the error code reported by the wrapper
	 * </p>
	 * 
	 * @return Error code reported by the wrapper
	 */
	public WrapperExceptionCode getCode(){
		return code;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage(){
		return message;
	}
	
	/* (non-Javadoc)
	 * @see com.brightcove.mediaapi.exceptions.BrightcoveException#toString()
	 */
	public String toString(){
		return "[" + this.getClass().getCanonicalName() + "] (" + code.getCode() + ": " + code.getDescription() + ") Message: '" + message + "'";
	}
}
