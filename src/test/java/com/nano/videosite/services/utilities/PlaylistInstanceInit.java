package com.nano.videosite.services.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.nano.videosite.models.Playlist;
import com.nano.videosite.models.User;
import com.nano.videosite.models.Video;
import com.nano.videosite.repositories.VideoRepository;
import com.nano.videosite.storage.FileSystemStorageService;
import com.nano.videosite.storage.StorageException;
import com.nano.videosite.thumbnails.VideoThumbnails;

public class PlaylistInstanceInit {
	
	
	public static Playlist playlist = new Playlist();
	
	public PlaylistInstanceInit(){
		try {
			PlaylistInstanceInit.playlist = getPlaylist();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	/*
	 * All the objects and functions created below is to make the function, getPlaylist() to work, and then to get the constructor to work.
	 */
	
	private static final Long time = new Date().getTime();
	
	public static final User user = new User((long) -1L,"name", "tom", "1234", "email");
	
	public static final Video v1 = new Video("yui-ura-on-"+time+".mp4", time,user.getId(),"yui-ura-on-funny","very funny video",(long)0);
	
	public static final Video v2 = new Video("test-"+time+".mp4",time,user.getId(),"Big bunny", "What a big bunny chasing a butterfly", (long) 0);
	
	private static final MultipartFile createMultipartFileForV1() throws IOException{
		return getVideoFile(v1.getFilename());
	}
	
	private static final MultipartFile createMultipartFileForV2() throws IOException{
		return getVideoFile(v2.getFilename());
	}
	
	private static final Video v1AfterStorage() throws IOException {
		return storeTestFiles(createMultipartFileForV1(),user.getId(),v1.getFilename(),v1);
	}
	
	private static final Video v2AfterStorage() throws IOException {
		return storeTestFiles(createMultipartFileForV2(),user.getId(),v2.getFilename(),v2);
	}
	
	/*
	 * getPlaylist() below is the most important function here.
	 */
	
	public static final Playlist getPlaylist() throws IOException {
		Playlist playlist = new Playlist(user.getId(), "test playlist", time);
		Map<Integer, Video> m1 = new HashMap<Integer, Video>();
        m1.put(1, v2AfterStorage());
        m1.put(2, v1AfterStorage());
        playlist.setPlaylist(m1);
        playlist.setId((long) 1L); 
        playlist.setUsername("tom");
        return playlist;
	}
	
	
	/*
	 * 
	 * 
	 * Methods below are utility methods for instantiating a playlist.
	 * 
	 * 
	 */
	
	private static final Video storeTestFiles(MultipartFile file, Long userId, String filename,Video video) {
		Path location = createDirectory(userId,"upload-dir");
		final List<String> videoFormats = new ArrayList<>(Arrays.asList(".mp4",".webm",".ogg"));
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
			createThumbnail(userId, filename, location.resolve(filename));
		} catch (Exception e) {
			throw new StorageException("Failed to create thumbnail: " + filename, e);
		}
        return new Video(filename, Long.parseLong(filename.substring(filename.lastIndexOf("-")+1,filename.lastIndexOf("."))), userId,video.getTitle(),video.getDescription(),video.getView());
	}
	
	private static void createThumbnail(Long userId, String realFilename, Path videoLocation) throws Exception, IOException {
    	// save video thumbnail here using ffmpeg which should be configured as an AddOn in Heroku (or installed locally).
    	Path saveLocation = createDirectory(userId,"thumbnail-dir").resolve(realFilename.substring(0,realFilename.lastIndexOf(".")));
    	Runtime.getRuntime().exec("ffmpeg -i " + videoLocation.toString()+ " -ss 00:00:0.500 -vframes 1 "+saveLocation.toString()+".png");
	}
	
	private static Path createDirectory(Long userId,String rootLoc) {
    	Path location = Paths.get(rootLoc +"/"+rootLoc+ "-"+userId.toString());
    	try {
			Files.createDirectories(location);
		} catch (IOException e) {
			throw new StorageException("Could not create custom directory for userId: "+userId.toString(), e);
		}
    	return location;
    }
	
	private static MultipartFile getVideoFile(String videoName) throws IOException {
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
	
}
