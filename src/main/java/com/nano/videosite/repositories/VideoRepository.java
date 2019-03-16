package com.nano.videosite.repositories;

import org.springframework.data.repository.CrudRepository;

import com.nano.videosite.models.Video;

public interface VideoRepository extends CrudRepository<Video, Long>{
	
}
