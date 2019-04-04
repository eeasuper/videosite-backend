package com.nano.videosite.repositories;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.nano.videosite.models.Playlist;
import com.nano.videosite.models.Video;

public interface PlaylistRepository extends CrudRepository<Playlist, Long>{
	public Playlist findByIdAndUserId(Long id, Long userId); 
	public Optional<List<Playlist>> findByUserId(Long userId);
}
