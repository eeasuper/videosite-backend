package com.nano.videosite.services;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nano.videosite.models.Video;
import com.nano.videosite.models.ViewVideo;
import com.nano.videosite.repositories.VideoRepository;
import com.nano.videosite.repositories.ViewRepository;

@Service
public class ViewService {
	@Autowired
	ViewRepository viewRepository;
	@Autowired 
	VideoRepository videoRepository;
	public ViewVideo addViewCount(ViewVideo view) {
		//I shouldn't save the viewVideo. I should just read it and save view count on VideoRepository.
		
		ViewVideo v = viewRepository.findByIpAndVideoId(view.getIp(), view.getVideoId())
				.orElse(view);

		if(view.equals(v)) {
			//if view equals v, it means the user of the request has not ever viewed the video
			setViewCount(view);
			return viewRepository.save(view);
		}
		//https://www.tutorialspoint.com/java/java_date_time.htm
		Date requestDate = new Date(view.getDate());
		Date lastSavedDate = new Date(v.getDate());
		long difference = (requestDate.getTime() - lastSavedDate.getTime())/3600000;
		if(difference > 6) {
			//greater than 6 hours. 3600000 equals an hour.
			setViewCount(view);
			return viewRepository.save(view);
		}
		//v should return last Saved View.
		return v;
	}
	
	private void setViewCount(ViewVideo view) {
		Video vid = videoRepository.findById(view.getVideoId()).orElseThrow();
		Long viewCount = vid.getView() + 1;
		vid.setView(viewCount);
		videoRepository.save(vid);
	}
	
	public Long getViewCount(Video video) {
		Video vid = videoRepository.findById(video.getId()).orElseThrow();
		return vid.getView();
		
		//NOTE: Later, change code to get view count through a SUM selection of sql.
//		List<ViewVideo> list = viewRepository.findByFileName(video.getFilename());
//		return list.size();
	}
}
