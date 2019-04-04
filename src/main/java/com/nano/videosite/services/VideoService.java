package com.nano.videosite.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nano.videosite.models.Video;
import com.nano.videosite.repositories.VideoRepository;

@Service
public class VideoService {
	@Autowired
	VideoRepository videoRepository;
	
	public Video add() {
		return null;
	}
}
