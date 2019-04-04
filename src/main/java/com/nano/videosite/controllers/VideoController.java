package com.nano.videosite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nano.videosite.models.Video;
import com.nano.videosite.services.VideoService;

@RestController
public class VideoController {
	
	@Autowired
	VideoService videoService;
	
	@RequestMapping(method=RequestMethod.POST, value="/video", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Video> add(@RequestBody Video video){
		//I need to first upload the video from FileUploadController, then save it using the filename.
		videoService.add();
		return null;
	}
}
