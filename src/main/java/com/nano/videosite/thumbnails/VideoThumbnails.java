package com.nano.videosite.thumbnails;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

import com.nano.videosite.storage.StorageException;


@Service
public class VideoThumbnails {

    private final String rootLoc = "thumbnail-dir";

    public void thumbnail(Long userId, String realFilename, Path videoLocation) throws Exception, IOException {
    	// save video thumbnail here using ffmpeg which should be configured as an AddOn in Heroku (or installed locally).
    	Path saveLocation = createDirectory(userId).resolve(realFilename.substring(0,realFilename.lastIndexOf(".")));
    	Runtime.getRuntime().exec("ffmpeg -i " + videoLocation.toString()+ " -ss 00:00:0.500 -vframes 1 "+saveLocation.toString()+".png");
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
}


/*
 *
	
	private final static Path rootLocation = Paths.get("thumbnail-dir");
	private long userId;
	private long videoId;
//	private final String fileName;
	
//	public VideoThumbnails(String fileName, Long userId, Long videoId) {
//		this.fileName = fileName;
//		this.userId = userId;
//		this.videoId = videoId;
//	}
	
	public static final double SECONDS_BETWEEN_FRAMES = 10;
  
//    private static final String inputFilename = "c:/Java_is_Everywhere.mp4";
//    private static final String outputFilePrefix = "c:/snapshots/mysnapshot";
     
    // The video stream index, used to ensure we display frames from one and
    // only one video stream from the media container.
    private static int mVideoStreamIndex = -1;
     
    // Time of last frame write
    private static long mLastPtsWrite = Global.NO_PTS;
     
    public static final long MICRO_SECONDS_BETWEEN_FRAMES = 
        (long)(Global.DEFAULT_PTS_PER_SECOND * SECONDS_BETWEEN_FRAMES);
    //https://www.javacodegeeks.com/2011/02/xuggler-tutorial-frames-capture-video.html
    public void main(Long userId, Long videoId) {
//    public void main() {
    	
    	Path outputFolder = Paths.get(rootLocation.toString(), userId.toString(), videoId.toString());
    	System.out.println(outputFolder);
    	Path inputFolder = Paths.get("seeding-dir", "yui-ura-on.mp4");
    	System.out.println(inputFolder);
        IMediaReader mediaReader = ToolFactory.makeReader(inputFolder.toString());
        // stipulate that we want BufferedImages created in BGR 24bit color space
        mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
         
        mediaReader.addListener(new ImageSnapListener(outputFolder));
 
        // read out the contents of the media file and
        // dispatch events to the attached listener
        while (mediaReader.readPacket() == null) ;
 
    }
 
    private static class ImageSnapListener extends MediaListenerAdapter {
    	Path outputFolder;
    	ImageSnapListener(Path outputFolder){
    		this.outputFolder = outputFolder;
    	}
    	
        public void onVideoPicture(IVideoPictureEvent event) {
 
            if (event.getStreamIndex() != mVideoStreamIndex) {
                // if the selected video stream id is not yet set, go ahead an
                // select this lucky video stream
                if (mVideoStreamIndex == -1)
                    mVideoStreamIndex = event.getStreamIndex();
                // no need to show frames from this video stream
                else
                    return;
            }
 
            // if uninitialized, back date mLastPtsWrite to get the very first frame
            if (mLastPtsWrite == Global.NO_PTS)
                mLastPtsWrite = event.getTimeStamp() - MICRO_SECONDS_BETWEEN_FRAMES;
 
            // if it's time to write the next frame
            if (event.getTimeStamp() - mLastPtsWrite >= 
                    MICRO_SECONDS_BETWEEN_FRAMES) {
                                 
                String outputFilename = dumpImageToFile(event.getImage(),outputFolder);
 
                // indicate file written
                double seconds = ((double) event.getTimeStamp()) / 
                    Global.DEFAULT_PTS_PER_SECOND;
                System.out.printf(
                        "at elapsed time of %6.3f seconds wrote: %s\n",
                        seconds, outputFilename);
 
                // update last write time
                mLastPtsWrite += MICRO_SECONDS_BETWEEN_FRAMES;
            }
 
        }
         
        private String dumpImageToFile(BufferedImage image, Path outputFolder) {
            try {
//                String outputFilename = outputFilePrefix + 
//                     System.currentTimeMillis() + ".png";
                String outputFilename = outputFolder.toString() + "/mysnapshot" + 
                        System.currentTimeMillis() + ".png";
                ImageIO.write(image, "png", new File(outputFilename));
                return outputFilename;
            } 
            catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
 
    }
 
 * */
