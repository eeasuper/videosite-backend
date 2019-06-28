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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.nano.videosite.configurations.MyUserPrincipal;
import com.nano.videosite.exceptions.ElementNotFoundException;
import com.nano.videosite.models.Playlist;
import com.nano.videosite.models.Video;
import com.nano.videosite.repositories.PlaylistRepository;
import com.nano.videosite.repositories.UserRepository;
import com.nano.videosite.repositories.VideoRepository;

@Service
public class PlaylistService {

	@Autowired
	PlaylistRepository playlistRepository;
	@Autowired
	VideoRepository videoRepository;
	@Autowired
	UserRepository userRepository;

	
	public Playlist add(Playlist newPlaylist) {
		if(!verifyUserIdIsUsers(newPlaylist.getUserId())){
			return null;
		}
		String username = userRepository.findById(newPlaylist.getUserId()).orElseThrow(()->new ElementNotFoundException()).getUsername();
		newPlaylist.setUsername(username);
		return playlistRepository.save(newPlaylist);
	}
	
	public Set<Playlist> allPlaylists(Long userId){
		List<Playlist> playlist = playlistRepository.findByUserId(userId).orElseThrow(()->new ElementNotFoundException());
//		playlist.forEach((val)->{
//			String username = userRepository.findById(val.getUserId()).orElseThrow(()->new ElementNotFoundException()).getUsername();
//			val.setUsername(username);
//		});
		Set<Playlist> set = new HashSet<Playlist>();
		playlist.forEach((val)->{
			set.add(val);
		});
		return set;
	}
	
	public Playlist onePlaylist(Long playlistId) {
		Playlist playlist =playlistRepository.findById(playlistId).orElseThrow(()->new ElementNotFoundException());
//		playlist.getPlaylist().forEach((i,vid)->{
//			String username = userRepository.findById(vid.getUploaderId()).orElseThrow(()->new ElementNotFoundException()).getUsername();
//			vid.setUploaderUsername(username);
//		});
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
		
		BufferedImage bufferedImage = ImageIO.read(imgPath);

		 // get DataBufferBytes from Raster
		WritableRaster raster = bufferedImage.getRaster();
		DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();

		return data.getData();
	}
	
	public Map<Integer, Video> editOrder( Long playlistId, Map<Integer,Video> newPlaylist){
		if(!verifyUserIdIsUsers(newPlaylist.get(1).getUploaderId())){
			return null;
		}
		Playlist playlist =playlistRepository.findById(playlistId).orElseThrow(()->new ElementNotFoundException());
		playlist.setPlaylist(newPlaylist);
		return playlistRepository.save(playlist).getPlaylist();
	}
	
	public Playlist editTitle(Long playlistId, Playlist newPlaylist){
		if(!verifyUserIdIsUsers(newPlaylist.getUserId())){
			return null;
		}
		Playlist playlist =playlistRepository.findById(playlistId).orElseThrow(()->new ElementNotFoundException());
		playlist.setTitle(newPlaylist.getTitle());
		return playlistRepository.save(playlist);
	}
	
	public Playlist editAddVideo(Long playlistId, List<Video> newVideo) {
		/*Add video to an already existing playlist.*/
		if(!verifyUserIdIsUsers(newVideo.get(0).getUploaderId())){
			return null;
		}
		Playlist playlist =playlistRepository.findById(playlistId).orElseThrow(()->new ElementNotFoundException());
		Map<Integer, Video> playlistList = playlist.getPlaylist();
		newVideo.forEach((val)->{
			/*Add the new videos to the end of the playlist.*/
			Video video = videoRepository.findById(val.getId()).orElseThrow(()->new ElementNotFoundException());
			playlistList.put(playlistList.size()+1, video);
		});
		playlist.setPlaylist(playlistList);
		return playlistRepository.save(playlist);
	}
	
	public boolean deletePlaylist(Long playlistId,Long userId) {
		if(!verifyUserIdIsUsers(userId)){
			return false;
		}
		playlistRepository.deleteById(playlistId);
		return true;
	}
	
	private boolean verifyUserIdIsUsers(Long userId) {
		MyUserPrincipal userPrincipal = (MyUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(userPrincipal.getId() != userId) {
			return false;
		}
		return true;
	}
}