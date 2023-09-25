package com.meet.meet.storage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {
    private final Path rootLocation;

	private final String rootLocationName;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
		this.rootLocationName = properties.getLocation();
	}

	@Override
	public String store(MultipartFile file) {
		return store(file, null);
	}

	@Override
	public String store(MultipartFile file, String basePath) {
		if(basePath == null) {
			basePath = "media";
		}
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file.");
			}

			LocalDate nowDate = LocalDate.now();
			
			String filePath = basePath
							  + "/" + Integer.toString(nowDate.getYear())
							  + "/" + Integer.toString(nowDate.getMonth().getValue())
							  + "/" + file.getOriginalFilename();

			Path destinationFile = this.rootLocation.resolve(Paths.get(filePath))
													.normalize().toAbsolutePath();

			System.out.println(destinationFile.toUri().getPath());
			try {
				Files.createDirectories(destinationFile);
			} catch (FileAlreadyExistsException ex) {
				logger.debug("File already exist: " + destinationFile.toUri().getPath() + " [ Updating file ]");
			}
			try (InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,
					StandardCopyOption.REPLACE_EXISTING);
			}

			return filePath;
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file.", e);
		}
	}

	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {

		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
