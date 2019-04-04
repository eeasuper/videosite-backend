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
import com.nano.videosite.repositories.PlaylistVideoRepository;
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
	PlaylistVideoRepository playlistVidRepository;
	@Autowired
	FileSystemStorageService uploadService;
    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
            User u1 = new User("name", "username", this.passwordEncoder.encode("password"), "email");
            userRepository.save(u1);
            Video v1 = new Video("yui-ura-on.mp4", new Date().getTime());
            Video v2 = new Video("test.mp4",new Date().getTime());
            videoRepository.save(v1);
            videoRepository.save(v2);
            Map m1 = new HashMap();
            m1.put(1, v2);
            m1.put(2, v1);
//            Set<Long> s1 = new HashSet<Long>();
            Playlist p1 = new Playlist(u1.getId(), "testTitle", new Date().getTime());
//            PlaylistVideo pv1 = playlistVidRepository.save(new PlaylistVideo(v1.getId(), p1.getId(),1));
//            PlaylistVideo pv2 = playlistVidRepository.save(new PlaylistVideo(v2.getId(), p1.getId(),2));
//            s1.add(pv1.getId());
//            s1.add(pv2.getId());
            MultipartFile video = getVideoFile(v1.getFilename());
            MultipartFile video2 = getVideoFile(v2.getFilename());
            uploadService.store(video, u1.getId());
            uploadService.store(video2, u1.getId());
            p1.setPlaylist(m1);
            playlistRepository.save(p1);
            VideoThumbnails test = new VideoThumbnails("ffmpegApp");
            test.main();
            
        };
    }
    
    private MultipartFile getVideoFile(String videoName) throws IOException {
//    	Path path = Paths.get("upload-dir/upload-dir-1/"+videoName);
    	File file = new File("seeding-dir/"+videoName);
    	FileItem fileItem = new DiskFileItem(videoName+"-"+new Date().getTime(), Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
    	try {
    		IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
    	} catch (final IOException e) {
    	}
    	MultipartFile result = new CommonsMultipartFile(fileItem);
    	return result;
    }
}
