package com.nano.videosite.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.nano.videosite.models.Video;

public interface VideoRepository extends CrudRepository<Video, Long>{
	
	List<Video> findFirst6ByUploaderIdOrderByDateAsc(Long uploaderId);
	
	@Query(value="SELECT * FROM Video ORDER BY RANDOM() LIMIT 6", nativeQuery = true)
	List<Video> getRandomList();
	
	public Optional<List<Video>> findByUploaderIdOrderByDateDesc(Long uploaderId);
	
	List<Video> findByTitleContainingIgnoreCase(String title);
}
