package com.brightcove.mediaapi.wrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.brightcove.commons.catalog.objects.Image;
import com.brightcove.commons.catalog.objects.Playlist;
import com.brightcove.commons.catalog.objects.Video;
import com.brightcove.commons.catalog.objects.enumerations.EncodeToEnum;
import com.brightcove.commons.catalog.objects.enumerations.UploadStatusEnum;
import com.brightcove.commons.catalog.objects.enumerations.VideoFieldEnum;
import com.brightcove.commons.http.DefaultHttpClientFactory;
import com.brightcove.commons.http.HttpClientFactory;
import com.brightcove.commons.http.HttpUtils;
import com.brightcove.commons.misc.nvpair.BcHttpParams;
import com.brightcove.mediaapi.exceptions.BrightcoveException;
import com.brightcove.mediaapi.exceptions.BrightcoveExceptionHandler;
import com.brightcove.mediaapi.exceptions.MediaApiException;
import com.brightcove.mediaapi.exceptions.WrapperException;
import com.brightcove.mediaapi.exceptions.WrapperExceptionCode;

/**
 * <p>
 *    This is a wrapper for the HTTP/JSON Media API provided by Brightcove
 *    (<a href="http://www.brightcove.com">http://www.brightcove.com</a>)
 * </p>
 * 
 * <p>
 *    The purpose of this library is to provide a simple interface from Java
 *    applications to the Brightcove Media API.  This class provides the
 *    interface to the WRITE portion of the Media API.
 * </p>
 * 
 * <p>
 *    This library relies on several external libraries:<ul>
 *        <li>(3rd party) <a href="http://www.json.org/java/">json.org</a></li>
 *        <li>(3rd party) <a href="http://commons.apache.org/">Apache Commons</a></li>
 *        <li>(3rd party) <a href="http://hc.apache.org/">Apache HTTP Components</a></li>
 *        <li>(3rd party) <a href="http://james.apache.org/mime4j/">Apache James - Mime4j</a></li>
 *        <li>(Brightcove Open Source) <a href="https://github.com/BrightcoveOS/Java-Commons">Java Commons</li>
 *    </ul>
 * </p>
 * 
 * <p>
 *    For more information on the Media API, see
 *    <a href="http://support.brightcove.com/en/docs/getting-started-media-api">http://support.brightcove.com/en/docs/getting-started-media-api</a>.
 * </p>
 * 
 * @author <a href="https://github.com/three4clavin">three4clavin</a>
 *
 */
public class WriteApi {
	private Logger  log;
	private String  charSet;
	private String  writeProtocolScheme;
	private String  writeHost;
	private Integer writePort;
	private String  writePath;
	
	// private HttpClient httpAgent;
	private HttpClientFactory clientFactory;
	
	private BcHttpParams httpParams;
	
	private static final String  WRITE_API_DEFAULT_SCHEME = "http";
	private static final String  WRITE_API_DEFAULT_HOST   = "api.brightcove.com";
	private static final Integer WRITE_API_DEFAULT_PORT   = 80;
	private static final String  WRITE_API_DEFAULT_PATH   = "/services/post";
	
	private BrightcoveExceptionHandler exceptionHandler;
	
	/**
	 * <p>Default constructor</p>
	 * 
	 * <p>Creates a new Read API wrapper object with default settings<ul>
	 * <li>No logging</li>
	 * <li>UTF-8 character set</li>
	 * </ul></p>
	 */
	public WriteApi(){
		init();
	}
	
	/**
	 * <p>Constructor with logging</p>
	 * 
	 * <p>Creates a new Read API wrapper object with the following settings<ul>
	 * <li>Logging to Logger object provided</li>
	 * <li>UTF-8 character set</li>
	 * </ul></p>
	 * 
	 * @param log java.util.logging.Logger object to log to
	 */
	public WriteApi(Logger log){
		init();
		this.log = log;
	}
	
	/**
	 * <p>Constructor with character set</p>
	 * 
	 * <p>Creates a new Read API wrapper object with the following settings<ul>
	 * <li>No logging</li>
	 * <li>Character set specified</li>
	 * </ul></p>
	 * 
	 * @param characterEncoding Character encoding to use for HTTP URLs and responses
	 */
	public WriteApi(String characterEncoding){
		init();
		this.charSet = characterEncoding;
	}
	
	/**
	 * <p>Constructor with character set and logging</p>
	 * 
	 * <p>Creates a new Read API wrapper object with the following settings<ul>
	 * <li>Logging to Logger object provided</li>
	 * <li>Character set specified</li>
	 * </ul></p>
	 * 
	 * @param log java.util.logging.Logger object to log to
	 * @param characterEncoding Character encoding to use for HTTP URLs and responses
	 */
	public WriteApi(Logger log, String characterEncoding){
		init();
		this.log     = log;
		this.charSet = characterEncoding;
	}

	/**
	 * <p>Called by constructors to initialize variables.</p>
	 */
	private void init(){
		log       = null;
		charSet   = "UTF-8";
		httpParams = null;
		
		writeProtocolScheme = WRITE_API_DEFAULT_SCHEME;
		writeHost           = WRITE_API_DEFAULT_HOST;
		writePort           = WRITE_API_DEFAULT_PORT;
		writePath           = WRITE_API_DEFAULT_PATH;
		
		clientFactory = new DefaultHttpClientFactory();
		
		exceptionHandler = null;
	}
	
	/**
	 * <p>Overrides the standard Write API Server settings to make calls against a test/staging server.</p>
	 * 
	 * @param scheme Protocol to use for call (usually http)
	 * @param host Host/IP address to call
	 * @param port Port to call on server
	 * @param path Path to API application on server
	 */
	public void OverrideWriteApiServerSettings(String scheme, String host, Integer port, String path){
		this.writeProtocolScheme = scheme;
		this.writeHost           = host;
		this.writePort           = port;
		this.writePath           = path;
	}
	
	/**
	 * <p>Overrides the standard factory used to create HTTPClients to connect to the Write API Server.</p>
	 * 
	 * @param clientFactory HttpClientFactory to use to generate HttpClient objects
	 */
	public void OverrideHttpClientFactory(HttpClientFactory clientFactory){
		this.clientFactory = clientFactory;
	}
	
