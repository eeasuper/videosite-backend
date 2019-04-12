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

import org.bytedeco.javacv.FrameGrabber.Exception;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.nano.videosite.exceptions.ElementNotFoundException;
import com.nano.videosite.models.Video;
import com.nano.videosite.repositories.VideoRepository;
import com.nano.videosite.thumbnails.VideoThumbnails;

@Service
public class FileSystemStorageService implements StorageService{
    private final Path rootLocation = Paths.get("upload-dir");
    private final String rootLoc = "upload-dir";
    //https://stackoverflow.com/questions/42850555/video-and-audio-formats-supported-by-all-browsers
    private final List<String> videoFormats = new ArrayList<>(Arrays.asList(".mp4",".webm",".ogg"));
    
    @Autowired
    VideoRepository videoRepository;
    @Autowired
    VideoThumbnails videoThumbnail;
    
//    @Autowired
//    public FileSystemStorageService(StorageProperties properties) {
//        this.rootLocation = Paths.get(properties.getLocation());
//    }
    
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
        
        try {
			videoThumbnail.thumbnail(userId, realFilename, location.resolve(realFilename));
		} catch (Exception e) {
			throw new StorageException("Failed to create thumbnail: " + filename, e);
		} catch (IOException e) {
			throw new StorageException("Failed to create thumbnail: " + filename, e);
		}
        return videoRepository.save(new Video(realFilename, date, userId));
        
    }
    
    //Copy the files in seeding-dir to a dir in upload-dir.
    @Override
    public Video storeSeedFiles(MultipartFile file, Long userId, String filename,Video video) {
        Long date = new Date().getTime();
        Path location = createDirectory(userId);
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }            
            //This is a check if the file is a video or not.
            int dot = filename.lastIndexOf(".");
            if(
            		videoFormats.stream().noneMatch(val -> val.equals(filename.substring(dot)))
            	) {
            	throw new StorageException("Cannot store file of this file type: " + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {

                Files.copy(inputStream, location.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
        
        try {
        	System.out.println(filename);
			videoThumbnail.thumbnail(userId, filename, location.resolve(filename));
		} catch (Exception e) {
			throw new StorageException("Failed to create thumbnail: " + filename, e);
		} catch (IOException e) {
			throw new StorageException("Failed to create thumbnail: " + filename, e);
		}
        return videoRepository.save(new Video(filename, Long.parseLong(filename.substring(filename.lastIndexOf("-")+1,filename.lastIndexOf("."))), userId,video.getTitle(),video.getDescription(),video.getView()));
        
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
    public Resource loadAsResource(Long videoId) {
        try {
        	Video video = videoRepository.findById(videoId).orElseThrow(()->new ElementNotFoundException());
            Path file = load(video.getFilename(), video.getUploaderId());
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
    public Path loadThumbnail(String filename, Long userId) {
    	return Paths.get("thumbnail-dir/thumbnail-dir-"+userId.toString()).resolve(filename);
    }

    @Override
    public Resource loadThumbnailAsResource(Video video) {
        try {
//        	Video video = videoRepository.findById(vid.getId()).orElseThrow();
        	String filename = video.getFilename().substring(0, video.getFilename().lastIndexOf(".")) + ".png";
            Path file = loadThumbnail(filename, video.getUploaderId());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else { 
                throw new StorageFileNotFoundException(
                        "Could not get thumbnail: " + filename);
            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not get thumbnail of videoId: " + video.getFilename(), e);
        }
    }
    
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
        Path thumbnailDir = Paths.get("thumbnail-dir");
        FileSystemUtils.deleteRecursively(thumbnailDir.toFile());
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
