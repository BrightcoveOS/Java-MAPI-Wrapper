package com.brightcove.mediaapi.wrapper.test;

import java.util.Calendar;
import java.util.EnumSet;
import java.util.Set;
import java.util.logging.Logger;

import com.brightcove.commons.catalog.objects.Playlist;
import com.brightcove.commons.catalog.objects.Playlists;
import com.brightcove.commons.catalog.objects.Video;
import com.brightcove.commons.catalog.objects.Videos;
import com.brightcove.commons.catalog.objects.enumerations.PlaylistFieldEnum;
import com.brightcove.commons.catalog.objects.enumerations.SortByTypeEnum;
import com.brightcove.commons.catalog.objects.enumerations.SortOrderTypeEnum;
import com.brightcove.commons.catalog.objects.enumerations.VideoFieldEnum;
import com.brightcove.commons.catalog.objects.enumerations.VideoStateFilterEnum;
import com.brightcove.commons.collection.CollectionUtils;
import com.brightcove.commons.system.commandLine.CommandLineProgram;
import com.brightcove.mediaapi.exceptions.BrightcoveException;
import com.brightcove.mediaapi.exceptions.ExceptionType;
import com.brightcove.mediaapi.exceptions.MediaApiException;
import com.brightcove.mediaapi.wrapper.ReadApi;

public class ReadTests extends CommandLineProgram {
	Logger log;
	
	/**
	 * <p>
	 *    Constructor
	 * </p>
	 */
	public ReadTests(){
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
		ReadTests rt  = new ReadTests();
		
		rt.allowNormalArgument("read-token",              "--read-token <Read API Token>",                       "--read-token:              Media API Token from Brightcove account allowing read access", true);
		rt.allowNormalArgument("video-id",                "--video-id <BC Video Id>",                            "--video-id:                Brightcove Video Id to search for in account",                 false);
		rt.allowNormalArgument("video-id-2",              "--video-id-2<BC Video Id 2>",                         "--video-id-2:              Second Brightcove Video Id to search for in account",          false);
		rt.allowNormalArgument("reference-id",            "--reference-id <Reference Id>",                       "--reference-id:            Video Reference Id to search for in account",                  false);
		rt.allowNormalArgument("reference-id-2",          "--reference-id-2 <Reference Id 2>",                   "--reference-id-2:          Second Video Reference Id to search for in account",           false);
		rt.allowNormalArgument("user-id",                 "--user-id <User Id>",                                 "--user-id:                 User Id to test searching by User Id",                         false);
		rt.allowNormalArgument("campaign-id",             "--campaign-id <Campaign Id>",                         "--campaign-id:             Campaign Id to test searching by Campaign Id",                 false);
		rt.allowNormalArgument("search-text",             "--search-text <Text>",                                "--search-text:             Text to test text search with",                                false);
		rt.allowNormalArgument("search-tag",              "--search-tag <Tag>",                                  "--search-tag:              Tag to test tag search with",                                  false);
		rt.allowNormalArgument("playlist-id",             "--playlist-id <Playlist Id>",                         "--playlist-id:             Playlist Id to lookup in the account",                         false);
		rt.allowNormalArgument("playlist-id-2",           "--playlist-id-2 <Playlist Id 2>",                     "--playlist-id-2:           Second Playlist Id to lookup in the account",                  false);
		rt.allowNormalArgument("playlist-reference-id",   "--playlist-reference-id <Playlist Reference Id>",     "--playlist-reference-id:   Playlist Reference Id to lookup in the account",               false);
		rt.allowNormalArgument("playlist-reference-id-2", "--playlist-reference-id-2 <Playlist Reference Id 2>", "--playlist-reference-id-2: Second Playlist Reference Id to lookup in the account",        false);
		rt.allowNormalArgument("player-id",               "--player-id <Player Id>",                             "--player-id:               Player Id to lookup in the account",                           false);
		
		rt.setMaxNakedArguments(0);
		rt.setMinNakedArguments(0);
		
		rt.run(args);
	}
	