	/**
	 * <p>Issues the command JSON to the Media API and returns the response as a String</p>
	 * 
	 * @param json JSON object to pass to the Media API
	 * @return Response from server in the form of a JSON Object
	 * @throws BrightcoveException If any of the following are true:<ul>
	 * 	<li>The URL or JSON built for the Media API is syntactically invalid</li>
	 * 	<li>The request can not be encoded using the specified character set</li>
	 *  <li>Client communication to Media API uses improper protocol</li>
	 *  <li>I/O exception thrown trying to communicate with Media API</li>
	 *  <li>Response from Media API can't be parsed</li>
	 *  <li>Media API indicates that there is an error with the request</li>
	 * </ul>
	 */
	private JSONObject executeCommand(JSONObject json, File file) throws BrightcoveException {
		if(log != null){
			log.info("JSON Command to execute: '" + json + "'.");
			
			if(file != null){
				log.info("File to upload: '" + file.getAbsolutePath() + "'.");
			}
		}
		
		URI uri = null;
		try{
			uri = URIUtils.createURI(writeProtocolScheme, writeHost, writePort, writePath, URLEncodedUtils.format(new ArrayList<NameValuePair>(), charSet), null);
		}
		catch(URISyntaxException urise){
			throw new WrapperException(WrapperExceptionCode.INVALID_URL_SYNTAX, "Exception: '" + urise + "'");
		}
		
		HttpPost        method   = new HttpPost(uri);
		MultipartEntity entityIn = new MultipartEntity();
		FileBody        fileBody = null;
		
		if(httpParams != null){
			method.setParams(httpParams.generateHttpParams());
		}
		
		if(file != null){
			fileBody = new FileBody(file);
		}
		
		try{
			entityIn.addPart("JSON-RPC", new StringBody(json.toString(), Charset.forName(charSet)));
		}
		catch(UnsupportedEncodingException uee){
			throw new WrapperException(WrapperExceptionCode.INVALID_URL_ENCODING, "Exception: '" + uee + "'");
		}
		
		if(file != null){
			entityIn.addPart(file.getName(), fileBody);
		}
		method.setEntity(entityIn);
		
		HttpResponse response = null;
		String       buffer;
		try{
			HttpClient httpAgent = clientFactory.getHttpClient();
			response = httpAgent.execute(method);
			
			// Make sure the HTTP communication was OK (not the same as an error in the Media API reponse)
			Integer statusCode = response.getStatusLine().getStatusCode();
			if(statusCode != 200){
				httpAgent.getConnectionManager().shutdown();
				throw new WrapperException(WrapperExceptionCode.HTTP_ERROR_RESPONSE_CODE, "Response code from HTTP server: '" + statusCode + "'");
			}
			
			// Parse the response
			HttpEntity entity = response.getEntity();
			buffer = HttpUtils.parseHttpEntity(entity);
			httpAgent.getConnectionManager().shutdown();
		}
		catch(ClientProtocolException cpe){
			throw new WrapperException(WrapperExceptionCode.CLIENT_PROTOCOL_EXCEPTION, "Exception: '" + cpe + "'");
		}
		catch (IllegalStateException ise) {
			throw new WrapperException(WrapperExceptionCode.MAPI_ILLEGAL_STATE_RESPONSE, "Exception: '" + ise + "'");
		}
		catch (IOException ioe) {
			throw new WrapperException(WrapperExceptionCode.MAPI_IO_EXCEPTION, "Exception: '" + ioe + "'");
		}
		
		if(log != null){
			log.info("Raw response from server: '" + buffer + "'.");
		}
		
		// Certain responses from the cache don't really return useful
		// JSON - e.g. an invalid reference id in a find_video_by_reference_id
		// will simply return the string "null"
		if("null".equals(buffer)){
			return null;
		}
		
		// Parse JSON
		JSONObject jsonObj = null;
		try{
			jsonObj = new JSONObject(buffer);
		}
		catch(JSONException jsone){
			throw new WrapperException(WrapperExceptionCode.MAPI_UNPARSABLE_RESPONSE, "JSON Exception: '" + jsone + "'");
		}
		
		// Try to see if there was an error
		MediaApiException mapie  = new MediaApiException(jsonObj);
		if((mapie != null) && (mapie.getResponseCode() != null)){
			throw mapie;
		}
		
		return jsonObj;
	}
	
	// --------------------- Video Write API Methods --------------------------
	
	/**
	 * <p>Create a new video in the account.</p>
	 * 
	 * @param writeToken Write Media API token for the account.
	 * @param video Video object with all of the meta data to create
	 * @param filename Name of file to use for the video rendition
	 * @param encodeTo If the file requires transcoding, use this parameter to specify the target encoding. Valid values are MP4 or FLV, representing the H264 and VP6 codecs respectively. Note that transcoding of FLV files to another codec is not currently supported.
	 * @param createMultipleRenditions If the file is a supported transcodeable type, this optional flag can be used to control the number of transcoded renditions. If true (default), multiple renditions at varying encoding rates and dimensions are created. Setting this to false will cause a single transcoded VP6 rendition to be created at the standard encoding rate and dimensions.
	 * @param preserveSourceRendition If the video file is H.264 encoded and if create_multiple_ renditions=true, then multiple VP6 renditions are created and in addition the H.264 source is retained as an additional rendition.
	 * @param h264NoProcessing Use this option to prevent H.264 source files from being transcoded. This parameter cannot be used in combination with create_multiple_renditions. It is optional and defaults to false.
	 * @return The video id of the video that's been created.
	 * @throws BrightcoveException If any of the following are true:<ul>
	 * 	<li>Request to the Media API fails</li>
	 * 	<li>Media API reports an error with the request</li>
	 * 	<li>Video can not be created</li>
	 * 	<li>Response from the Media API couldn't be parsed</li>
	 * </ul>
	 */
	public Long CreateVideo(String writeToken, Video video, String filename, EncodeToEnum encodeTo, Boolean createMultipleRenditions, Boolean preserveSourceRendition, Boolean h264NoProcessing) throws BrightcoveException {
		try{
			return _CreateVideo(writeToken, video, filename, encodeTo, createMultipleRenditions, preserveSourceRendition, h264NoProcessing);
		}
		catch(BrightcoveException be){
			if(exceptionHandler == null){
				throw be;
			}
			else{
				if(exceptionHandler.handleException(be, "CreateVideo")){
					return CreateVideo(writeToken, video, filename, encodeTo, createMultipleRenditions, preserveSourceRendition, h264NoProcessing);
				}
				else{
					throw be;
				}
			}
		}
	}
	
