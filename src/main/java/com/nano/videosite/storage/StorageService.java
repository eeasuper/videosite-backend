package com.nano.videosite.storage;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.nano.videosite.models.Video;

public interface StorageService {
    void init();

    Video store(MultipartFile file, Long userId);

    Stream<Path> loadAll();

    Path load(String filename, Long userId);

    void deleteAll();

	Path loadThumbnail(String filename, Long userId);

	Resource loadThumbnailAsResource(Video video);

	Resource loadAsResource(Long videoId);

	Video storeSeedFiles(MultipartFile file, Long userId, String filename, Video video);
}
