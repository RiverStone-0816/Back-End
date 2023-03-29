package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.exception.StorageException;
import kr.co.eicn.ippbx.exception.StorageFileNotFoundException;
import kr.co.eicn.ippbx.util.UrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.cleanPath;

@Slf4j
@Service
public class FileSystemStorageService implements StorageService {

	@Override
	public void store(Path path, MultipartFile file) {
		store(path, UrlUtils.decode(cleanPath(Objects.requireNonNull(file.getOriginalFilename()))), file);
	}

	public void store(Path path, String fileName, MultipartFile file) {
			if (Files.notExists(path)) {
				try {
					Files.createDirectories(path);
				} catch (IOException ignored) {
				}
			}

		try {
			if (path.toString().indexOf("../") > 0)
				throw new StorageException("파일을 확인하세요.");
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + fileName);
			}
			Files.copy(file.getInputStream(), path.resolve(fileName));
		} catch (IOException e) {
			e.printStackTrace();
			log.error("FileSystemStorageService.store ERROR[error={}]", e.getMessage());
			throw new StorageException("[" + fileName + "] 파일 업로드에 실패하였습니다. 다시 시도하십시오.", e);
		}
	}

	@Override
	public Stream<Path> loadAll(Path storagePath) {
		try {
			return Files.walk(storagePath, 1)
					.filter(path -> !path.equals(storagePath))
					.map(storagePath::relativize);
		} catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}
	}

	@Override
	public Path load(String path, String fileName) {
		return load(Paths.get(path), fileName);
	}

	@Override
	public Path load(Path path, String fileName) {
		return path.resolve(fileName);
	}

	@Override
	public Resource loadAsResource(String path, String fileName) {
		return loadAsResource(Paths.get(path), fileName);
	}

	@Override
	public Resource loadAsResource(Path path, String fileName) {
		String decodedFileName = UrlUtils.decode(cleanPath(Objects.requireNonNull(fileName)));
		Path file = load(path, decodedFileName);

		if (path.toString().indexOf("..") > 0)
			throw new StorageFileNotFoundException(decodedFileName + "파일을 찾을 수 없습니다.");

		final FileSystemResource fileSystemResource = new FileSystemResource(file);

		if (fileSystemResource.exists() || fileSystemResource.isReadable()) {
			return fileSystemResource;
		} else {
			log.error("FileSystemStorageService.loadAsResource ERROR[file={}, path={}]", decodedFileName, file.toString());
			throw new StorageFileNotFoundException(decodedFileName + " 파일을 찾을 수 없습니다.");
		}
	}

	@Override
	public void deleteAll(Path path) {
		FileSystemUtils.deleteRecursively(path.toFile());
	}

	@Override
	public boolean delete(String path, String fileName) {
		return delete(load(path, fileName));
	}

	@Override
	public boolean delete(Path path) {
		try {
			Files.deleteIfExists(path);
		} catch (IOException ignored) {
			return false;
		}
		return true;
	}

	@Override
	public void init(Path path) {
		try {
			Files.createDirectory(path);
		} catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}
