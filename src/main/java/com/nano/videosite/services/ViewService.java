package com.nano.videosite.services;


import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nano.videosite.models.Video;
import com.nano.videosite.models.ViewVideo;
import com.nano.videosite.repositories.ViewRepository;

@Service
public class ViewService {
	@Autowired
	ViewRepository viewRepository;
	
	public ViewVideo addViewCount(ViewVideo view) {
//		System.out.println(view.getFileName());
		ViewVideo v = viewRepository.findByIpAndFileName(view.getIp(), view.getFileName())
				.orElse(view);
		if(view.equals(v)) {
			//if view equals v, it means the user of the request has not ever viewed the video
			return viewRepository.save(view);
		}
		//https://www.tutorialspoint.com/java/java_date_time.htm
		Date requestDate = new Date(view.getDate());
		Date lastSavedDate = new Date(v.getDate());
		long difference = (requestDate.getTime() - lastSavedDate.getTime())/3600000;
		if(difference > 6) {
			//greater than 6 hours. 3600000 equals an hour.
			return viewRepository.save(view);
		}
		//v should return last Saved View.
		return v;
	}
	
	public int getViewCount(Video video) {
		//NOTE: Later, change code to get view count through a SUM selection of sql.
		List<ViewVideo> list = viewRepository.findByFileName(video.getFilename());
		return list.size();
	}
}
