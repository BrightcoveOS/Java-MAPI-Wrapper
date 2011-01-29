package com.brightcove.mediaapi.wrapper.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import com.brightcove.commons.catalog.objects.Image;
import com.brightcove.commons.catalog.objects.Playlist;
import com.brightcove.commons.catalog.objects.Video;
import com.brightcove.commons.catalog.objects.enumerations.EconomicsEnum;
import com.brightcove.commons.catalog.objects.enumerations.EncodeToEnum;
import com.brightcove.commons.catalog.objects.enumerations.GeoFilterCodeEnum;
import com.brightcove.commons.catalog.objects.enumerations.ImageTypeEnum;
import com.brightcove.commons.catalog.objects.enumerations.ItemStateEnum;
import com.brightcove.commons.catalog.objects.enumerations.PlaylistTypeEnum;
import com.brightcove.commons.catalog.objects.enumerations.UploadStatusEnum;
import com.brightcove.commons.system.commandLine.CommandLineProgram;
import com.brightcove.mediaapi.exceptions.BrightcoveException;
import com.brightcove.mediaapi.wrapper.WriteApi;

public class WriteTests extends CommandLineProgram {
	Logger log;
	
	/**
	 * <p>
	 *    Constructor
	 * </p>
	 */
	public WriteTests(){
		log = Logger.getLogger(this.getClass().getCanonicalName());
	}
	
	/**
	 * <p>
	 *    Main execution kickoff
	 * </p>
	 * 
	 * @param args Arguments passed in on command line
	 */
	public static void main(String[] args) {
		WriteTests wt = new WriteTests();
		
		wt.allowNormalArgument("write-token",      "--write-token <Write API Token>",   "--write-token:              Media API Token from Brightcove account allowing write access",         true);
		wt.allowNormalArgument("video-filename",   "--video-filename <File Path>",      "--video-filename:           Path to video file on disk",                                            false);
		wt.allowNormalArgument("account-id",       "--account-id <Publisher Id>",       "--account-id:               Brighcove Account/Publisher Id",                                        false);
		wt.allowNormalArgument("thumb-filename",   "--thumb-filename <File Path>",      "--thumb-filename:           Path to image file on disk for thumbnail",                              false);
		wt.allowNormalArgument("still-filename",   "--still-filename <File Path>",      "--still-filename:           Path to image file on disk for video still",                            false);
		wt.allowNormalArgument("child-account-id", "--child-account-id <Publisher Id>", "--child-account-id:         Brightcove Account/Publisher Id for child account (for media sharing)", false);
		
		wt.setMaxNakedArguments(0);
		wt.setMinNakedArguments(0);
		
		wt.run(args);
	}
	