	// Internal version of CreateVideo
	private Long _CreateVideo(String writeToken, Video video, String filename, EncodeToEnum encodeTo, Boolean createMultipleRenditions, Boolean preserveSourceRendition, Boolean h264NoProcessing) throws BrightcoveException {
		if(log != null){
			if(video.getCreationDate() != null){
				log.warning("Field \"Creation Date\" is set on the video, but this can not be set by the Media API.  This will be passed through to the Media API, but it will be ignored.");
			}
			if(video.getFlvUrl() != null){
				log.warning("Field \"FLV URL\" is set on the video, but this can not be set by the Media API.  This will be passed through to the Media API, but it will be ignored.");
			}
			if(video.getLastModifiedDate() != null){
				log.warning("Field \"Last Modified Date\" is set on the video, but this can not be set by the Media API.  This will be passed through to the Media API, but it will be ignored.");
			}
			if(video.getLength() != null){
				log.warning("Field \"Length\" is set on the video, but this can not be set by the Media API.  This will be passed through to the Media API, but it will be ignored.");
			}
			if(video.getPlaysTotal() != null){
				log.warning("Field \"Plays Total\" is set on the video, but this can not be set by the Media API.  This will be passed through to the Media API, but it will be ignored.");
			}
			if(video.getPlaysTrailingWeek() != null){
				log.warning("Field \"Plays Trailing Week\" is set on the video, but this can not be set by the Media API.  This will be passed through to the Media API, but it will be ignored.");
			}
			if(video.getPublishedDate() != null){
				log.warning("Field \"Published Date\" is set on the video, but this can not be set by the Media API.  This will be passed through to the Media API, but it will be ignored.");
			}
			if(video.getThumbnailUrl() != null){
				log.warning("Field \"Thumbnail URL\" is set on the video, but this can not be set by the Media API.  This will be passed through to the Media API, but it will be ignored.");
			}
			if(video.getVideoStillUrl() != null){
				log.warning("Field \"Video Still URL\" is set on the video, but this can not be set by the Media API.  This will be passed through to the Media API, but it will be ignored.");
			}
		}
		
		if(video.getCuePoints() != null){
			throw new WrapperException(WrapperExceptionCode.USER_REQUESTED_INCORRECT_PARAMETERS, "Video contains cue points, which can not be written to a video in the initial create_video call to the Media API.");
		}
		if(video.getId() != null){
			throw new WrapperException(WrapperExceptionCode.USER_REQUESTED_INCORRECT_PARAMETERS, "Video has the unique Id set, which can not be written via the Media API - it is assigned by the create_video call.");
		}
		if(video.getVideoFullLength() != null){
			throw new WrapperException(WrapperExceptionCode.USER_REQUESTED_INCORRECT_PARAMETERS, "Video has the Video Full Length field set, which can not be written to a video in the initial create_video call to the Media API.");
		}
		if(video.getRenditions() != null){
			throw new WrapperException(WrapperExceptionCode.USER_REQUESTED_INCORRECT_PARAMETERS, "Video contains renditions, which can not be written to a video in the initial create_video call to the Media API.");
		}
		
		File file    = null;
		Long maxSize = 0l;
		if(filename != null){
			file    = new File(filename);
			maxSize = file.length();
		}
		
		String fileChecksum = null;
		try{
			fileChecksum = GenerateFileData.getMD5Checksum(filename);
		}
		catch(Exception e){
			throw new WrapperException(WrapperExceptionCode.USER_REQUESTED_INVALID_FILE, "Exception caught trying to generate hash code for file '" + filename + "': " + e + ".");
		}
		
		JSONObject json = null;
		try{
			json = new JSONObject();
			json.put("method", "create_video");
			
			JSONObject paramObj = new JSONObject();
			json.put("params", paramObj);
			
			paramObj.put("token", writeToken);
			
			// JSONObject videoObj = new JSONObject();
			paramObj.put("video", video.toJson());
			
			paramObj.put("filename",                   file.getName());
			paramObj.put("maxsize",                    maxSize);
			paramObj.put("file_checksum",              fileChecksum);
			paramObj.put("create_multiple_renditions", createMultipleRenditions);
			paramObj.put("preserve_source_rendition",  preserveSourceRendition);
			paramObj.put("H264NoProcessing",           h264NoProcessing);
			
			if(encodeTo != null){
				paramObj.put("encode_to", encodeTo);
			}
		}
		catch(JSONException jsone){
			throw new WrapperException(WrapperExceptionCode.INVALID_JSON_BUILD_REQUEST, "Exception caught trying to add parameters to JSON request object: " + jsone + ".");
		}
		
		JSONObject response = executeCommand(json, file);
		if(response == null){
			throw new WrapperException(WrapperExceptionCode.MAPI_VIDEO_NOT_CREATED, "Null response from Media API when trying to create video '" + video + "'.");
		}
		
		try{
			Long result = response.getLong("result");
			return result;
		}
		catch(JSONException jsone){
			throw new WrapperException(WrapperExceptionCode.MAPI_VIDEO_NOT_CREATED, "No Brightcove Id returned in the response from Media API when trying to create video '" + video + "'.");
		}
	}
	
	/**
	 * <p>Add a new thumbnail or video still image to a video, or assign an existing image to another video.</p>
	 * 
	 * @param writeToken Write Media API token for the account.
	 * @param image Image object with all of the meta data to create
	 * @param filename Name of file to use for the image
	 * @param videoId The id of the video to associate this image with
	 * @param videoReferenceId The reference id of the video to associate this image with
	 * @param resize Set this to false if you don't want your image to be automatically resized to the default size for its type. By default images will be resized.
	 * @return Image object representing the image that's been created.
	 * @throws BrightcoveException If any of the following are true:<ul>
	 * 	<li>Request to the Media API fails</li>
	 * 	<li>Media API reports an error with the request</li>
	 * 	<li>Image could not be added</li>
	 * 	<li>Response from the Media API couldn't be parsed</li>
	 * </ul>
	 */
	public Image AddImage(String writeToken, Image image, String filename, Long videoId, String videoReferenceId, Boolean resize) throws BrightcoveException {
		try{
			return _AddImage(writeToken, image, filename, videoId, videoReferenceId, resize);
		}
		catch(BrightcoveException be){
			if(exceptionHandler == null){
				throw be;
			}
			else{
				if(exceptionHandler.handleException(be, "AddImage")){
					return AddImage(writeToken, image, filename, videoId, videoReferenceId, resize);
				}
				else{
					throw be;
				}
			}
		}
	}
	
