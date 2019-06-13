package com.nano.videosite.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nano.videosite.exceptions.ElementNotFoundException;
import com.nano.videosite.models.Video;
import com.nano.videosite.repositories.UserRepository;
import com.nano.videosite.repositories.VideoRepository;
import com.nano.videosite.storage.FileSystemStorageService;

@Service
public class VideoService {
	@Autowired
	VideoRepository videoRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	FileSystemStorageService storageService;
	
	public List<Video> all(Long userId){
		List<Video> list =  this.videoRepository.findByUploaderIdOrderByDateDesc(userId).orElseThrow(()->new ElementNotFoundException());
		list.forEach((vid)->{
			String username = userRepository.findById(vid.getUploaderId()).orElseThrow(()->new ElementNotFoundException()).getUsername();
			vid.setUploaderUsername(username);
		});
		return list;
	}
	
	public Video add(Video video) {
		Video vid = videoRepository.findById(video.getId()).orElseThrow(()->new ElementNotFoundException());
		vid.setDescription(video.getDescription());
		vid.setTitle(video.getTitle());
		return videoRepository.save(vid);
	}
	
//	public String getThumbnail(Video video) throws IOException {
//		String filename = video.getFilename().substring(0,video.getFilename().lastIndexOf("."));
//		 // open image
//		File imgPath = new File("thumbnail-dir/thumbnail-dir-"+video.getUploaderId()+"/"+filename+".png");
//		System.out.println(imgPath.toString());
//		BufferedImage bufferedImage = ImageIO.read(imgPath);
//
//		 // get DataBufferBytes from Raster
//		WritableRaster raster = bufferedImage.getRaster();
//		DataBufferByte data   = (DataBufferByte) raster.getDataBuffer();
//		return Base64.encodeBase64String(data.getData());
//	}
	
//	public Resource getOneThumbnail(Long videoId) throws IOException {
//		Video video = videoRepository.findById(videoId).orElseThrow(()->new ElementNotFoundException());
//		Resource image = storageService.loadThumbnailAsResource(video);
//		return image;
//	}
	
	public byte[] getOneThumbnail(Long videoId) throws IOException{
		Video video = videoRepository.findById(videoId).orElseThrow(()->new ElementNotFoundException());
		String filename = video.getFilename().substring(0, video.getFilename().lastIndexOf(".")) + ".png";
//		Path file = Paths.get("thumbnail-dir/thumbnail-dir-"+video.getUploaderId().toString()).resolve(filename);
		String file = "thumbnail-dir/thumbnail-dir-"+video.getUploaderId().toString()+"/"+filename;
		byte[] fileContent = FileUtils.readFileToByteArray(new File(file));
		InputStream in = new ByteArrayInputStream(fileContent);
		return IOUtils.toByteArray(in);
	}
	 
	
	public List<Video> getRandomVideoList(){
		List<Video> videoList = videoRepository.getRandomList();
		videoList.forEach((vid)->{
			String username = userRepository.findById(vid.getUploaderId()).orElseThrow(()->new ElementNotFoundException()).getUsername();
			vid.setUploaderUsername(username);
			//use method getThumbnail if I think I can make front-end use the base64 for image.
//			vid.setThumbnailImage(storageService.loadThumbnailAsResource(vid));
		});
		return videoList;
	}
	
	public List<Video> getRecentVideoList(Long uploaderId){
		List<Video> videoList = videoRepository.findFirst6ByUploaderIdOrderByDateDesc(uploaderId);
		videoList.forEach((vid)->{
			String username = userRepository.findById(vid.getUploaderId()).orElseThrow(()->new ElementNotFoundException()).getUsername();
			vid.setUploaderUsername(username);
//			vid.setThumbnailImage(storageService.loadThumbnailAsResource(vid));
		});
		return videoList;
	}
	
	public Video getVideoDescription(Long videoId) {
		Video vid= videoRepository.findById(videoId).orElseThrow(()->new ElementNotFoundException());
		String username = userRepository.findById(vid.getUploaderId()).orElseThrow(()->new ElementNotFoundException()).getUsername();
		vid.setUploaderUsername(username);
		return vid;
	}
	
	public List<Video> getSearch(String query){
		List<Video> videoList = videoRepository.findByTitleContainingIgnoreCase(query);
		videoList.forEach((vid)->{
			String username = userRepository.findById(vid.getUploaderId()).orElseThrow(()->new ElementNotFoundException()).getUsername();
			vid.setUploaderUsername(username);
		});
		return videoList;
	}
}
