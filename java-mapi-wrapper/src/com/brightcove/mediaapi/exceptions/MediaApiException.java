package com.brightcove.mediaapi.exceptions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <p>
 *    This is an exception thrown by the Media API (not the Java wrapper)
 * </p>
 * 
 * @author <a href="https://github.com/three4clavin">three4clavin</a>
 *
 */
public class MediaApiException extends BrightcoveException {
	private static final long serialVersionUID = -4136614631876754138L;
	
	private JSONObject jsonObject;
	private Integer    responseCode;
	private String     responseMessage;
	
	public static final Integer MEDIA_API_ERROR_UNPARSABLE = -1;
	
	/**
	 * <p>
	 *    Creates a new Media API Exception using JSON returned by the Media
	 *    API server to populate any details.
	 * </p>
	 * 
	 * @param jsonObject Details of exception as reported by the server
	 * @throws WrapperException If JSON can't be parsed for some reason
	 */
	public MediaApiException(JSONObject jsonObject) throws WrapperException {
		this.type = ExceptionType.MEDIA_API_EXCEPTION;
		
		this.responseCode    = null;
		this.responseMessage = null;
		this.jsonObject      = jsonObject;
		
		// Parse out the error object if available
		JSONObject errorObj  = null;
		try{
			String errorString = jsonObject.getString("error");
			if(errorString == null){ errorString = "null"; }
			
			if("null".equals(errorString)){
				// Ignore - basically "null" means there was no error
			}
			else{
				errorObj = new JSONObject(errorString);
			}
		}
		catch(JSONException jsone){
			// Couldn't find an "error" entry, request was probably successful
			errorObj = null;
		}
		
		// Parse the error code and message out of the error object
		if(errorObj != null){
			// Known formats for error JSON:
			//     {
			//         "error": "invalid token",
			//         "code":210
			//     }
			//     
			//     {
			//         "result": null,
			//         "error": { 
			//             "code": 103, 
			//             "name": "CallTimeoutError", 
			//             "message": "The request you made is taking longer than expected to return. If requesting a large amount of data please try again with a smaller page_size."
			//         } 
			//     }
			
			try{
				responseCode    = jsonObject.getInt("code");
				responseMessage = jsonObject.getString("error");
			}
			catch(JSONException jsone){
				// Maybe it's on the error object instead of the json object
				try{
					responseCode    = errorObj.getInt("code");
					responseMessage = errorObj.getString("message");
				}
				catch(JSONException jsone2){
					responseCode = MEDIA_API_ERROR_UNPARSABLE;
				}
			}
		}
	}
	
	/**
	 * <p>
	 *    Returns the JSON reported by the Media API server
	 * </p>
	 * 
	 * @return JSON returned by the server
	 */
	public JSONObject getJsonObject(){
		return this.jsonObject;
	}
	
	/**
	 * <p>
	 *    Sets the JSON reported by the Media API server
	 * </p>
	 * 
	 * @param jsonObject JSON returned by the server
	 */
	public void setJsonObject(JSONObject jsonObject){
		this.jsonObject = jsonObject;
	}
	
	/**
	 * <p>
	 *    Returns the numeric error code reported by the Media API server
	 * </p>
	 * 
	 * @return Numeric error code reported by the server
	 */
	public Integer getResponseCode(){
		return this.responseCode;
	}
	
	/**
	 * <p>
	 *    Sets the numeric error code reported by the Media API server
	 * </p>
	 * 
	 * @param responseCode Numeric error code reported by the server
	 */
	public void setResponseCode(Integer responseCode){
		this.responseCode = responseCode;
	}
	
	/**
	 * <p>
	 *    Returns the textual description of the error as reported by the
	 *    Media API server
	 * </p>
	 * 
	 * @return Description of error as reported by the server
	 */
	public String getResponseMessage(){
		return responseMessage;
	}
	
	/**
	 * <p>
	 *    Sets the textual description of the error as reported by the Media
	 *    API server
	 * </p>
	 * 
	 * @param responseMessage Description of error as reported by the server
	 */
	public void setResponseMessage(String responseMessage){
		this.responseMessage = responseMessage;
	}
	
	/* (non-Javadoc)
	 * @see com.brightcove.mediaapi.exceptions.BrightcoveException#toString()
	 */
	public String toString(){
		return "[" + this.getClass().getCanonicalName() + "] JSON Object: '" + jsonObject + "'";
	}
}