	// Internal version of AddImage
	private Image _AddImage(String writeToken, Image image, String filename, Long videoId, String videoReferenceId, Boolean resize) throws BrightcoveException {
		File file    = null;
		Long maxSize = 0l;
		if(filename != null){
			file    = new File(filename);
			maxSize = file.length();
		}
		
		String fileChecksum = null;
		try{
			fileChecksum = GenerateFileData.getMD5Checksum(filename);
		}
		catch(Exception e){
			throw new WrapperException(WrapperExceptionCode.USER_REQUESTED_INVALID_FILE, "Exception caught trying to generate hash code for file '" + filename + "': " + e + ".");
		}
		
		JSONObject json = null;
		try{
			json = new JSONObject();
			json.put("method", "add_image");
			
			JSONObject paramObj = new JSONObject();
			json.put("params", paramObj);
			
			paramObj.put("token", writeToken);
			paramObj.put("image", image.toJson());
			
			paramObj.put("filename",                   file.getName());
			paramObj.put("maxsize",                    maxSize);
			paramObj.put("file_checksum",              fileChecksum);
			
			if(videoId != null){
				paramObj.put("video_id", ""+videoId);
			}
			else if(videoReferenceId != null){
				paramObj.put("video_reference_id", ""+videoReferenceId);
			}
			else{
				throw new WrapperException(WrapperExceptionCode.USER_REQUESTED_INCORRECT_PARAMETERS, "Adding an image to a video must specify either a video id or a video reference id.");
			}
			
			if(resize != null){
				paramObj.put("resize", resize);
			}
		}
		catch(JSONException jsone){
			throw new WrapperException(WrapperExceptionCode.INVALID_JSON_BUILD_REQUEST, "Exception caught trying to add parameters to JSON request object: " + jsone + ".");
		}
		
		JSONObject response = executeCommand(json, file);
		if(response == null){
			throw new WrapperException(WrapperExceptionCode.MAPI_IMAGE_NOT_ADDED, "Null response from Media API when trying to add image '" + image + "'.");
		}
		Image result = null;
		try{
			if(response.getString("result") != null){
				 String jsonResult = response.getString("result");
				 result = new Image(jsonResult);
			}
			return result;
		}
		catch(JSONException jsone){
			throw new WrapperException(WrapperExceptionCode.MAPI_IMAGE_NOT_ADDED, "Couldn't create Image object from Media API response.  Exception caught: " + jsone + ".");
		}
	}
	
	/**
	 * <p>Call this function in an HTTP POST request to determine the status of an upload.</p>
	 * 
	 * @param writeToken The Write API authentication token required to use this method.
	 * @param videoId The id of the video whose status you'd like to get.
	 * @param referenceId The publisher-assigned reference id of the video whose status you'd like to get.
	 * @return UploadStatusEnum object indicating the status of the video
	 * @throws BrightcoveException If any of the following are true:<ul>
	 * 	<li>Request to the Media API fails</li>
	 * 	<li>Media API reports an error with the request</li>
	 * 	<li>Video could not be found or status could not be determined</li>
	 * 	<li>Response from the Media API couldn't be parsed</li>
	 * </ul>
	 */
	public UploadStatusEnum GetUploadStatus(String writeToken, Long videoId, String referenceId) throws BrightcoveException {
		try{
			return _GetUploadStatus(writeToken, videoId, referenceId);
		}
		catch(BrightcoveException be){
			if(exceptionHandler == null){
				throw be;
			}
			else{
				if(exceptionHandler.handleException(be, "GetUploadStatus")){
					return GetUploadStatus(writeToken, videoId, referenceId);
				}
				else{
					throw be;
				}
			}
		}
	}
	
	// Internal version of GetUploadStatus
	private UploadStatusEnum _GetUploadStatus(String writeToken, Long videoId, String referenceId) throws BrightcoveException {
		File file = null;
		
		JSONObject json = null;
		try{
			json = new JSONObject();
			json.put("method", "get_upload_status");
			
			JSONObject paramObj = new JSONObject();
			json.put("params", paramObj);
			
			paramObj.put("token", writeToken);
			
			if(videoId != null){
				paramObj.put("video_id", ""+videoId);
			}
			else if(referenceId != null){
				paramObj.put("reference_id", ""+referenceId);
			}
			else{
				throw new WrapperException(WrapperExceptionCode.USER_REQUESTED_INCORRECT_PARAMETERS, "Requesting the status of a video must specify either a video id or a video reference id.");
			}
		}
		catch(JSONException jsone){
			throw new WrapperException(WrapperExceptionCode.INVALID_JSON_BUILD_REQUEST, "Exception caught trying to add parameters to JSON request object: " + jsone + ".");
		}
		
		JSONObject response = executeCommand(json, file);
		if(response == null){
			throw new WrapperException(WrapperExceptionCode.MAPI_VIDEO_STATUS_UNKNOWN, "Null response from Media API when trying to get status for video (" + videoId + "," + referenceId + ").");
		}
		
		UploadStatusEnum result = null;
		try{
			if(response.getString("result") != null){
				String jsonResult = response.getString("result");
				if(jsonResult.equals(UploadStatusEnum.COMPLETE.toString())){
					result = UploadStatusEnum.COMPLETE;
				}
				else if(jsonResult.equals(UploadStatusEnum.ERROR.toString())){
					result = UploadStatusEnum.ERROR;
				}
				else if(jsonResult.equals(UploadStatusEnum.PROCESSING.toString())){
					result = UploadStatusEnum.PROCESSING;
				}
				else if(jsonResult.equals(UploadStatusEnum.UPLOADING.toString())){
					result = UploadStatusEnum.UPLOADING;
				}
				else{
					throw new WrapperException(WrapperExceptionCode.MAPI_VIDEO_STATUS_UNKNOWN, "Unknown upload status '" + jsonResult + "' returned from Media API.");
				}
			}
		}
		catch(JSONException jsone){
			throw new WrapperException(WrapperExceptionCode.MAPI_VIDEO_STATUS_UNKNOWN, "Couldn't parse Upload Status from json '" + response + "'.  Exception caught: " + jsone + ".");
		}
		
		return result;
	}
	
