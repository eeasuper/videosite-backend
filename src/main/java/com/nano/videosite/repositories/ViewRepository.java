package com.nano.videosite.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.nano.videosite.models.ViewVideo;

public interface ViewRepository extends CrudRepository<ViewVideo, Long> {
	public Optional<ViewVideo> findByIpAndFileName(String ip, String filename);
	public List<ViewVideo> findByFileName(String filename);
}
