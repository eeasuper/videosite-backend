package com.nano.videosite.thumbnails;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FFmpegFrameGrabber;

//@Component
public class VideoThumbnails {
	//https://stackoverflow.com/questions/37163978/how-to-get-a-thumbnail-of-an-uploaded-video-file/42386323

    protected String ffmpegApp;

    public VideoThumbnails(String ffmpegApp)
    {
        this.ffmpegApp = ffmpegApp;
    }

    public void getThumb(String videoFilename, String thumbFilename, int width, int height,int hour, int min, float sec)
      throws IOException, InterruptedException
    {
        ProcessBuilder processBuilder = new ProcessBuilder(ffmpegApp, "-y", "-i", videoFilename, "-vframes", "1",
    "-ss", hour + ":" + min + ":" + sec, "-f", "mjpeg", "-s", width + "*" + height, "-an", thumbFilename);
        Process process = processBuilder.start();
        InputStream stderr = process.getErrorStream();
        InputStreamReader isr = new InputStreamReader(stderr);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null);
        process.waitFor();
    }

    public void main() throws Exception, IOException
    {
//    	Path outputFolder = Paths.get("thumbnail-dir", "1", "1");
//    	System.out.println(outputFolder);
//    	Path inputFolder = Paths.get("seeding-dir", "yui-ura-on.mp4");
//    	System.out.println(inputFolder);
    	Path b = Paths.get("seeding-dir/yui-ura-on.mp4");
        FFmpegFrameGrabber g = new FFmpegFrameGrabber(b.toString());
        g.setFormat("mp4");
        g.start();

        for (int i = 0 ; i < 50 ; i++) {
        	Path a = Paths.get("thumbnail-dir");
            ImageIO.write(g.grab().getBufferedImage(), "png", new File(a.toString() +"/"+"yui-ura-on-" + System.currentTimeMillis() + ".png"));
        }

         g.stop();
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