	/**
	 * <p>Deletes a video.</p>
	 * 
	 * @param writeToken The Write API authentication token required to use this method. A string, generally ending in . (dot).
	 * @param videoId The id of the video you'd like to delete
	 * @param referenceId The publisher-assigned reference id of the video you'd like to delete.
	 * @param cascade Set this to true if you want to delete this video even if it is part of a manual playlist or assigned to a player. The video will be removed from all playlists and players in which it appears, then deleted.
	 * @param deleteShares Set this to true if you want also to delete shared copies of this video. Note that this will delete all shared copies from sharee accounts, regardless of whether or not those accounts are currently using the video in playlists or players.
	 * @return Any output from the Media API as a JSON Object, but this generally will be void or null
	 * @throws BrightcoveException If any of the following are true:<ul>
	 * 	<li>Request to the Media API fails</li>
	 * 	<li>Media API reports an error with the request</li>
	 * 	<li>Video could not be deleted</li>
	 * 	<li>Response from the Media API couldn't be parsed</li>
	 * </ul>
	 */
	public JSONObject DeleteVideo(String writeToken, Long videoId, String referenceId, Boolean cascade, Boolean deleteShares) throws BrightcoveException {
		try{
			return _DeleteVideo(writeToken, videoId, referenceId, cascade, deleteShares);
		}
		catch(BrightcoveException be){
			if(exceptionHandler == null){
				throw be;
			}
			else{
				if(exceptionHandler.handleException(be, "DeleteVideo")){
					return DeleteVideo(writeToken, videoId, referenceId, cascade, deleteShares);
				}
				else{
					throw be;
				}
			}
		}
	}
	
	// Internal version of DeleteVideo
	private JSONObject _DeleteVideo(String writeToken, Long videoId, String referenceId, Boolean cascade, Boolean deleteShares) throws BrightcoveException {
		File file = null;
		
		JSONObject json = null;
		try{
			json = new JSONObject();
			json.put("method", "delete_video");
			
			JSONObject paramObj = new JSONObject();
			json.put("params", paramObj);
			
			paramObj.put("token", writeToken);
			
			if(videoId != null){
				paramObj.put("video_id", ""+videoId);
			}
			else if(referenceId != null){
				paramObj.put("reference_id", ""+referenceId);
			}
			else{
				throw new WrapperException(WrapperExceptionCode.USER_REQUESTED_INCORRECT_PARAMETERS, "Requesting the deletion of a video must specify either a video id or a video reference id.");
			}
			
			if(cascade != null){
				paramObj.put("cascade", cascade);
			}
			if(deleteShares != null){
				paramObj.put("delete_shares", deleteShares);
			}
		}
		catch(JSONException jsone){
			throw new WrapperException(WrapperExceptionCode.INVALID_JSON_BUILD_REQUEST, "Exception caught trying to add parameters to JSON request object: " + jsone + ".");
		}
		
		JSONObject response = executeCommand(json, file);
		if(response == null){
			throw new WrapperException(WrapperExceptionCode.MAPI_VIDEO_NOT_DELETED, "Null response from Media API when trying to delete video (" + videoId + "," + referenceId + ").");
		}
		
		return response;
	}
	
	/**
	 * <p>Shares the specified video with a list of sharee accounts</p>
	 * 
	 * @param writeToken The Write API authentication token required to use this method.
	 * @param videoId The id for video that will be shared.
	 * @param autoAccept If the target account has the option enabled, setting this flag to true will bypass the approval process, causing the shared video to automatically appear in the target account's library. If the target account does not have the option enabled, or this flag is unspecified or false, then the shared video will be queued up to be approved by the target account before appearing in their library.
	 * @param shareeAccountIds List of Account IDs to share video with.
	 * @return Array of new video IDs (one for each account id).
	 * @throws BrightcoveException If any of the following are true:<ul>
	 * 	<li>Request to the Media API fails</li>
	 * 	<li>Media API reports an error with the request</li>
	 * 	<li>Video could not be shared</li>
	 * 	<li>Response from the Media API couldn't be parsed</li>
	 * </ul>
	 */
	public List<Long> ShareVideo(String writeToken, Long videoId, Boolean autoAccept, List<Long> shareeAccountIds) throws BrightcoveException {
		try{
			return _ShareVideo(writeToken, videoId, autoAccept, shareeAccountIds);
		}
		catch(BrightcoveException be){
			if(exceptionHandler == null){
				throw be;
			}
			else{
				if(exceptionHandler.handleException(be, "ShareVideo")){
					return ShareVideo(writeToken, videoId, autoAccept, shareeAccountIds);
				}
				else{
					throw be;
				}
			}
		}
	}
	
	// Internal version of ShareVideo
	private List<Long> _ShareVideo(String writeToken, Long videoId, Boolean autoAccept, List<Long> shareeAccountIds) throws BrightcoveException {
		return _ShareVideo(writeToken, videoId, autoAccept, shareeAccountIds, false);
	}
	
	/**
	 * <p>Shares the specified video with a list of sharee accounts</p>
	 *
	 * @param writeToken The Write API authentication token required to use this method.
	 * @param videoId The id for video that will be shared.
	 * @param autoAccept If the target account has the option enabled, setting this flag to true will bypass the approval process, causing the shared video to automatically appear in the target account's library. If the target account does not have the option enabled, or this flag is unspecified or false, then the shared video will be queued up to be approved by the target account before appearing in their library.
	 * @param shareeAccountIds List of Account IDs to share video with.
	 * @param forceReshare Setting force_reshare to true indicates that if the shared video already exists in the target account's library, it should be overwritten by the video in the sharer's account
	 * @return Array of new video IDs (one for each account id).
	 * @throws BrightcoveException If any of the following are true:<ul>
	 * <li>Request to the Media API fails</li>
	 * <li>Media API reports an error with the request</li>
	 * <li>Video could not be shared</li>
	 * <li>Response from the Media API couldn't be parsed</li>
	 * </ul>
	 */
	public List<Long> ShareVideo(String writeToken, Long videoId, Boolean autoAccept, List<Long> shareeAccountIds, Boolean forceReshare) throws BrightcoveException {
		try{
			return _ShareVideo(writeToken, videoId, autoAccept, shareeAccountIds, forceReshare);
		}
		catch(BrightcoveException be){
			if(exceptionHandler == null){
				throw be;
			}
			else{
				if(exceptionHandler.handleException(be, "ShareVideo")){
					return ShareVideo(writeToken, videoId, autoAccept, shareeAccountIds, forceReshare);
				}
				else{
					throw be;
				}
			}
		}
	}
	
