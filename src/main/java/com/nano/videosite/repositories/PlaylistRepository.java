package com.nano.videosite.repositories;

import java.util.Map;

import org.springframework.data.repository.CrudRepository;

import com.nano.videosite.models.Playlist;
import com.nano.videosite.models.Video;

public interface PlaylistRepository extends CrudRepository<Playlist, Long>{
	public Playlist findByIdAndUserId(Long id, Long userId); 
}
