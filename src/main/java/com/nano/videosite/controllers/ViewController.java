package com.nano.videosite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nano.videosite.models.ViewVideo;
import com.nano.videosite.services.ViewService;

@RestController
public class ViewController {
	
    @Autowired
    ViewService viewService;
    
	
    @RequestMapping(method=RequestMethod.POST, value="/addViewCount", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<HttpStatus> addViewCount(@RequestBody ViewVideo view){
    	viewService.addViewCount(view);
    	return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @RequestMapping(method=RequestMethod.GET, value="/getViewCount/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Long> getViewCount(@PathVariable("id") Long videoId){
    	Long viewCount = viewService.getViewCount(videoId);
    	return ResponseEntity.ok(viewCount);
    }
    
}