	// Internal version of ShareVideo
	private List<Long> _ShareVideo(String writeToken, Long videoId, Boolean autoAccept, List<Long> shareeAccountIds, Boolean forceReshare) throws BrightcoveException {
		File file = null;
		
		JSONObject json = null;
		try{
			json = new JSONObject();
			json.put("method", "share_video");
			
			JSONObject paramObj = new JSONObject();
			json.put("params", paramObj);
			
			paramObj.put("token", writeToken);
			paramObj.put("video_id", ""+videoId);
			
			if(autoAccept != null){
				paramObj.put("auto_accept", ""+autoAccept);
			}
			if(forceReshare != null){
				paramObj.put("force_reshare", ""+forceReshare);
			}
			
			JSONArray accountIds = new JSONArray();
			for(Long accountId : shareeAccountIds){
				accountIds.put(accountId);
			}
			paramObj.put("sharee_account_ids", accountIds);
		}
		catch(JSONException jsone){
			throw new WrapperException(WrapperExceptionCode.INVALID_JSON_BUILD_REQUEST, "Exception caught trying to add parameters to JSON request object: " + jsone + ".");
		}
		
		JSONObject response = executeCommand(json, file);
		if(response == null){
			throw new WrapperException(WrapperExceptionCode.MAPI_VIDEO_NOT_SHARED, "Null response from Media API when trying to share video '" + videoId + "'.");
		}
		
		try{
			JSONArray  jsonResult = response.getJSONArray("result");
			List<Long> result     = new ArrayList<Long>();
			for(int resultIdx=0;resultIdx<jsonResult.length();resultIdx++){
				Long id = jsonResult.getLong(resultIdx);
				result.add(id);
			}
			
			return result;
		}
		catch(JSONException jsone){
			throw new WrapperException(WrapperExceptionCode.MAPI_VIDEO_NOT_SHARED, "Couldn't parse Video Ids from json '" + response + "'.  Exception caught: " + jsone + ".");
		}
	}
	
	
	/**
	 * <p>Use this method to modify the metadata for a single video in your Brightcove Media Library.</p>
	 * 
	 * @param writeToken The Write API authentication token required to use this method.
	 * @param video The video meta data to be updated.
	 * @return The video object updated.
	 * @throws BrightcoveException If any of the following are true:<ul>
	 * 	<li>Request to the Media API fails</li>
	 * 	<li>Media API reports an error with the request</li>
	 * 	<li>Video could not be updated</li>
	 * 	<li>Response from the Media API couldn't be parsed</li>
	 * </ul>
	 */
	public Video UpdateVideo(String writeToken, Video video) throws BrightcoveException {
		try{
			return _UpdateVideo(writeToken, video);
		}
		catch(BrightcoveException be){
			if(exceptionHandler == null){
				throw be;
			}
			else{
				if(exceptionHandler.handleException(be, "UpdateVideo")){
					return UpdateVideo(writeToken, video);
				}
				else{
					throw be;
				}
			}
		}
	}
	
	// Internal version of UpdateVideo
	private Video _UpdateVideo(String writeToken, Video video) throws BrightcoveException {
		EnumSet<VideoFieldEnum> includeNullFields = VideoFieldEnum.CreateEmptyEnumSet();
		return UpdateVideo(writeToken, video, includeNullFields);
	}
	
	/**
	 * <p>Use this method to modify the metadata for a single video in your Brightcove Media Library.</p>
	 * 
	 * @param writeToken The Write API authentication token required to use this method.
	 * @param video The video meta data to be updated.
	 * @param includeNullFields Set of fields on the video to update even if set to null.
	 * @return The video object updated.
	 * @throws BrightcoveException If any of the following are true:<ul>
	 * 	<li>Request to the Media API fails</li>
	 * 	<li>Media API reports an error with the request</li>
	 * 	<li>Video could not be updated</li>
	 * 	<li>Response from the Media API couldn't be parsed</li>
	 * </ul>
	 */
	public Video UpdateVideo(String writeToken, Video video, EnumSet<VideoFieldEnum> includeNullFields) throws BrightcoveException {
		try{
			return _UpdateVideo(writeToken, video, includeNullFields);
		}
		catch(BrightcoveException be){
			if(exceptionHandler == null){
				throw be;
			}
			else{
				if(exceptionHandler.handleException(be, "UpdateVideo")){
					return UpdateVideo(writeToken, video, includeNullFields);
				}
				else{
					throw be;
				}
			}
		}
	}
	
	// Internal version of UpdateVideo
	private Video _UpdateVideo(String writeToken, Video video, EnumSet<VideoFieldEnum> includeNullFields) throws BrightcoveException {
		File file = null;
		
		JSONObject json = null;
		try{
			json = new JSONObject();
			json.put("method", "update_video");
			
			JSONObject paramObj = new JSONObject();
			json.put("params", paramObj);
			
			paramObj.put("token", writeToken);
			paramObj.put("video", video.toJson(includeNullFields));
		}
		catch(JSONException jsone){
			throw new WrapperException(WrapperExceptionCode.INVALID_JSON_BUILD_REQUEST, "Exception caught trying to add parameters to JSON request object: " + jsone + ".");
		}
		
		JSONObject response = executeCommand(json, file);
		if(response == null){
			throw new WrapperException(WrapperExceptionCode.MAPI_VIDEO_NOT_UPDATED, "Null response from Media API when trying to update video '" + video + "'.");
		}
		
		try{
			Video result = null;
			if(response.getString("result") != null){
				 String jsonResult = response.getString("result");
				 result = new Video(jsonResult);
			}
			return result;
		}
		catch(JSONException jsone){
			System.out.println(jsone);
			throw new WrapperException(WrapperExceptionCode.MAPI_VIDEO_NOT_UPDATED, "Couldn't parse Video object from Media API response when trying to update video '" + video + "'.");
		}
	}
	