	/* (non-Javadoc)
	 * @see com.brightcove.commons.system.commandLine.CommandLineProgram#run(java.lang.String[])
	 */
	public void run(String[] args) {
		setCaller(this.getClass().getCanonicalName());
		parseArguments(args);
		
		String writeToken     = getNormalArgument("write-token");
		String videoFilename  = getNormalArgument("video-filename");
		Long   accountId      = castToLong(getNormalArgument("account-id"));
		String thumbFilename  = getNormalArgument("thumb-filename");
		String stillFilename  = getNormalArgument("still-filename");
		Long   childAccountId = castToLong(getNormalArgument("child-account-id"));
		
		log.info("Configuration:\n" +
			"\tWrite token:          '" + writeToken     + "'\n." +
			"\tVideo filename:       '" + videoFilename  + "'\n." +
			"\tAccount id:           '" + accountId      + "'\n." +
			"\tThumbnail filename:   '" + thumbFilename  + "'\n." +
			"\tVideo still filename: '" + stillFilename  + "'\n." +
			"\tChild account id:     '" + childAccountId + "'\n.");
		
		// Video object to fill up and send up
		Video video = new Video();
		
		// Instantiate a WriteApi wrapper object to make the actual calls
		Logger  log  = Logger.getLogger("ApiTests");
		WriteApi writeApi = new WriteApi(log);
		
		log.info("Setting up video object to write");
		
		// --------------------- Video Write API Methods ------------------
		Boolean createMultipleRenditions = false;
		Boolean preserveSourceRendition  = false;
		Boolean h264NoProcessing         = false;
		
		// ---- Required fields ----
		video.setName("this is the video name");
		video.setShortDescription("this is the short description");
		
		// ---- Optional fields ----
		if(accountId != null){
			video.setAccountId(accountId);
		}
		video.setEconomics(EconomicsEnum.FREE);
		video.setItemState(ItemStateEnum.ACTIVE);
		video.setLinkText("Brightcove");
		video.setLinkUrl("http://www.brightcove.com");
		video.setLongDescription("this is the long description");
		video.setReferenceId("this is the reference id");
		video.setStartDate(new Date((new Date()).getTime() - 30*1000*60 )); // 30 minutes ago
		
		// ---- Complex (and optional) fields ----
		// End date must be in the future - add 30 minutes to "now"
		Date endDate = new Date();
		endDate.setTime(endDate.getTime() + (30*1000*60));
		video.setEndDate(endDate);
		
		// Geo-filtering must be combined with filtered countries
		video.setGeoFiltered(true);
		List<GeoFilterCodeEnum> geoFilteredCountries = new ArrayList<GeoFilterCodeEnum>();
		geoFilteredCountries.add(GeoFilterCodeEnum.lookupByName("UNITED STATES"));
		geoFilteredCountries.add(GeoFilterCodeEnum.CA);
		video.setGeoFilteredCountries(geoFilteredCountries);
		video.setGeoFilteredExclude(true);
		
		// Tags must be added as a list of strings
		List<String> tags = new ArrayList<String>();
		tags.add("tag one");
		tags.add("tag two");
		video.setTags(tags);
		
		// By definition, custom fields are custom and vary by account
		// Essentially though, you have a list of key-value pairs
		// List<CustomField> customFields = new ArrayList<CustomField>();
		// customFields.add(new CustomField("foo", "bar"));
		// video.setCustomFields(customFields);
		
		Long newVideoId = null;
		
		if(videoFilename != null){
			// ****
			// **** Create Video
			// ****
			try{
				log.info("Writing video to Media API");
				newVideoId = writeApi.CreateVideo(writeToken, video, videoFilename, EncodeToEnum.FLV, createMultipleRenditions, preserveSourceRendition, h264NoProcessing);
				log.info("New video id: '" + newVideoId + "'.");
			}
			catch(BrightcoveException be){
				usage(be);
			}
			
			if(thumbFilename != null){
				// ****
				// **** Add Thumbnail
				// ****
				try{
					log.info("Setting up Thumbnail Image object to add to newly created video.");
					
					Image thumbnail  = new Image();
					thumbnail.setReferenceId("this is the thumbnail refid");
					thumbnail.setDisplayName("this is the thumbnail");
					thumbnail.setType(ImageTypeEnum.THUMBNAIL);
					
					Boolean resizeImage = true;
					
					log.info("Writing images to Media API");
					Image thumbReturn = writeApi.AddImage(writeToken, thumbnail, thumbFilename, newVideoId, null, resizeImage);
					
					log.info("Thumbnail image: " + thumbReturn + ".");
				}
				catch(BrightcoveException be){
					usage(be);
				}
			}
			
			if(stillFilename != null){
				try{
					log.info("Setting up Video Still Image object to add to newly created video.");
					
					Image videoStill = new Image();
					videoStill.setReferenceId("this is the video still refid");
					videoStill.setDisplayName("this is the video still");
					videoStill.setType(ImageTypeEnum.VIDEO_STILL);
					
					Boolean resizeImage = true;
					
					log.info("Writing images to Media API");
					
					Image stillReturn = writeApi.AddImage(writeToken, videoStill, stillFilename, newVideoId, null, resizeImage);
					
					log.info("Video still image: " + stillReturn + ".");
				}
				catch(BrightcoveException be){
					usage(be);
				}
			}
			
			// ****
			// **** Get Upload Status
			// ****
			try{
				log.info("Getting status of created video");
				UploadStatusEnum status = writeApi.GetUploadStatus(writeToken, newVideoId, null);
				log.info("Status: '" + status + "'.");
			}
			catch(BrightcoveException be){
				usage(be);
			}
			
			if(childAccountId != null){
				// ****
				// **** Share Video
				// ****
				try{
					log.info("Sharing video out to child account");
					Boolean autoAccept   = true; // If enabled in the account, bypasses normal manual workflow
					Boolean forceReshare = true; // Forces the reshare
					List<Long> shareeAccountIds = new ArrayList<Long>();
					shareeAccountIds.add(childAccountId);
					List<Long> shareResponse = writeApi.ShareVideo(writeToken, newVideoId, autoAccept, shareeAccountIds, forceReshare);
					log.info("Shared ids: '" + shareResponse + "'.");
				}
				catch(BrightcoveException be){
					usage(be);
				}
			}
		}
		
		Long       playlistId = null;
		Playlist   playlist   = new Playlist();
		List<Long> videoIds   = new ArrayList<Long>();
		
		// ****
		// **** Create Playlist
		// ****
		try{
			log.info("Setting up new playlist");
			List<String> filterTags = new ArrayList<String>();
			filterTags.add("this is a filter tag");
			
			if(newVideoId != null){
				videoIds.add(newVideoId);
			}
			
			List<Video> videos = new ArrayList<Video>();
			if(videoFilename != null){
				videos.add(video);
			}
			
			playlist.setFilterTags(filterTags);
			playlist.setName("this is the playlist name");
			playlist.setPlaylistType(PlaylistTypeEnum.EXPLICIT);
			playlist.setReferenceId("this is the playlist reference id");
			playlist.setShortDescription("this is the playlist short description");
			
			log.info("Creating new playlist in Media API");
			playlistId = writeApi.CreatePlaylist(writeToken, playlist);
			log.info("New playlist id: '" + playlistId + "'.");
		}
		catch(BrightcoveException be){
			usage(be);
		}
		
		// ****
		// **** Update Playlist
		// ****
		try{
			log.info("Updating the playlist with a new name and a video");
			playlist.setName("this is also the playlist name");
			playlist.setVideoIds(videoIds);
			Playlist updatedPlaylist = writeApi.UpdatePlaylist(writeToken, playlist);
			log.info("Updated playlist: '" + updatedPlaylist + "'.");
		}
		catch(BrightcoveException be){
			usage(be);
		}
		
		// ****
		// **** Delete Playlist
		// ****
		try{
			log.info("Deleting playlist");
			Boolean cascadePlaylistDelete = true; // Set this to true if you'd like to delete this playlist even if it is referenced by players
			String playlistDeleteResponse = "" + writeApi.DeletePlaylist(writeToken, playlistId, null, cascadePlaylistDelete);
			log.info("Response from server for delete: '" + playlistDeleteResponse + "'.");
		}
		catch(BrightcoveException be){
			usage(be);
		}
		
		if(videoFilename != null){
			// ****
			// **** Delete Video
			// ****
			try{
				log.info("Deleting created video");
				Boolean cascade      = true; // Deletes even if it is in use by playlists/players
				Boolean deleteShares = true; // Deletes if shared to child accounts
				String deleteResponse = "" + writeApi.DeleteVideo(writeToken, newVideoId, null, cascade, deleteShares);
				log.info("Response from server for delete (no message is perfectly OK): '" + deleteResponse + "'.");
			}
			catch(BrightcoveException be){
				usage(be);
			}
		}
	}
	
	/**
	 * <p>
	 *    Parses a string for a Long value, guarding against null and parse
	 *    exceptions
	 * </p>
	 * 
	 * @param val String to parse for Long value
	 * @return Long value
	 */
	private Long castToLong(String val){
		if(val == null){
			return null;
		}
		
		Long ret = null;
		try{
			ret = new Long(val);
		}
		catch(Exception e){
			return null;
		}
		
		return ret;
	}
}
