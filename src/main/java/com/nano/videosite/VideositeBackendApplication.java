package com.nano.videosite;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.nano.videosite.models.Playlist;
import com.nano.videosite.models.User;
import com.nano.videosite.models.Video;
import com.nano.videosite.repositories.PlaylistRepository;
import com.nano.videosite.repositories.UserRepository;
import com.nano.videosite.repositories.VideoRepository;
import com.nano.videosite.services.PlaylistService;
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
	
    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
            User u1 = new User("name", "username", "password", "email");
            userRepository.save(u1);
            Video v1 = new Video("yui-ura-on.mp4");
            Video v2 = new Video("test.mp4");
            videoRepository.save(v1);
            //you get this error if you don't save: object references an unsaved transient instance
            videoRepository.save(v2);
            Map m1 = new HashMap();
            m1.put(1, v2);
            m1.put(2, v1);
            Playlist p1 = new Playlist(u1.getId(),m1);
            playlistRepository.save(p1);
            System.out.println(v1.getFilename());
            System.out.println(u1.getId());
            System.out.println(v1.getId());
            VideoThumbnails test = new VideoThumbnails("ffmpegApp");
            test.main();
            
        };
    }
}