	// --------------------- Playlist Write API Methods --------------------------
	
	/**
	 * <p>Creates a playlist. This method must be called using an HTTP POST request and JSON parameters.</p>
	 * 
	 * @param writeToken The authentication token provided to authorize using the Media APIs. A string, generally ending in . (dot).
	 * @param playlist The metadata for the playlist you'd like to create. This takes the form of a JSON object of name/value pairs, each of which corresponds to a settable property of the Playlist object. Populate the videoIds property of the playlist, not the videos property.
	 * @return The ID of the Playlist you created.
	 * @throws BrightcoveException If any of the following are true:<ul>
	 * 	<li>Request to the Media API fails</li>
	 * 	<li>Media API reports an error with the request</li>
	 * 	<li>Playlist could not be created</li>
	 * 	<li>Response from the Media API couldn't be parsed</li>
	 * </ul>
	 */
	public Long CreatePlaylist(String writeToken, Playlist playlist) throws BrightcoveException {
		try{
			return _CreatePlaylist(writeToken, playlist);
		}
		catch(BrightcoveException be){
			if(exceptionHandler == null){
				throw be;
			}
			else{
				if(exceptionHandler.handleException(be, "CreatePlaylist")){
					return CreatePlaylist(writeToken, playlist);
				}
				else{
					throw be;
				}
			}
		}
	}
	
	// Internal version of CreatePlaylist
	private Long _CreatePlaylist(String writeToken, Playlist playlist) throws BrightcoveException {
		if(log != null){
			if(playlist.getAccountId() != null){
				log.warning("Field \"Account Id\" is set on the playlist, but this can not be set by the Media API.  This will be passed through to the Media API, but it will be ignored.");
			}
			if(playlist.getId() != null){
				log.warning("Field \"Id\" is set on the playlist, but this can not be set by the Media API.  This will be passed through to the Media API, but it will be ignored.");
			}
			if(playlist.getThumbnailUrl() != null){
				log.warning("Field \"Thumbnail URL\" is set on the playlist, but this can not be set by the Media API.  This will be passed through to the Media API, but it will be ignored.");
			}
			if(playlist.getVideoIds() != null){
				log.warning("Field \"Video Ids\" is set on the playlist, but this can not be set by the Media API.  This will be passed through to the Media API, but it will be ignored.");
			}
			if(playlist.getVideos() != null){
				log.warning("Field \"Videos\" is set on the playlist, but this can not be set by the Media API.  This will be passed through to the Media API, but it will be ignored.");
			}
		}
		
		File file    = null;
		
		JSONObject json = null;
		try{
			json = new JSONObject();
			json.put("method", "create_playlist");
			
			JSONObject paramObj = new JSONObject();
			json.put("params", paramObj);
			
			paramObj.put("token", writeToken);
			
			paramObj.put("playlist", playlist.toJson());
		}
		catch(JSONException jsone){
			throw new WrapperException(WrapperExceptionCode.INVALID_JSON_BUILD_REQUEST, "Exception caught trying to add parameters to JSON request object: " + jsone + ".");
		}
		
		JSONObject response = executeCommand(json, file);
		if(response == null){
			throw new WrapperException(WrapperExceptionCode.MAPI_PLAYLIST_NOT_CREATED, "Null response from Media API when trying to create playlist '" + playlist + "'.");
		}
		
		try{
			Long result = response.getLong("result");
			return result;
		}
		catch(JSONException jsone){
			throw new WrapperException(WrapperExceptionCode.MAPI_PLAYLIST_NOT_CREATED, "No Brightcove Id returned in the response from Media API when trying to create playlist '" + playlist + "'.");
		}
	}
	
	/**
	 * <p>Updates a playlist, specified by playlist id. This method must be called using an HTTP POST request and JSON parameters.</p>
	 * 
	 * @param writeToken The authentication token provided to authorize using the Media APIs. A string, generally ending in . (dot).
	 * @param playlist The metadata for the playlist you'd like to create. This takes the form of a JSON object of name/value pairs, each of which corresponds to a settable property of the Playlist object. Populate the videoIds property of the playlist, not the videos property.
	 * @return The Playlist updated
	 * @throws BrightcoveException If any of the following are true:<ul>
	 * 	<li>Request to the Media API fails</li>
	 * 	<li>Media API reports an error with the request</li>
	 * 	<li>Playlist could not be updated</li>
	 * 	<li>Response from the Media API couldn't be parsed</li>
	 * </ul>
	 */
	public Playlist UpdatePlaylist(String writeToken, Playlist playlist) throws BrightcoveException {
		try{
			return _UpdatePlaylist(writeToken, playlist);
		}
		catch(BrightcoveException be){
			if(exceptionHandler == null){
				throw be;
			}
			else{
				if(exceptionHandler.handleException(be, "UpdatePlaylist")){
					return UpdatePlaylist(writeToken, playlist);
				}
				else{
					throw be;
				}
			}
		}
	}
	
	// Internal version of UpdatePlaylist
	private Playlist _UpdatePlaylist(String writeToken, Playlist playlist) throws BrightcoveException {
		if(log != null){
			if(playlist.getAccountId() != null){
				log.warning("Field \"Account Id\" is set on the playlist, but this can not be set by the Media API.  This will be passed through to the Media API, but it will be ignored.");
			}
			if(playlist.getId() != null){
				log.warning("Field \"Id\" is set on the playlist, but this can not be set by the Media API.  This will be passed through to the Media API, but it will be ignored.");
			}
			if(playlist.getThumbnailUrl() != null){
				log.warning("Field \"Thumbnail URL\" is set on the playlist, but this can not be set by the Media API.  This will be passed through to the Media API, but it will be ignored.");
			}
			if(playlist.getVideos() != null){
				log.warning("Field \"Videos\" is set on the playlist, but this can not be set by the Media API.  This will be passed through to the Media API, but it will be ignored.");
			}
		}
		
		File file    = null;
		
		JSONObject json = null;
		try{
			json = new JSONObject();
			json.put("method", "update_playlist");
			
			JSONObject paramObj = new JSONObject();
			json.put("params", paramObj);
			
			paramObj.put("token", writeToken);
			
			paramObj.put("playlist", playlist.toJson());
		}
		catch(JSONException jsone){
			throw new WrapperException(WrapperExceptionCode.INVALID_JSON_BUILD_REQUEST, "Exception caught trying to add parameters to JSON request object: " + jsone + ".");
		}
		
		JSONObject response = executeCommand(json, file);
		if(response == null){
			throw new WrapperException(WrapperExceptionCode.MAPI_PLAYLIST_NOT_UPDATED, "Null response from Media API when trying to update playlist '" + playlist + "'.");
		}
		
		try{
			Playlist result = new Playlist(response);
			return result;
		}
		catch(JSONException jsone){
			throw new WrapperException(WrapperExceptionCode.MAPI_PLAYLIST_NOT_UPDATED, "No Playlist returned in the response from Media API when trying to update playlist '" + playlist + "'.");
		}
	}
	