	/* (non-Javadoc)
	 * @see com.brightcove.commons.system.commandLine.CommandLineProgram#run(java.lang.String[])
	 */
	public void run(String[] args){
		setCaller(this.getClass().getCanonicalName());
		parseArguments(args);
		
		String readToken            = getNormalArgument("read-token");
		Long   videoId              = castToLong(getNormalArgument("video-id"));
		Long   videoId2             = castToLong(getNormalArgument("video-id-2"));
		String referenceId          = getNormalArgument("reference-id");
		String referenceId2         = getNormalArgument("reference-id-2");
		String userId               = getNormalArgument("user-id");
		String campaignId           = getNormalArgument("campaign-id");
		String searchText           = getNormalArgument("search-text");
		String searchTag            = getNormalArgument("search-tag");
		Long   playlistId           = castToLong(getNormalArgument("playlist-id"));
		Long   playlistId2          = castToLong(getNormalArgument("playlist-id-2"));
		String playlistReferenceId  = getNormalArgument("playlist-reference-id");
		String playlistReferenceId2 = getNormalArgument("playlist-reference-id-2");
		String playerId             = getNormalArgument("player-id");
		
		log.info("Configuration:\n" +
			"\tRead token:             '" + readToken            + "'.\n" + 
			"\tVideo id:               '" + videoId              + "'.\n" + 
			"\tVideo id 2:             '" + videoId2             + "'.\n" + 
			"\tReference id:           '" + referenceId          + "'.\n" + 
			"\tReference id 2:         '" + referenceId2         + "'.\n" + 
			"\tUser id:                '" + userId               + "'.\n" + 
			"\tCampaign id:            '" + campaignId           + "'.\n" + 
			"\tSearch text:            '" + searchText           + "'.\n" + 
			"\tSearch tag:             '" + searchTag            + "'.\n" + 
			"\tPlaylist id:            '" + playlistId           + "'.\n" + 
			"\tPlaylist id2:           '" + playlistId2          + "'.\n" + 
			"\tPlaylist reference id:  '" + playlistReferenceId  + "'.\n" + 
			"\tPlaylist reference id2: '" + playlistReferenceId2 + "'.\n" + 
			"\tPlayer id:              '" + playerId             + "'.\n");
		
		// Sets of Playlist/Video/Reference Ids are needed for some Media API calls that look up
		// multiple videos at once
		Set<Long> videoIds = CollectionUtils.CreateEmptyLongSet();
		if(videoId  != null){ videoIds.add(videoId);  }
		if(videoId2 != null){ videoIds.add(videoId2); }
		
		Set<String> referenceIds = CollectionUtils.CreateEmptyStringSet();
		if(referenceId  != null){ referenceIds.add(referenceId);  }
		if(referenceId2 != null){ referenceIds.add(referenceId2); }
		
		Set<Long> playlistIds = CollectionUtils.CreateEmptyLongSet();
		if(playlistId  != null){ playlistIds.add(playlistId);  }
		if(playlistId2 != null){ playlistIds.add(playlistId2); }
		
		Set<String> playlistReferenceIds = CollectionUtils.CreateEmptyStringSet();
		if(playlistReferenceId  != null){ playlistReferenceIds.add(playlistReferenceId);  }
		if(playlistReferenceId2 != null){ playlistReferenceIds.add(playlistReferenceId2); }
		
		// Video fields determine which standard fields to fill out on returned videos
		EnumSet<VideoFieldEnum> videoFields = VideoFieldEnum.CreateFullEnumSet();
		
		EnumSet<VideoFieldEnum> partialVideoFields = VideoFieldEnum.CreateEmptyEnumSet();
		partialVideoFields.add(VideoFieldEnum.ID);
		partialVideoFields.add(VideoFieldEnum.NAME);
		
		// Custom fields determine which custom fields to fill out on returned videos
		Set<String> customFields = CollectionUtils.CreateEmptyStringSet();
		
		// Playlist fields determine which fields to fill out on returned playlists
		EnumSet<PlaylistFieldEnum> playlistFields = PlaylistFieldEnum.CreateFullEnumSet();
		
		// Video state filters determine which videos to return on a FindModifiedVideos call
		Set<VideoStateFilterEnum> videoStateFilters = VideoStateFilterEnum.CreateEmptySet();
		videoStateFilters.add(VideoStateFilterEnum.PLAYABLE);
		
		// Pick a date 30 days in the past to use for time-based calls (e.g. FindModifiedVideos)
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -30);
		Long thirtyDaysAgo = cal.getTimeInMillis() / 1000 / 60; // Time in minutes, not milliseconds
		
		// And/or tags for searching
		Set<String> andTags = CollectionUtils.CreateEmptyStringSet();
		andTags.add(searchTag);
		
		Set<String> orTags = CollectionUtils.CreateEmptyStringSet();
		
		// Instantiate a ReadApi wrapper object to make the actual calls
		ReadApi readApi = new ReadApi(log);
		
