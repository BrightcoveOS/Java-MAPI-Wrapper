package com.brightcove.mediaapi.exceptions;

/**
 * <p>
 *    All calls to the Media API Wrapper can throw a BrightcoveException.
 *    Most should throw a subclassed version with specific information (e.g. a
 *    wrapper-specific exception will throw a WrapperException).
 * </p>
 * 
 * @author <a href="https://github.com/three4clavin">three4clavin</a>
 * 
 */
public class BrightcoveException extends Exception {
	private static final long serialVersionUID = 1312325283793630179L;
	
	protected ExceptionType type;
	
	/**
	 * <p>
	 *    Creates a generic exception with type UNKNOWN_TYPE.
	 * </p>
	 */
	public BrightcoveException(){
		super();
		type = ExceptionType.UNKNOWN_TYPE;
	}
	
	/**
	 * <p>
	 *    Creates an exception of type JAVA_EXCEPTION.  This will operate
	 *    the same as a java.lang.Exception.
	 * </p>
	 * 
	 * @param message Exception message
	 */
	public BrightcoveException(String message){
		super(message);
		type = ExceptionType.JAVA_EXCEPTION;
	}
	
	/**
	 * <p>
	 *    Creates an exception of type JAVA_EXCEPTION.  This will operate
	 *    the same as a java.lang.Exception.
	 * </p>
	 * 
	 * @param e java.lang.Exception to replicate
	 */
	public BrightcoveException(Exception e){
		super(e.getMessage());
		this.setStackTrace(e.getStackTrace());
		type = ExceptionType.JAVA_EXCEPTION;
	}
	
	/**
	 * <p>
	 *    Gets the type of exception.  Different methods may be available
	 *    depending on the type of exception thrown.
	 * </p>
	 * 
	 * @return Type of exception thrown
	 */
	public ExceptionType getType(){
		return this.type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#toString()
	 */
	public String toString(){
		return "[" + this.getClass().getCanonicalName() + " TYPE=" + type + "] Message: '" + this.getMessage() + "'";
	}
}