	/**
	 * <p>Deletes a playlist, specified by playlist id.</p>
	 * 
	 * @param writeToken The authentication token provided to authorize using the Media APIs. A string, generally ending in . (dot).
	 * @param playlistId the id for the playlist to delete
	 * @param referenceId The publisher-assigned reference id of the playlist you'd like to delete.
	 * @param cascade Set this to true if you'd like to delete this playlist even if it is referenced by players. The playlist will be removed from all players in which it appears, then deleted.
	 * @return Any response messages from the server
	 * @throws BrightcoveException If any of the following are true:<ul>
	 * 	<li>Request to the Media API fails</li>
	 * 	<li>Media API reports an error with the request</li>
	 * 	<li>Playlist could not be deleted</li>
	 * 	<li>Response from the Media API couldn't be parsed</li>
	 * </ul>
	 */
	public JSONObject DeletePlaylist(String writeToken, Long playlistId, String referenceId, Boolean cascade) throws BrightcoveException {
		try{
			return _DeletePlaylist(writeToken, playlistId, referenceId, cascade);
		}
		catch(BrightcoveException be){
			if(exceptionHandler == null){
				throw be;
			}
			else{
				if(exceptionHandler.handleException(be, "DeletePlaylist")){
					return DeletePlaylist(writeToken, playlistId, referenceId, cascade);
				}
				else{
					throw be;
				}
			}
		}
	}
	
	// Internal version of DeletePlaylist
	private JSONObject _DeletePlaylist(String writeToken, Long playlistId, String referenceId, Boolean cascade) throws BrightcoveException {
		File file    = null;
		
		JSONObject json = null;
		try{
			json = new JSONObject();
			json.put("method", "delete_playlist");
			
			JSONObject paramObj = new JSONObject();
			json.put("params", paramObj);
			
			paramObj.put("token", writeToken);
			
			if(playlistId != null){
				paramObj.put("playlist_id", playlistId);
			}
			else if(referenceId != null){
				paramObj.put("reference_id", referenceId);
			}
			else{
				throw new WrapperException(WrapperExceptionCode.USER_REQUESTED_INCORRECT_PARAMETERS, "Deletion of a playlist requires either a playlist id or a playlist reference id.");
			}
			
			if(cascade != null){
				paramObj.put("cascade", cascade);
			}
		}
		catch(JSONException jsone){
			throw new WrapperException(WrapperExceptionCode.INVALID_JSON_BUILD_REQUEST, "Exception caught trying to add parameters to JSON request object: " + jsone + ".");
		}
		
		JSONObject response = executeCommand(json, file);
		if(response == null){
			throw new WrapperException(WrapperExceptionCode.MAPI_PLAYLIST_NOT_DELETED, "Null response from Media API when trying to delete playlist (" + playlistId + "," + referenceId + ").");
		}
		
		return response;
	}
	
	/**
	 * <p>
	 *    This shouldn't commonly be used, but before the post request is
	 *    submitted, these parameters can be added to the method.
	 * </p>
	 * 
	 * <p>
	 *    This is roughly analagous to:<br/>
	 *    &nbsp;&nbsp;&nbsp;&nbsp;HttpPost method = new HttpPost(uri);
	 *    &nbsp;&nbsp;&nbsp;&nbsp;method.getParams().setParameter(key, value);
	 * </p>
	 * 
	 * @param httpParams Extra parameters to attach to the post method
	 */
	public void setExtraPostParams(BcHttpParams httpParams){
		this.httpParams = httpParams;
	}
	
	/**
	 * <p>
	 *    This shouldn't commonly be used, but before the post request is
	 *    submitted, these parameters can be added to the method.
	 * </p>
	 * 
	 * <p>
	 *    This is roughly analagous to:<br/>
	 *    &nbsp;&nbsp;&nbsp;&nbsp;HttpPost method = new HttpPost(uri);
	 *    &nbsp;&nbsp;&nbsp;&nbsp;method.getParams().setParameter(key, value);
	 * </p>
	 * 
	 * @return Extra parameters to attach to the post method
	 */
	public BcHttpParams getExtraPostParams(){
		return httpParams;
	}
	
	public void setBrightcoveExceptionHandler(BrightcoveExceptionHandler exceptionHandler){
		this.exceptionHandler = exceptionHandler;
	}
}

/**
 * Generates an XML file with system data for a file and/or directory
 * 
 * 
 * TODO - move this to java commons libs
 *
 */
class GenerateFileData {
	/**
	 * This implementation is cribbed from http://www.rgagnon.com/javadetails/java-0416.html
	 * 
	 * @param filename Path to file to generate checksum for
	 * @return Checksum for file
	 * @throws Exception
	 */
	public static byte[] createChecksum(String filename) throws FileNotFoundException, NoSuchAlgorithmException, IOException {
		InputStream   fis      =  new FileInputStream(filename);
		byte[]        buffer   = new byte[1024];
		MessageDigest complete = MessageDigest.getInstance("MD5");
		
		int numRead;
		do {
			numRead = fis.read(buffer);
			if (numRead > 0) {
				complete.update(buffer, 0, numRead);
			}
		} while (numRead != -1);
		fis.close();
		return complete.digest();
	}
	
	/**
	 * Faster version of the above
	 * 
	 * @param filename Path to file to generate checksum for
	 * @return Checksum for file
	 * @throws Exception
	 */
	public static String getMD5Checksum(String filename) throws FileNotFoundException, NoSuchAlgorithmException, IOException {
		byte[] b = createChecksum(filename);
		String result = "";
		for (int i=0; i < b.length; i++) {
			result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return result;
	}
}