		// Control when and how to retry on failed requests
		Boolean retry = true;
		Integer maxAttempts      = 10;
		Integer currentAttempt   = 0;
		Long    retryPauseMillis = 30000l; // 30 seconds
		
		// --------------------- Video Read API Methods ------------------
		
		// ****
		// **** Find Video By Id
		// ****
		retry          = true;
		currentAttempt = 0;
		if(videoId == null){
			log.info("Skipping FindVideoById test as no video id was passed in.");
			retry = false;
			currentAttempt = -1;
		}
		while(retry || (currentAttempt == 0)){
			currentAttempt++;
			
			try{
				log.info("Looking up a single video - id '" + videoId + "'.");
				Video video = readApi.FindVideoById(readToken, videoId, videoFields, customFields);
				log.info("Video: '" + video + "'.");
				log.info("----------------------------------------");
				
				// Set to false so we exit the retry loop
				retry = false;
			}
			catch(BrightcoveException be){
				handleRetry(be, currentAttempt, maxAttempts, retryPauseMillis);
			}
		}
		
		// ****
		// **** Find All Videos
		// ****
		retry          = true;
		currentAttempt = 0;
		while(retry || (currentAttempt == 0)){
			currentAttempt++;
			
			try{
				log.info("Looking up first page of all videos in account.");
				Videos videos = readApi.FindAllVideos(readToken, 100, 0, SortByTypeEnum.MODIFIED_DATE, SortOrderTypeEnum.DESC, videoFields, customFields);
				for(Video video : videos){
					log.info("Video: '" + video + "'.");
					log.info("----------------------------------------");
				}
				log.info("First page returned " + videos.size() + " videos.  Account contains " + videos.getTotalCount() + " active videos.");
				
				// Set to false so we exit the retry loop
				retry = false;
			}
			catch(BrightcoveException be){
				handleRetry(be, currentAttempt, maxAttempts, retryPauseMillis);
			}
		}
		
		// ****
		// **** Find Related Videos
		// ****
		retry          = true;
		currentAttempt = 0;
		if(videoId == null){
			log.info("Skipping FindRelatedVideos test as no video id was passed in.");
			retry = false;
			currentAttempt = -1;
		}
		while(retry || (currentAttempt == 0)){
			currentAttempt++;
			
			try{
				log.info("Looking up related videos for video id '" + videoId + "'.");
				Videos videos = readApi.FindRelatedVideos(readToken, videoId, null, 100, 0, videoFields, customFields);
				for(Video video : videos){
					log.info("Video: '" + video + "'.");
					log.info("----------------------------------------");
				}
				log.info("First page returned " + videos.size() + " videos.  Account contains " + videos.getTotalCount() + " related videos.");
				
				// Set to false so we exit the retry loop
				retry = false;
			}
			catch(BrightcoveException be){
				handleRetry(be, currentAttempt, maxAttempts, retryPauseMillis);
			}
		}
		
		// ****
		// **** Find Videos By Ids
		// ****
		retry          = true;
		currentAttempt = 0;
		if(videoIds.size() < 1){
			log.info("Skipping FindVideosByIds test as no video ids were passed in.");
			retry = false;
			currentAttempt = -1;
		}
		while(retry || (currentAttempt == 0)){
			currentAttempt++;
			
			try{
				log.info("Looking up videos by multiple ids - '" + videoIds + "'.");
				Videos videos = readApi.FindVideosByIds(readToken, videoIds, videoFields, customFields);
				for(Video video : videos){
					log.info("Video: '" + video + "'.");
					log.info("----------------------------------------");
				}
				log.info("First page returned " + videos.size() + " videos.");
				
				// Set to false so we exit the retry loop
				retry = false;
			}
			catch(BrightcoveException be){
				handleRetry(be, currentAttempt, maxAttempts, retryPauseMillis);
			}
		}
		
		// ****
		// **** Find Video By Reference Id
		// ****
		retry          = true;
		currentAttempt = 0;
		if(referenceId == null){
			log.info("Skipping FindVideoByReferenceId test as no reference id was passed in.");
			retry = false;
			currentAttempt = -1;
		}
		while(retry || (currentAttempt == 0)){
			currentAttempt++;
			
			try{
				log.info("Looking up video by reference id - '" + referenceId + "'.");
				Video video = readApi.FindVideoByReferenceId(readToken, referenceId, videoFields, customFields);
				log.info("Video: '" + video + "'.");
				log.info("----------------------------------------");
				
				// Set to false so we exit the retry loop
				retry = false;
			}
			catch(BrightcoveException be){
				handleRetry(be, currentAttempt, maxAttempts, retryPauseMillis);
			}
		}
		
