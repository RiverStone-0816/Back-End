package kr.co.eicn.ippbx.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.eicn.ippbx.server.service.StorageService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
public class FileUploadsTests {
	@Autowired
	private MockMvc mvc;
	private static final ObjectMapper mapper = new ObjectMapper();
	@Autowired
	private StorageService fileSystemStorageService;

//	@Test
	public void shouldSaveUploadedFile() throws Exception {
		MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
				"text/plain", "Spring Framework".getBytes());

		this.mvc.perform(multipart("/files/upload")
				.file(multipartFile)
				.param("soundName", "ARS 음원듣기")
				.param("comment", "미리듣기"))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@SneakyThrows
//	@Test
	public void limitCheckSizeAndExtCheck() {
		final Resource resource = this.fileSystemStorageService.loadAsResource("/Users/isang-ug/Uploads", "프리미엄_화면기획_V1.8.pptx");

		log.info("file name {}", resource.getFilename());
		log.info("content length {} ", resource.contentLength());
		log.info(sizeCalculation(resource.contentLength()));
		log.info(sizeCalculation(5 * 1024 * 1024));

		if (resource.contentLength() > 5 * 1024 * 1024) {
			log.error("최대 파일 사이즈는 " + 5 + "MB 까지입니다.");
		}
		if (!StringUtils.endsWithAny(resource.getFilename(), ".wav", ".WAV", ".gsm", ".GSM")) { log.error("알수 없는 파일 확장자입니다."); }
	}

//	@Test
	public void uploadFile() throws IOException {
		MockMultipartFile multipartFile = new MockMultipartFile("file", "11111.txt",
				"text/plain", "Spring Framework".getBytes());

	}

	@SneakyThrows
//	@Test
	public void files() {
		final Path path = Paths.get("D:\\tts\\demo_master.wav");

		log.info("root {}", path.getParent().toString());
		log.info("path {}", path.toString());
		log.info("is exists ? {}", Files.exists(path));
		log.info("is size ? {}", Files.size(path));

		final Path path1 = Paths.get("D:\\aaa");
		if (Files.notExists(path1)) {
			Files.createDirectories(path1);

			log.info("is exists ? {}", Files.exists(path1));
		}

		final Path path2 = Paths.get("D:\\aaa", "\\bbbb\\");
		Files.deleteIfExists(path2);
	}

//	@Test
	public void lists() {
		final File file = new File("D:\\data\\primium");

		final String[] list = file.list();
		Arrays.sort(list);
		Arrays.asList(list).forEach(e -> log.info(e));


		final List<Path> files = this.fileSystemStorageService.loadAll(Paths.get("D:\\data", "primium"))
				.peek(e -> {
					log.info(e.getFileName().toString());
					log.info("is true? {}", e.endsWith(".gz"));
				})
				.sorted(Comparator.comparing(Path::toString))
				.collect(Collectors.toList());
	}

//	@Test
	public void disk() {
		try {
			final BufferedReader br = Files.newBufferedReader(Paths.get("D:\\tmp", "disk.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				if (line.endsWith("/")) {
					final String[] s = line.split(" ");
					log.info(line);
					log.info("s {} " + s.length);

					StringTokenizer st = new StringTokenizer(line);

					log.info("StringTokenzier {}", st);

					while (st.hasMoreElements()) {
						final String s1 = st.nextToken();
						log.info("token {}", s1);
					}

					for (String s1 : s) {
						if (StringUtils.isNotEmpty(s1)) {
							log.info("string value {}", s1);
						}
					}
				}
			}
		} catch (IOException e) {
			log.error("Exception!", e);
		}
	}

	/**
	 * 용량계산
	 * @param size
	 * @return
	 */
	public static String sizeCalculation(long size) {
		String CalcuSize = null;
		int i = 0;

		double calcu = (double) size;
		while (calcu >= 1024 && i < 5) { // 단위 숫자로 나누고 한번 나눌 때마다 i 증가
			calcu = calcu / 1024;
			i++;
		}
		DecimalFormat df = new DecimalFormat("##0.0");
		switch (i) {
			case 0:
				CalcuSize = df.format(calcu) + "Byte";
				break;
			case 1:
				CalcuSize = df.format(calcu) + "KB";
				break;
			case 2:
				CalcuSize = df.format(calcu) + "MB";
				break;
			case 3:
				CalcuSize = df.format(calcu) + "GB";
				break;
			case 4:
				CalcuSize = df.format(calcu) + "TB";
				break;
			default:
				CalcuSize="ZZ"; //용량표시 불가
		}
		return CalcuSize;
	}
}
