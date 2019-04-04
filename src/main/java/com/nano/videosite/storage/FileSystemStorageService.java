package com.nano.videosite.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nano.videosite.models.Video;
import com.nano.videosite.repositories.VideoRepository;

@Service
public class FileSystemStorageService implements StorageService{
    private final Path rootLocation;
    private final String rootLoc = "upload-dir";
    //https://stackoverflow.com/questions/42850555/video-and-audio-formats-supported-by-all-browsers
    private final List<String> videoFormats = new ArrayList<>(Arrays.asList(".mp4",".webm",".ogg"));
    
    @Autowired
    VideoRepository videoRepository;
    
    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }
    
    private Path createDirectory(Long userId) {
    	Path location = Paths.get(this.rootLoc + "/" +this.rootLoc + "-"+userId.toString());
    	try {
			Files.createDirectories(location);
		} catch (IOException e) {
			throw new StorageException("Could not create custom directory for userId: "+userId.toString(), e);
		}
    	return location;
    }
    
    private String getRealFilename(String filename, Long date) {
    	int dot = filename.lastIndexOf(".");
        String fileSubstring = filename.substring(0, dot);
        String realFilename = filename.replace(fileSubstring, fileSubstring.concat("-").concat(date + ""));
        return realFilename;
    }
    
    @Override
    public Video store(MultipartFile file, Long userId) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        Long date = new Date().getTime();
        String realFilename = getRealFilename(filename, date);
    	Path location = createDirectory(userId);
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            
            //This is a check if the file is a video or not.
            int dot = filename.lastIndexOf(".");
            if(
            		videoFormats.stream().noneMatch(val -> val.equals(filename.substring(dot)))
            	) {
            	throw new StorageException("Cannot store file of this file type: " + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
            	System.out.println("test1");
                Files.copy(inputStream, location.resolve(realFilename),
                    StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        return videoRepository.save(new Video(realFilename, date));
        
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                .filter(path -> !path.equals(this.rootLocation))
                .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename, Long userId) {
    	return Paths.get(this.rootLoc + "/" +this.rootLoc + "-"+userId.toString()).resolve(filename);
    }

    @Override
    public Resource loadAsResource(Long userId, Long videoId) {
        try {
        	Video video = videoRepository.findById(videoId).orElseThrow();
            Path file = load(video.getFilename(), userId);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else { 
                throw new StorageFileNotFoundException(
                        "Could not read file of videoId: " + videoId);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file of videoId: " + videoId, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