		// ****
		// **** Find Videos By Reference Ids
		// ****
		retry          = true;
		currentAttempt = 0;
		if(referenceIds.size() < 1){
			log.info("Skipping FindVideosByReferenceIds test as no reference ids were passed in.");
			retry = false;
			currentAttempt = -1;
		}
		while(retry || (currentAttempt == 0)){
			currentAttempt++;
			
			try{
				log.info("Looking up multiple videos by reference ids - '" + referenceIds + "'.");
				Videos videos = readApi.FindVideosByReferenceIds(readToken, referenceIds, videoFields, customFields);
				for(Video video : videos){
					log.info("Video: '" + video + "'.");
					log.info("----------------------------------------");
				}
				log.info("First page returned " + videos.size() + " videos.");
				
				// Set to false so we exit the retry loop
				retry = false;
			}
			catch(BrightcoveException be){
				handleRetry(be, currentAttempt, maxAttempts, retryPauseMillis);
			}
		}
		
		// ****
		// **** Find Videos By User Id
		// ****
		retry          = true;
		currentAttempt = 0;
		if(userId == null){
			log.info("Skipping FindVideosByUserId test as no user id was passed in.");
			retry = false;
			currentAttempt = -1;
		}
		while(retry || (currentAttempt == 0)){
			currentAttempt++;
			
			try{
				log.info("Looking up videos by user id - '" + userId + "'.");
				Videos videos = readApi.FindVideosByUserId(readToken, userId, 100, 0, SortByTypeEnum.MODIFIED_DATE, SortOrderTypeEnum.DESC, videoFields, customFields);
				for(Video video : videos){
					log.info("Video: '" + video + "'.");
					log.info("----------------------------------------");
				}
				log.info("First page returned " + videos.size() + " videos.  Account contains " + videos.getTotalCount() + " videos for user " + userId + ".");
				
				// Set to false so we exit the retry loop
				retry = false;
			}
			catch(BrightcoveException be){
				handleRetry(be, currentAttempt, maxAttempts, retryPauseMillis);
			}
		}
		
		// ****
		// **** Find Videos By Campaign Id
		// ****
		retry          = true;
		currentAttempt = 0;
		if(campaignId == null){
			log.info("Skipping FindVideosByCampaignId test as no campaign id was passed in.");
			retry = false;
			currentAttempt = -1;
		}
		while(retry || (currentAttempt == 0)){
			currentAttempt++;
			
			try{
				log.info("Looking up videos by campaign id - '" + campaignId + "'.");
				Videos videos = readApi.FindVideosByCampaignId(readToken, campaignId, 100, 0, SortByTypeEnum.MODIFIED_DATE, SortOrderTypeEnum.DESC, videoFields, customFields);
				for(Video video : videos){
					log.info("Video: '" + video + "'.");
					log.info("----------------------------------------");
				}
				log.info("First page returned " + videos.size() + " videos.  Account contains " + videos.getTotalCount() + " videos for campaign " + campaignId + ".");
				
				// Set to false so we exit the retry loop
				retry = false;
			}
			catch(BrightcoveException be){
				handleRetry(be, currentAttempt, maxAttempts, retryPauseMillis);
			}
		}
		
		// ****
		// **** Find Modified Videos
		// ****
		retry          = true;
		currentAttempt = 0;
		while(retry || (currentAttempt == 0)){
			currentAttempt++;
			
			try{
				log.info("Looking up videos modified in the last 30 days.");
				Videos videos = readApi.FindModifiedVideos(readToken, thirtyDaysAgo, videoStateFilters, 100, 0, SortByTypeEnum.MODIFIED_DATE, SortOrderTypeEnum.DESC, videoFields, customFields);
				for(Video video : videos){
					log.info("Video: '" + video + "'.");
					log.info("----------------------------------------");
				}
				log.info("First page returned " + videos.size() + " videos.  Account contains " + videos.getTotalCount() + " videos modified.");
				
				// Set to false so we exit the retry loop
				retry = false;
			}
			catch(BrightcoveException be){
				handleRetry(be, currentAttempt, maxAttempts, retryPauseMillis);
			}
		}
		
