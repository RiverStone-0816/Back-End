package kr.co.eicn.ippbx.server.controller.api.v1.admin.record.file;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.StorageFileNotFoundException;
import kr.co.eicn.ippbx.model.dto.eicn.DiskResponse;
import kr.co.eicn.ippbx.model.dto.eicn.FileSummaryResponse;
import kr.co.eicn.ippbx.server.service.StorageService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * 녹취관리 > 녹취파일관리 > 녹취파일관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/record/file", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecordManagerApiController extends ApiBaseController {

	private final StorageService fileSystemStorageService;
	@Value("${file.path.data}")
	private String savePath;

	/**
	 * 녹취파일관리 목록조회
	 */
	@GetMapping("")
	public ResponseEntity<JsonResult<List<FileSummaryResponse>>> list() {
		final List<FileSummaryResponse> files = this.fileSystemStorageService.loadAll(Paths.get(savePath, g.getUser().getCompanyId()))
				.filter(e -> e.toString().endsWith(".gz"))
				.sorted(Comparator.comparing(Path::toString))
				.map((e) -> {
					final FileSummaryResponse fileSummaryResponse = new FileSummaryResponse();
					fileSummaryResponse.setFileName(e.toString());
					try {
						fileSummaryResponse.setSize(Files.size(Paths.get(savePath, g.getUser().getCompanyId(), e.toString())));
					} catch (IOException ignored) {
					}

					return fileSummaryResponse;
				})
				.collect(Collectors.toList());

		return ResponseEntity.ok().body(data(files));
	}

	/**
	 * 녹취파일관리 삭제
	 */
	@DeleteMapping("{fileName:.+}")
	public ResponseEntity<JsonResult<Void>> delete(@PathVariable String fileName) {
		final Path path = Paths.get(savePath, g.getUser().getCompanyId(), fileName);
		if (Files.notExists(path))
			throw new StorageFileNotFoundException(fileName + " 파일을 찾을 수 없습니다.");

		this.fileSystemStorageService.delete(path);

		return ResponseEntity.ok(create());
	}

	/**
	 * 녹취파일관리 파일 다운로드
	 */
	@GetMapping(value = "resource", params = {"token"})
	public ResponseEntity<Resource> resource(@RequestParam String fileName) {
		if (isEmpty(fileName))
			throw new IllegalArgumentException();

		if (fileName.indexOf("../") > 0)
			throw new IllegalArgumentException();

		final Resource resource = this.fileSystemStorageService.loadAsResource(Paths.get(savePath, g.getUser().getCompanyId()), fileName);

		return ResponseEntity.ok()
				.contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
				.headers(header -> header.add(HttpHeaders.CONTENT_DISPOSITION,
						ContentDisposition.builder("attachment").filename(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8).build().toString()))
				.body(resource);
	}

	/**
	 * 디스크 사용량
	 */
	@GetMapping("disk")
	public ResponseEntity<JsonResult<DiskResponse>> disk() {
		final DiskResponse disk = new DiskResponse();
		try (final InputStream in = Runtime.getRuntime().exec("df -h").getInputStream();
		     final InputStreamReader reader = new InputStreamReader(in);
		     final BufferedReader br = new BufferedReader(reader)) {
			String line;
			while ((line = br.readLine()) != null) {
				if (line.endsWith("/")) {
					final StringTokenizer st = new StringTokenizer(line);
					int colNumber = 0;
					while (st.hasMoreElements()) {
						final String data = st.nextToken();
						if (colNumber == 2)
							disk.setUsed(data);
						if (colNumber == 3)
							disk.setAvail(data);
						if (colNumber == 4)
							disk.setUse(data);

						colNumber++;
					}
				}
			}
		} catch (IOException e) {
			log.error("RecordManagerApiController.disk ERROR[error={}]", e.getMessage());
			disk.setAvail("0");
			disk.setUse("0");
			disk.setUsed("0");
			return ResponseEntity.ok().body(data(disk));
			//throw new IllegalArgumentException("시스템 정보를 읽어올 수 없습니다.");
		}

		return ResponseEntity.ok().body(data(disk));
	}
}
