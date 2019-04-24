package com.nano.videosite.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nano.videosite.models.Playlist;
import com.nano.videosite.models.Video;
import com.nano.videosite.services.JWTAuthenticationService;
import com.nano.videosite.services.PlaylistService;

@RestController
public class PlaylistController {
	
	@Autowired
	PlaylistService playlistService;
	@Autowired
	JWTAuthenticationService jwtService;
	
	@RequestMapping(method=RequestMethod.GET, value="/user/{id}/playlist", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Set<Playlist>> allPlaylists(@PathVariable("id") Long userId){
		Set<Playlist> playlists = playlistService.allPlaylists(userId);
		return ResponseEntity.ok(playlists);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/playlist", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Playlist> add(@RequestBody Playlist newPlaylist, @RequestHeader("Authorization") String authorization) {
		if(jwtService.isAuthenticated(authorization)) {
			Playlist playlist = playlistService.add(newPlaylist);
			return ResponseEntity.status(HttpStatus.CREATED).body(playlist);
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/playlist/list/{id}", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Map<Integer, Video>> onePlaylistList(@PathVariable("id") Long playlistId) {
		Map<Integer, Video> playlist = playlistService.onePlaylistList(playlistId);
		return ResponseEntity.ok(playlist);
	}
	@RequestMapping(method=RequestMethod.GET, value="/playlist/{id}", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Playlist> onePlaylist(@PathVariable("id") Long playlistId) {
		Playlist playlist = playlistService.onePlaylist(playlistId);
		return ResponseEntity.ok(playlist);
	}
	@RequestMapping(method=RequestMethod.GET, value="/playlist/{id}/thumbnail", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<byte[]> onePlaylistThumbnail(@PathVariable("id") Long playlistId) throws IOException {
		byte[] image = playlistService.onePlaylistThumbnail(playlistId);
	    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/playlist/{id}/edit/order-change", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Map<Integer, Video>> editOrder(@RequestBody Map<Integer,Video> newPlaylist, @PathVariable("id") Long playlistId, @RequestHeader("Authorization") String authorization){
		if(jwtService.isAuthenticated(authorization)) {
			Map<Integer, Video> playlist = playlistService.editOrder(playlistId,newPlaylist);
			return ResponseEntity.ok(playlist);
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/playlist/{id}/edit/title-change", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Playlist> editTitle(@RequestBody Playlist newPlaylist,@PathVariable("id") Long playlistId, @RequestHeader("Authorization") String authorization){
		if(jwtService.isAuthenticated(authorization)) {
			Playlist playlist = playlistService.editTitle(playlistId, newPlaylist);
			return ResponseEntity.ok(playlist);
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/playlist/{id}/edit/add-video", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Playlist> editAddVideo(@RequestBody List<Video> newVideo,@PathVariable("id") Long playlistId, @RequestHeader("Authorization") String authorization){
		if(jwtService.isAuthenticated(authorization)) {
			Playlist playlist = playlistService.editAddVideo(playlistId, newVideo);
			return ResponseEntity.ok(playlist);
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/playlist/{id}")
	public ResponseEntity<?> deletePlaylist(@PathVariable("id") Long playlistId,@RequestHeader("Authorization") String authorization){
		if(jwtService.isAuthenticated(authorization)) {
			playlistService.deletePlaylist(playlistId);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}
}