		// ****
		// **** Find Videos By Text
		// ****
		retry          = true;
		currentAttempt = 0;
		if(searchText == null){
			log.info("Skipping FindVideosByText test as no search text was passed in.");
			retry = false;
			currentAttempt = -1;
		}
		while(retry || (currentAttempt == 0)){
			currentAttempt++;
			
			try{
				log.info("Looking up videos by text search for '" + searchText + "'.");
				Videos videos = readApi.FindVideosByText(readToken, searchText, 100, 0, videoFields, customFields);
				for(Video video : videos){
					log.info("Video: '" + video + "'.");
					log.info("----------------------------------------");
				}
				log.info("First page returned " + videos.size() + " videos.  Account contains " + videos.getTotalCount() + " matching videos.");
				
				// Set to false so we exit the retry loop
				retry = false;
			}
			catch(BrightcoveException be){
				handleRetry(be, currentAttempt, maxAttempts, retryPauseMillis);
			}
		}
		
		// ****
		// **** Find Videos By Tag
		// ****
		retry          = true;
		currentAttempt = 0;
		if(searchTag == null){
			log.info("Skipping FindVideosByTag test as no search tag was passed in.");
			retry = false;
			currentAttempt = -1;
		}
		while(retry || (currentAttempt == 0)){
			currentAttempt++;
			
			try{
				log.info("Looking up videos by tags '" + andTags + "' '" + orTags + "'.'");
				Videos videos = readApi.FindVideosByTags(readToken, andTags, orTags, 100, 0, SortByTypeEnum.MODIFIED_DATE, SortOrderTypeEnum.DESC, videoFields, customFields);
				for(Video video : videos){
					log.info("Video: '" + video + "'.");
					log.info("----------------------------------------");
				}
				log.info("First page returned " + videos.size() + " videos.  Account contains " + videos.getTotalCount() + " matching videos.");
				
				// Set to false so we exit the retry loop
				retry = false;
			}
			catch(BrightcoveException be){
				handleRetry(be, currentAttempt, maxAttempts, retryPauseMillis);
			}
		}
		
		// T.B.D.
		//   SearchVideos method
		//   *_unfiltered methods
		
		// --------------------- Playlist Read API Methods ---------------
		
		// ****
		// **** Find All Playlists
		// ****
		retry          = true;
		currentAttempt = 0;
		while(retry || (currentAttempt == 0)){
			currentAttempt++;
			
			try{
				log.info("Looking up first page of all playlists.");
				Playlists playlists = readApi.FindAllPlaylists(readToken, 100, 0, SortByTypeEnum.MODIFIED_DATE, SortOrderTypeEnum.DESC, videoFields, customFields, playlistFields);
				for(Playlist playlist : playlists){
					log.info("Playlist: '" + playlist + "'.");
					log.info("----------------------------------------");
				}
				
				// Set to false so we exit the retry loop
				retry = false;
			}
			catch(BrightcoveException be){
				handleRetry(be, currentAttempt, maxAttempts, retryPauseMillis);
			}
		}
		
		// ****
		// **** Find Playlist By Id
		// ****
		retry          = true;
		currentAttempt = 0;
		if(playlistId == null){
			log.info("Skipping FindPlaylistById test as no playlist id was passed in.");
			retry = false;
			currentAttempt = -1;
		}
		while(retry || (currentAttempt == 0)){
			currentAttempt++;
			
			try{
				log.info("Looking up playlist by playlist id - '" + playlistId + "'.");
				Playlist playlist = readApi.FindPlaylistById(readToken, playlistId, videoFields, customFields, playlistFields);
				log.info("Playlist: '" + playlist + "'.");
				log.info("----------------------------------------");
				
				// Set to false so we exit the retry loop
				retry = false;
			}
			catch(BrightcoveException be){
				handleRetry(be, currentAttempt, maxAttempts, retryPauseMillis);
			}
		}
		
		// ****
		// **** Find Playlist By Reference Id
		// ****
		retry          = true;
		currentAttempt = 0;
		if(playlistReferenceId == null){
			log.info("Skipping FindPlaylistByReferenceId test as no playlist reference id was passed in.");
			retry = false;
			currentAttempt = -1;
		}
		while(retry || (currentAttempt == 0)){
			currentAttempt++;
			
			try{
				log.info("Looking up playlist by reference id - '" + playlistReferenceId + "'.");
				Playlist playlist = readApi.FindPlaylistByReferenceId(readToken, playlistReferenceId, videoFields, customFields, playlistFields);
				log.info("Playlist: '" + playlist + "'.");
				log.info("----------------------------------------");
				
				// Set to false so we exit the retry loop
				retry = false;
			}
			catch(BrightcoveException be){
				handleRetry(be, currentAttempt, maxAttempts, retryPauseMillis);
			}
		}
		
