package com.nano.videosite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.nano.videosite.controllers.FileUploadController;
import com.nano.videosite.models.Playlist;
import com.nano.videosite.models.User;
import com.nano.videosite.models.Video;
import com.nano.videosite.repositories.PlaylistRepository;
import com.nano.videosite.repositories.UserRepository;
import com.nano.videosite.repositories.VideoRepository;
import com.nano.videosite.services.PlaylistService;
import com.nano.videosite.storage.FileSystemStorageService;
import com.nano.videosite.storage.StorageProperties;
import com.nano.videosite.storage.StorageService;
import com.nano.videosite.thumbnails.VideoThumbnails;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class VideositeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideositeBackendApplication.class, args);
	}
	
	@Autowired
	PlaylistService playlistService;
	@Autowired
	PlaylistRepository playlistRepository;
	@Autowired
	VideoRepository videoRepository;
	@Autowired
	UserRepository userRepository;	
	@Autowired
	BCryptPasswordEncoder passwordEncoder;
	@Autowired
	FileSystemStorageService uploadService;
    @Bean
    CommandLineRunner init(StorageService storageService) {
    	//==== Set up seeding ====
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
            Long time = new Date().getTime();
            //===Create a user:
            User u1 = new User("name", "username", this.passwordEncoder.encode("password"), "email");
            userRepository.save(u1);
            //===Create new instances of Video.
            Video v1 = new Video("yui-ura-on-"+time+".mp4", time,u1.getId(),"yui-ura-on-funny","very funny video",(long)0);
            Video v2 = new Video("test-"+time+".mp4",time,u1.getId(),"test-video", "just a test description", (long) 0);
            //===Convert the videos in /seeding-dir  to an upload File.
            MultipartFile video = getVideoFile(v1.getFilename());
            MultipartFile video2 = getVideoFile(v2.getFilename());
            //===Store the converted videos into /upload-dir and store thumbnails as well.
            Video video11 = uploadService.storeSeedFiles(video, u1.getId(), v1.getFilename(),v1);
            Video video22 = uploadService.storeSeedFiles(video2, u1.getId(), v2.getFilename(),v2);
            //===Create the playlists for the videos.
            Playlist p1 = new Playlist(u1.getId(), "testTitle", time);
            Map<Integer, Video> m1 = new HashMap<Integer, Video>();
            m1.put(1, video22);
            m1.put(2, video11);
            p1.setPlaylist(m1);
            playlistRepository.save(p1);            
        };
    }
    
    //Important method to seed video files. Makes video file into an 'uploaded' file.
    private MultipartFile getVideoFile(String videoName) throws IOException {
//    	Path path = Paths.get("upload-dir/upload-dir-1/"+videoName);
    	String realVideoName = videoName.substring(0,videoName.lastIndexOf("-"))+".mp4"; 
    	File file = new File("seeding-dir/"+realVideoName);
    	FileItem fileItem = new DiskFileItem(realVideoName, Files.probeContentType(file.toPath()), false, realVideoName, (int) file.length(), file.getParentFile());
    	try {
    		IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
    	} catch (final IOException e) {
    	}
    	MultipartFile result = new CommonsMultipartFile(fileItem);
    	return result;
    }
}
