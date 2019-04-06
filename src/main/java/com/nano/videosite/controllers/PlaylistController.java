package com.nano.videosite.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nano.videosite.models.Playlist;
import com.nano.videosite.models.Video;
import com.nano.videosite.services.PlaylistService;

@RestController
public class PlaylistController {
	
	@Autowired
	PlaylistService playlistService;
	
	@RequestMapping(method=RequestMethod.GET, value="/user/{id}/playlist", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Set<Map<Integer,Video>>> allPlaylists(@PathVariable("id") Long userId){
		Set<Map<Integer,Video>> playlists = playlistService.allPlaylists(userId);
		return ResponseEntity.ok(playlists);
	}
	
	@RequestMapping(method=RequestMethod.POST, value="/playlist", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Playlist> add(@RequestBody Playlist newPlaylist) {
		Playlist playlist = playlistService.add(newPlaylist);
		return ResponseEntity.status(HttpStatus.CREATED).body(playlist);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/playlist/{id}", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Map<Integer, Video>> onePlaylist(@PathVariable("id") Long playlistId) {
		Map<Integer, Video> playlist = playlistService.onePlaylist(playlistId);
		return ResponseEntity.ok(playlist);
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/playlist/{id}/thumbnail", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<byte[]> onePlaylistThumbnail(@PathVariable("id") Long playlistId) throws IOException {
		byte[] image = playlistService.onePlaylistThumbnail(playlistId);
	    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/playlist/{id}/edit/order-change", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Map<Integer, Video>> editOrder(@RequestBody Map<Integer,Video> newPlaylist,@PathVariable("id") Long userId, @PathVariable("id2") Long playlistId){
		Map<Integer, Video> playlist = playlistService.editOrder(playlistId,newPlaylist);
		//Or I could return 204, with no Response Body. For Testing, return a body.
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//		return ResponseEntity.ok(playlist);
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="/playlist/{id}/edit/title-change", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Playlist> editTitle(@RequestBody Playlist newPlaylist,@PathVariable("id") Long playlistId){
		Playlist playlist = playlistService.editTitle(playlistId, newPlaylist);
		//Or I could return 204, with no Response Body. For Testing, return a body.
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//		return ResponseEntity.ok(playlist);
	}
	
	@RequestMapping(method = RequestMethod.POST, value="/playlist/{id}/edit/add-video", produces={MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Playlist> editAddVideo(@RequestBody List<Video> newVideo,@PathVariable("id") Long playlistId){
		Playlist playlist = playlistService.editAddVideo(playlistId, newVideo);
		//Or I could return 204, with no Response Body. For Testing, return a body.
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
//		return ResponseEntity.ok(playlist);
	}
	
	@RequestMapping(method = RequestMethod.DELETE, value="/playlist/{id}")
	public void deletePlaylist(@PathVariable("id") Long playlistId){
		playlistService.deletePlaylist(playlistId);
	}
}
//Maybe I should use the below code for JWT authorization?
//@RequestHeader(value="Authorization") String authorization,