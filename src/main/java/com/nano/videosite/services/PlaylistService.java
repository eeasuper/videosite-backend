package com.nano.videosite.services;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;

import com.nano.videosite.models.Playlist;
import com.nano.videosite.models.PlaylistVideo;
import com.nano.videosite.models.Video;
import com.nano.videosite.repositories.PlaylistRepository;
import com.nano.videosite.repositories.VideoRepository;

@Service
public class PlaylistService {

	@Autowired
	PlaylistRepository playlistRepository;
	@Autowired
	VideoRepository videoRepository;
	
	public Playlist add(Playlist newPlaylist) {
		return playlistRepository.save(newPlaylist);
	}
	
	public Set<Map<Integer,Video>> allPlaylists(Long userId){
		List<Playlist> playlist = playlistRepository.findByUserId(userId).orElseThrow();
		Set set = new HashSet();
		playlist.forEach((val)->{
			set.add(val.getPlaylist());
		});
		return set;
	}
	
	public Map<Integer, Video> onePlaylist(Long playlistId){
		Playlist playlist =playlistRepository.findById(playlistId).orElseThrow();
		return playlist.getPlaylist();
	}
	
	public Map<Integer, Video> editOrder( Long playlistId, Map<Integer,Video> newPlaylist){
		Playlist playlist =playlistRepository.findById(playlistId).orElseThrow();
		playlist.setPlaylist(newPlaylist);
		return playlistRepository.save(playlist).getPlaylist();
	}
	
	public Playlist editTitle(Long playlistId, Playlist newPlaylist){
		Playlist playlist =playlistRepository.findById(playlistId).orElseThrow();
		playlist.setTitle(newPlaylist.getTitle());
		return playlistRepository.save(playlist);
	}
	
	public Playlist editAddVideo(Long playlistId, List<Video> newVideo) {
		Playlist playlist =playlistRepository.findById(playlistId).orElseThrow();
		Map<Integer, Video> playlistList = playlist.getPlaylist();
		newVideo.forEach((val)->{
			Video video = videoRepository.findById(val.getId()).orElseThrow();
			playlistList.put(playlistList.size()+1, video);
		});
		playlist.setPlaylist(playlistList);
		return playlistRepository.save(playlist);
	}
	
	public void deletePlaylist(Long playlistId) {
		playlistRepository.deleteById(playlistId);
		
	}
}
