package com.nano.videosite.controllers;

import java.io.IOException;
import java.util.List;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nano.videosite.models.Video;
import com.nano.videosite.services.VideoService;

@RestController
public class VideoController {
	
	@Autowired
	VideoService videoService;
	
	@RequestMapping(method=RequestMethod.POST, value="/video", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Video> add(@RequestBody Video video){
		//Video is uploaded in FileUploadController. This is to save title and descriptions for the uploaded video.
		Video vid = videoService.add(video);
		return ResponseEntity.status(HttpStatus.CREATED).body(vid);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/video/{id}/thumbnail")
	public ResponseEntity<Resource> getOneThumbnail(@PathVariable("id") Long videoId) throws IOException{
		Resource image = videoService.getOneThumbnail(videoId);
	    return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + image.getFilename() + "\"").contentType(MediaType.IMAGE_JPEG).body(image);
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
}
