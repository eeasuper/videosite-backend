package com.nano.videosite.services;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;

import com.nano.videosite.exceptions.ElementNotFoundException;
import com.nano.videosite.models.Playlist;
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
	
//	public Set<Map<Integer,Video>> allPlaylists(Long userId){
//		List<Playlist> playlist = playlistRepository.findByUserId(userId).orElseThrow(()->new ElementNotFoundException());
//		Set set = new HashSet();
//		playlist.forEach((val)->{
//			set.add(val.getPlaylist());
//		});
//		return set;
//	}
	public Set<Playlist> allPlaylists(Long userId){
		List<Playlist> playlist = playlistRepository.findByUserId(userId).orElseThrow(()->new ElementNotFoundException());
		Set set = new HashSet();
		playlist.forEach((val)->{
			set.add(val);
		});
		return set;
	}
	
	public Playlist onePlaylist(Long playlistId) {
		Playlist playlist =playlistRepository.findById(playlistId).orElseThrow(()->new ElementNotFoundException());
		return playlist;
	}
	
	public Map<Integer, Video> onePlaylistList(Long playlistId){
		Playlist playlist =playlistRepository.findById(playlistId).orElseThrow(()->new ElementNotFoundException());
		return playlist.getPlaylist();
	}
	
	public byte[] onePlaylistThumbnail(Long playlistId) throws IOException {
		Playlist playlist =playlistRepository.findById(playlistId).orElseThrow(()->new ElementNotFoundException());
		Video video = playlist.getPlaylist().get(1);
		String filename = video.getFilename().substring(0,video.getFilename().lastIndexOf("."));
		 // open image
		File imgPath = new File("thumbnail-dir/thumbnail-dir-"+video.getUploaderId()+"/"+filename+".png");
		System.out.println(imgPath.toString());
		BufferedImage bufferedImage = ImageIO.read(imgPath);

		 // get DataBufferBytes from Raster
		WritableRaster raster = bufferedImage.getRaster();
		DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();

		return data.getData();
	}
	
	public Map<Integer, Video> editOrder( Long playlistId, Map<Integer,Video> newPlaylist){
		Playlist playlist =playlistRepository.findById(playlistId).orElseThrow(()->new ElementNotFoundException());
		playlist.getPlaylist().forEach((key, val)->{
			System.out.println(key);
			System.out.println(val.getFilename());
		});
		playlist.setPlaylist(newPlaylist);
//		newPlaylist.forEach((val,v)->{
//			System.out.println(val);
//			System.out.println();
//		});
		playlist.getPlaylist().forEach((key, val)->{
			System.out.println(key);
			System.out.println(val.getFilename());
		});
		return playlistRepository.save(playlist).getPlaylist();
	}
	
	public Playlist editTitle(Long playlistId, Playlist newPlaylist){
		Playlist playlist =playlistRepository.findById(playlistId).orElseThrow(()->new ElementNotFoundException());
		playlist.setTitle(newPlaylist.getTitle());
		return playlistRepository.save(playlist);
	}
	
	public Playlist editAddVideo(Long playlistId, List<Video> newVideo) {
		Playlist playlist =playlistRepository.findById(playlistId).orElseThrow(()->new ElementNotFoundException());
		Map<Integer, Video> playlistList = playlist.getPlaylist();
		newVideo.forEach((val)->{
			Video video = videoRepository.findById(val.getId()).orElseThrow(()->new ElementNotFoundException());
			playlistList.put(playlistList.size()+1, video);
		});
		playlist.setPlaylist(playlistList);
		return playlistRepository.save(playlist);
	}
	
	public void deletePlaylist(Long playlistId) {
		playlistRepository.deleteById(playlistId);
	}
}
