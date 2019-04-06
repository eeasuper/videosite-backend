package com.nano.videosite.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.nano.videosite.models.ViewVideo;

public interface ViewRepository extends CrudRepository<ViewVideo, Long> {
	public Optional<ViewVideo> findByIpAndVideoId(String ip, Long videoId);
	public List<ViewVideo> findByVideoId(Long videoId);
}
