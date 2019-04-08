package com.nano.videosite.services;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import com.nano.videosite.exceptions.ElementNotFoundException;
import com.nano.videosite.models.Video;
import com.nano.videosite.repositories.VideoRepository;
import com.nano.videosite.storage.FileSystemStorageService;

@Service
public class VideoService {
	@Autowired
	VideoRepository videoRepository;
	
	@Autowired
	FileSystemStorageService storageService;
	
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
	
	public Resource getOneThumbnail(Long videoId) throws IOException {
		Video video = videoRepository.findById(videoId).orElseThrow(()->new ElementNotFoundException());
		Resource image = storageService.loadThumbnailAsResource(video);
		return image;
	}
	
	public List<Video> getRandomVideoList(){
		List<Video> videoList = videoRepository.getRandomList();
		videoList.forEach((vid)->{
			//use method getThumbnail if I think I can make front-end use the base64 for image.
//			vid.setThumbnailImage(storageService.loadThumbnailAsResource(vid));
		});
		return videoList;
	}
	
	public List<Video> getRecentVideoList(Long uploaderId){
		List<Video> videoList = videoRepository.findFirst6ByUploaderIdOrderByDateAsc(uploaderId);
		videoList.forEach((vid)->{
//			vid.setThumbnailImage(storageService.loadThumbnailAsResource(vid));
		});
		return videoList;
	}
	
	public Video getVideoDescription(Long videoId) {
		return videoRepository.findById(videoId).orElseThrow(()->new ElementNotFoundException());
		
	}
}
