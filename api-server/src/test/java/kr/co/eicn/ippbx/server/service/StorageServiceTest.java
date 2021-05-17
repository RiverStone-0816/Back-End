package kr.co.eicn.ippbx.server.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.parameters.P;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.stream.Stream;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class StorageServiceTest {

	@Autowired
	private StorageService storageService;

//	@Test
	public void path() {
		final Path path = Paths.get("/Users/isang-ug/Uploads");
		log.info(path.toString());
		log.info(path.getFileSystem().getSeparator());

		final Stream<Path> pathStream = storageService.loadAll(path);

		pathStream.forEach(e -> {
			log.info(e.getFileName().toString());
		});
	}

//	@Test
	public void load() {
		final Path fullFilePath = this.storageService.load(Paths.get("/Users/isang-ug/Uploads"), "wave.wav");

		log.info(fullFilePath.toUri().toString());
		log.info(fullFilePath.toString());
	}
}