		// ****
		// **** Find Playlists By Ids
		// ****
		retry          = true;
		currentAttempt = 0;
		if(playlistIds.size() < 1){
			log.info("Skipping FindPlaylistsByIds test as no playlist ids were passed in.");
			retry = false;
			currentAttempt = -1;
		}
		while(retry || (currentAttempt == 0)){
			currentAttempt++;
			
			try{
				log.info("Looking up multiple playlists by ids - '" + playlistIds + "'.");
				Playlists playlists = readApi.FindPlaylistsByIds(readToken, playlistIds, videoFields, customFields, playlistFields);
				for(Playlist playlist : playlists){
					log.info("Playlist: '" + playlist + "'.");
					log.info("----------------------------------------");
				}
				
				// Set to false so we exit the retry loop
				retry = false;
			}
			catch(BrightcoveException be){
				handleRetry(be, currentAttempt, maxAttempts, retryPauseMillis);
			}
		}
		
		// ****
		// **** Find Playlists By Reference Ids
		// ****
		retry          = true;
		currentAttempt = 0;
		if(playlistReferenceIds.size() < 1){
			log.info("Skipping FindPlaylistsByReferenceIds test as no playlist reference ids were passed in.");
			retry = false;
			currentAttempt = -1;
		}
		while(retry || (currentAttempt == 0)){
			currentAttempt++;
			
			try{
				log.info("Looking up multiple playlists by reference ids - '" + playlistReferenceIds + "'.");
				Playlists playlists = readApi.FindPlaylistsByReferenceIds(readToken, playlistReferenceIds, videoFields, customFields, playlistFields);
				for(Playlist playlist : playlists){
					log.info("Playlist: '" + playlist + "'.");
					log.info("----------------------------------------");
				}
				
				// Set to false so we exit the retry loop
				retry = false;
			}
			catch(BrightcoveException be){
				handleRetry(be, currentAttempt, maxAttempts, retryPauseMillis);
			}
		}
		
		// ****
		// **** Find Playlists For Player Id
		// ****
		retry          = true;
		currentAttempt = 0;
		if(playerId == null){
			log.info("Skipping FindPlaylistsForPlayerId test as no player id was passed in.");
			retry = false;
			currentAttempt = -1;
		}
		while(retry || (currentAttempt == 0)){
			currentAttempt++;
			
			try{
				log.info("Looking up playlists for player id '" + playerId + "'.");
				Playlists playlists = readApi.FindPlaylistsForPlayerId(readToken, playerId, 100, 0, videoFields, customFields, playlistFields);
				for(Playlist playlist : playlists){
					log.info("Playlist: '" + playlist + "'.");
					log.info("----------------------------------------");
				}
				
				// Set to false so we exit the retry loop
				retry = false;
			}
			catch(BrightcoveException be){
				handleRetry(be, currentAttempt, maxAttempts, retryPauseMillis);
			}
		}
	}
	
	private void handleRetry(BrightcoveException be, Integer currentAttempt, Integer maxAttempts, Long retryPauseMillis) {
		log.severe("Exception caught: " + be + ".");
		log.info("    Request failed, checking for retry.");
		
		if(be.getType().equals(ExceptionType.MEDIA_API_EXCEPTION)){
			log.info("        Exception was thrown by the Media API.  Checking to see if it was a timeout error.");
			
			Integer exceptionCode = ((MediaApiException)be).getResponseCode();
			if((exceptionCode != null) && (exceptionCode == 103)){
				log.info("            Exception was a timeout (code 103).");
				
				if(currentAttempt >= maxAttempts){
					usage("Maximum number of attempts reached.  No retry will be attempted.");
					// Execution halts
				}
				else{
					log.info("                Pausing " + retryPauseMillis + " milliseconds and retrying the request.");
					
					try{
						Thread.sleep(retryPauseMillis);
					}
					catch(InterruptedException ie){}
					// Execution continues
				}
			}
			else{
				usage("Exception was not a timeout (code " + exceptionCode + ").  No retry will be attempted.");
				// Execution halts
			}
		}
		else{
			usage("Exception was thrown by the Wrapper or a run time Java exception.  No retry will be attempted.");
			// Execution halts
		}
		
		// If we get to here, it means a retry is allowed
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
