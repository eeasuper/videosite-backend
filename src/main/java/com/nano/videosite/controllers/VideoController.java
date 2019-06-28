package com.nano.videosite.controllers;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nano.videosite.models.Video;
import com.nano.videosite.services.JWTAuthenticationService;
import com.nano.videosite.services.VideoService;

@RestController
public class VideoController {
	
	@Autowired
	VideoService videoService;
	@Autowired
	JWTAuthenticationService jwtService;
	@RequestMapping(method=RequestMethod.GET, value="/video/{id}/all", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<Video>> all(@PathVariable("id") Long userId){
		List<Video> videos = this.videoService.all(userId);
		return ResponseEntity.ok(videos);
	}
	@RequestMapping(method=RequestMethod.PUT, value="/video", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Video> add(@RequestBody Video video){
		//Video is uploaded in FileUploadController. This is to save title and descriptions for the uploaded video.
		Video vid = videoService.add(video);
		if(vid == null) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
		return ResponseEntity.ok(vid);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/video/{id}/thumbnail", produces= MediaType.IMAGE_PNG_VALUE)
	public @ResponseBody ResponseEntity<byte[]> getOneThumbnail(@PathVariable("id") Long videoId) throws IOException{
		byte[] image = videoService.getOneThumbnail(videoId);
	    return ResponseEntity.ok().body(image);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/video/random", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<Video>> getRandomVideoList() throws IOException{
		List<Video> videoList = videoService.getRandomVideoList();
	    return ResponseEntity.ok().body(videoList);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/video/{id}/recent", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<Video>> getRecentVideoList(@PathVariable("id") Long uploaderId) throws IOException{
		List<Video> videoList = videoService.getRecentVideoList(uploaderId);
	    return ResponseEntity.ok().body(videoList);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/video/{id}", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Video> getVideoDescription(@PathVariable("id") Long videoId) throws IOException{
		Video video = videoService.getVideoDescription(videoId);
	    return ResponseEntity.ok().body(video);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/search/{query}", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<List<Video>> getSearch(@PathVariable("query") String query) throws IOException{
		List<Video> videoList = videoService.getSearch(query);
	    return ResponseEntity.ok().body(videoList);
	}
}
