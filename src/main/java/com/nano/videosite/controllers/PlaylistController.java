package com.nano.videosite.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nano.videosite.models.Playlist;
import com.nano.videosite.models.Video;
import com.nano.videosite.services.PlaylistService;

@RestController
public class PlaylistController {
	
	@Autowired
	PlaylistService playlistService;
	
	@RequestMapping(method=RequestMethod.GET, value="/playlist/{id}/{id2}", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Map<Integer, Video>> one(@PathVariable("id") Long userId, @PathVariable("id2") Long playlistId) {
		Map<Integer, Video> playlist = playlistService.one(userId, playlistId);
		return ResponseEntity.ok(playlist);
	}
}
