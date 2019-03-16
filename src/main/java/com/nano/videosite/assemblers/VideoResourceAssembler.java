package com.nano.videosite.assemblers;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;

import com.nano.videosite.models.Video;

public class VideoResourceAssembler implements ResourceAssembler<Video, Resource<Video>>{

	@Override
	public Resource<Video> toResource(Video entity) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
