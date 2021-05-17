package kr.co.eicn.ippbx.server.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
	void init(Path path);

	void store(Path path, MultipartFile file);

	Stream<Path> loadAll(Path path);

	Path load(String path, String fileName);

	Path load(Path path, String fileName);

	Resource loadAsResource(String path, String fileName);

	Resource loadAsResource(Path path, String fileName);

	void deleteAll(Path path);

	boolean delete(String path, String fileName);

	boolean delete(Path path);
}
