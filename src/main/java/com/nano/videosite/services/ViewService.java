package com.nano.videosite.services;



import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nano.videosite.exceptions.ElementNotFoundException;
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
		
		ViewVideo v = viewRepository.findByIpAndVideoId(view.getIp(), view.getVideoId())
				.orElse(view);

		if(view.equals(v)) {
			//if view equals v, it means the user of the request has not ever viewed the video
			setViewCount(view);
			return viewRepository.save(view);
		}
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
		Video vid = videoRepository.findById(view.getVideoId()).orElseThrow(()->new ElementNotFoundException());
		Long viewCount = vid.getView() + 1;
		vid.setView(viewCount);
		videoRepository.save(vid);
	}
	
	public Long getViewCount(Long videoId) {
		Video vid = videoRepository.findById(videoId).orElseThrow(()->new ElementNotFoundException());
		return vid.getView();
	}
}
