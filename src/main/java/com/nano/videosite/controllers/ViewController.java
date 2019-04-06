package com.nano.videosite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nano.videosite.models.Video;
import com.nano.videosite.models.ViewVideo;
import com.nano.videosite.services.ViewService;

@RestController
public class ViewController {
	
    @Autowired
    ViewService viewService;
    
	
    @RequestMapping(method=RequestMethod.POST, value="/addViewCount", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<HttpStatus> addViewCount(@RequestBody ViewVideo view){
    	ViewVideo v = viewService.addViewCount(view);
    	return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
//    @RequestMapping(method=RequestMethod.GET, value="/debounceView", produces = {MediaType.APPLICATION_JSON_VALUE})
//    public ResponseEntity<ViewVideo> debounceView(@RequestBody ViewVideo view){
//    	return null;
//    }
    
    //get view count of one specific video
    @RequestMapping(method=RequestMethod.GET, value="/getViewCount", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity getViewCount(@RequestBody Video video){
    	Long viewCount = viewService.getViewCount(video);
    	return ResponseEntity.ok().build();
    }
    
}
