package com.nano.videosite.repositories;

import java.util.Map;

import org.springframework.data.repository.CrudRepository;

import com.nano.videosite.models.PlaylistVideo;
import com.nano.videosite.models.Video;

public interface PlaylistVideoRepository extends CrudRepository<PlaylistVideo,Long>{

}
