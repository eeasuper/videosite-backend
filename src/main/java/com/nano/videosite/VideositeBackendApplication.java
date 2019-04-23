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
import org.springframework.util.FileSystemUtils;
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
        	//====FOR TESTING====ERASE IN PRODUCTION
            storageService.deleteAll();
            storageService.init();
            deleteAllUsers();
            this.playlistRepository.deleteAll();
            this.videoRepository.deleteAll();
            //=======ABOVE IS FOR TESTING ERASE IN PRODUCTION.
            Long time = new Date().getTime();
            //===Create a user:
            User u1 = new User("name", "tom", this.passwordEncoder.encode("1234"), "email");
            userRepository.save(u1);
            User u2 = new User("name1", "jerry", this.passwordEncoder.encode("1234"), "email");
            userRepository.save(u1);
            userRepository.save(u2);
            //===Create new instances of Video.
            Video v1 = new Video("yui-ura-on-"+time+".mp4", time,u1.getId(),"yui-ura-on-funny","very funny video",(long)0);
            Video v2 = new Video("test-"+time+".mp4",time,u1.getId(),"Big bunny", "What a big bunny chasing a butterfly", (long) 0);
            Video v3 = new Video("bear-"+time+".mp4",time,u2.getId(),"Video of a Bear", "곰이 물고기를 찾고 있습니다.", (long) 0);
            Video v4 = new Video("flower-"+time+".webm",time,u2.getId(),"꽃이 피고 있어요", "아름다운 꽃이 피고 있네요.", (long) 0);
            Video v5 = new Video("yui-ura-on2-"+time+".mp4",time,u2.getId(),"yui-ura-on-2", "Just a japanese animation", (long) 0);
            Video v6 = new Video("yui-ura-on4-"+time+".mp4",time,u2.getId(),"yui-ura-on-4", "Just another japanese animation", (long) 0);
            //===Convert the videos in /seeding-dir  to an upload File.
            MultipartFile video = getVideoFile(v1.getFilename());
            MultipartFile video2 = getVideoFile(v2.getFilename());
            MultipartFile video3 = getVideoFile(v3.getFilename());
            MultipartFile video4 = getVideoFile(v4.getFilename());
            MultipartFile video5 = getVideoFile(v5.getFilename());
            MultipartFile video6 = getVideoFile(v6.getFilename());
            //===Store the converted videos into /upload-dir and store thumbnails as well.
            Video video11 = uploadService.storeSeedFiles(video, u1.getId(), v1.getFilename(),v1);
            Video video22 = uploadService.storeSeedFiles(video2, u1.getId(), v2.getFilename(),v2);
            Video video33 = uploadService.storeSeedFiles(video3, u2.getId(), v3.getFilename(),v3);
            Video video44 = uploadService.storeSeedFiles(video4, u2.getId(), v4.getFilename(),v4);
            Video video55 = uploadService.storeSeedFiles(video5, u1.getId(), v5.getFilename(),v5);
            Video video66 = uploadService.storeSeedFiles(video6, u1.getId(), v6.getFilename(),v6);
            //===Create the playlists for the videos.
            Playlist p1 = new Playlist(u1.getId(), "What a playlist", time);
            Map<Integer, Video> m1 = new HashMap<Integer, Video>();
            m1.put(1, video22);
            m1.put(2, video11);
            m1.put(3, video55);
            p1.setPlaylist(m1);
            Playlist p2 = new Playlist(u2.getId(), "A good playlist", time);
            Map<Integer, Video> m2 = new HashMap<Integer, Video>();
            m2.put(1, video33);
            p2.setPlaylist(m2);
            playlistRepository.save(p1);            
            playlistRepository.save(p2);
        };
    }
    
    //Important method to seed video files. Makes video file into an 'uploaded' file.
    private MultipartFile getVideoFile(String videoName) throws IOException {
//    	Path path = Paths.get("upload-dir/upload-dir-1/"+videoName);
    	String realVideoName;
    	if(videoName.contains("webm")) {
    		realVideoName = videoName.substring(0,videoName.lastIndexOf("-"))+".webm";
    	}else{
    		realVideoName = videoName.substring(0,videoName.lastIndexOf("-"))+".mp4";
    	}
    	File file = new File("seeding-dir/"+realVideoName);
    	FileItem fileItem = new DiskFileItem(realVideoName, Files.probeContentType(file.toPath()), false, realVideoName, (int) file.length(), file.getParentFile());
    	try {
    		IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
    	} catch (final IOException e) {
    	}
    	MultipartFile result = new CommonsMultipartFile(fileItem);
    	return result;
    }
    
    private void deleteAllUsers() {
//        FileSystemUtils.deleteRecursively(rootLocation.toFile());
//        Path thumbnailDir = Paths.get("thumbnail-dir");
//        FileSystemUtils.deleteRecursively(thumbnailDir.toFile());
    	userRepository.deleteAll();
    }
}
