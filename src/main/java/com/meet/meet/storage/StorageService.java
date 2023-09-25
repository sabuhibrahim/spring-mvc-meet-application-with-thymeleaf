package com.meet.meet.storage;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
    void init();

	String store(MultipartFile file);

	String store(MultipartFile file, String basePath);

	Stream<Path> loadAll();

	Path load(String filename);

	Resource loadAsResource(String filename);

	void deleteAll();

}
