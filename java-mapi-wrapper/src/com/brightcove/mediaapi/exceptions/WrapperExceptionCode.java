package com.brightcove.mediaapi.exceptions;

/**
 * <p>
 *    Custom error codes that can be reported by the Media API Wrapper
 * </p>
 * 
 * @author <a href="https://github.com/three4clavin">three4clavin</a>
 *
 */
public enum WrapperExceptionCode {
	// 100 Series: Error building request to send to the Media API
	INVALID_URL_SYNTAX(100,          "Couldn't build Media API URL - Invalid URL syntax"),
	INVALID_URL_ENCODING(101,        "Couldn't build Media API URL - Invalid URL encoding"),
	INVALID_JSON_BUILD_REQUEST(110,  "Couldn't build Media API request JSON"),
	
	// 200 Series: Error communicating with Media API server
	HTTP_ERROR_RESPONSE_CODE(200,    "Couldn't communicate with Media API - HTTP server returned error response code"),
	CLIENT_PROTOCOL_EXCEPTION(201,   "Couldn't communicate with Media API - Client used invalid protocol"),
	MAPI_IO_EXCEPTION(202,           "Couldn't communicate with Media API - I/O exception thrown"),
	MAPI_ILLEGAL_STATE_RESPONSE(203, "Couldn't communicate with Media API - Illegal state exception caught trying to parse response"),
	
	// 300 Series: Error tyring to parse the response from the Media API
	MAPI_UNPARSABLE_RESPONSE(300,    "Couldn't parse response from Media API"),
	MAPI_UNPARSABLE_VIDEO(301,       "Couldn't parse video from Media API response"),
	MAPI_UNPARSABLE_VIDEOS(302,      "Couldn't parse video list from Media API response"),
	MAPI_UNPARSABLE_PLAYLIST(310,    "Couldn't parse playlist from Media API response"),
	MAPI_UNPARSABLE_PLAYLISTS(311,   "Couldn't parse playlist list from Media API response"),
	
	// 400 Series: Media API couldn't return desired objects
	MAPI_VIDEO_NOT_FOUND(400,        "Couldn't find the requested video"),
	MAPI_VIDEO_NOT_CREATED(401,      "Couldn't create the requested video"),
	MAPI_IMAGE_NOT_ADDED(402,        "Couldn't add image to video"),
	MAPI_VIDEO_NOT_DELETED(403,      "Couldn't delete the requested video"),
	MAPI_VIDEO_NOT_SHARED(404,       "Couldn't share the requested video"),
	MAPI_VIDEO_NOT_UPDATED(405,      "Couldn't update the requested video"),
	MAPI_VIDEO_STATUS_UNKNOWN(406,   "Couldn't get status of requested video"),
	MAPI_PLAYLIST_NOT_CREATED(407,   "Couldn't create the requested playlist"),
	MAPI_PLAYLIST_NOT_DELETED(408,   "Couldn't delete the requested playlist"),
	MAPI_PLAYLIST_NOT_UPDATED(409,   "Couldn't update the requested playlist"),
	MAPI_UNKNOWN_NULL(499,           "Media API returned a \"null\" response - unclear how to handle this response"),
	
	// 500 Series: User error
	USER_REQUESTED_TOO_MANY_VIDEOS_PER_PAGE(500,    "Requested too many videos per page"),
	USER_REQUESTED_REFERENCE_ID_WITH_COMMA(501,     "Request included a Reference Id field with a comma in it - this is not allowed by the Media API"),
	USER_REQUESTED_TOO_MANY_PLAYLISTS_PER_PAGE(510, "Requested too many playlists per page"),
	USER_REQUESTED_INCORRECT_PARAMETERS(520,        "Request contained incorrect parameters"),
	USER_REQUESTED_INVALID_FILE(530,                "Request included reference to file that does not exist or could not be read"),
	
	// Old:
	oINVALID_URL_SYNTAX(1100,          "Invalid URL syntax"),
	oINVALID_URL_ENCODING(1101,        "Invalid URL encoding"),
	oINVALID_JSON_PARAMETERS(1102,     "Invalid parameters for JSON request"),
	
	oINVALID_CLIENT_PROTOCOL(1110,     "Invalid internet protocol used by client"),
	oHTTP_IO_ERROR(1111,               "IO error trying to communicate with HTTP server"),
	
	oRESPONSE_READ_ERROR(1120,         "Error trying to read server response"),
	oHTTP_SERVER_ERROR(1121,           "API server returned bad status code"),
	
	oTOO_MANY_VIDEOS_PER_PAGE(1130,    "Requested too many videos per page"),
	oTOO_MANY_PLAYLISTS_PER_PAGE(1140, "Requested too many playlists per page"),
	
	oLIST_ITEM_CONTAINS_COMMAS(1150,   "Item in list contains commas, which is not valid in JSON requests"),
	
	oVIDEO_PARAMETERS_INCORRECT(1160,  "Incorrect parameters used in Video object construction"),
	oIMAGE_PARAMETERS_INCORRECT(1162,  "Incorrect parameters used in Image object construction"),
	
	oJSON_PARSE_EXCEPTION(1200,        "JSON parse exception"),
	
	oVIDEO_PARSE_FAIL(1300,            "Couldn't parse Video object from JSON"),
	
	oPLAYLIST_PARSE_FAIL(1310,         "Couldn't parse Playlist object from JSON"),
	
	oUNKNOWN_EXCEPTION(1999,           "Unknown exception.");
	
	private final Integer code;
	private final String  description;
	WrapperExceptionCode(Integer code, String description){
		this.code        = code;
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Integer getCode() {
		return code;
	}
}
