package com.nano.videosite.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nano.videosite.models.Playlist;
import com.nano.videosite.models.Video;
import com.nano.videosite.repositories.PlaylistRepository;

@Service
public class PlaylistService {

	@Autowired
	PlaylistRepository playlistRepository;
	
	public Map<Integer,Video> one(Long userId, Long playlistId){
		Playlist playlist =playlistRepository.findByIdAndUserId(playlistId, userId);
		return playlist.getPlaylist();
	}
}
