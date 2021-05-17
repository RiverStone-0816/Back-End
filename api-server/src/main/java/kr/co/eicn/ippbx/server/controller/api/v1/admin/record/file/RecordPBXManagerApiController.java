package kr.co.eicn.ippbx.server.controller.api.v1.admin.record.file;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.model.dto.eicn.DiskResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.FileSummaryResponse;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.IpccUrlConnection;
import kr.co.eicn.ippbx.server.service.StorageService;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.util.JsonResult.Result.failure;
import static kr.co.eicn.ippbx.server.util.JsonResult.Result.success;
import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * 녹취관리 > 녹취파일관리 > 녹취파일관리(분리형)
 */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/record/remote-file", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecordPBXManagerApiController extends ApiBaseController {

	private final CacheService cacheService;
	private final StorageService fileSystemStorageService;
	@Value("${file.path.data}")
	private String savePath;
	private String targetHost = "localhost";

	/**
	 * 녹취파일관리(PBX/WEB 서버 분리형일때) 목록조회
	 */
	@GetMapping("")
	public ResponseEntity<JsonResult<List<FileSummaryResponse>>> list() {
		cacheService.pbxServerList(g.getUser().getCompanyId()).stream()
				.findFirst()
				.ifPresent(e -> targetHost = e.getServer().getIp());

		final String fetch = IpccUrlConnection.fetch("http://" + targetHost + "/ipcc/multichannel/remote/record_file_server.jsp?company_id=" + g.getUser().getCompanyId()
				, Collections.emptyMap());

		if (fetch != null) {
			final String replace = fetch.replace("|", "");
			if (isNotEmpty(replace)) {
				final String[] strings = split(replace.substring(0, replace.lastIndexOf("/")), "/");
				final List<FileSummaryResponse> files = Arrays.stream(strings)
						.map(e -> {
							final FileSummaryResponse summaryResponse = new FileSummaryResponse();
							final String[] split1 = e.split("&");
							summaryResponse.setFileName(trim(split1[0]));
							summaryResponse.setSize(Long.valueOf(split1[1].replaceAll(",", "")));
							return summaryResponse;
						})
						.collect(Collectors.toList());

				return ResponseEntity.ok().body(data(files));
			}
		}

		return ResponseEntity.ok().body(data(Collections.emptyList()));
	}

	/**
	 * 녹취파일관리(PBX/WEB 서버 분리형일때) 삭제
	 */
	@DeleteMapping("")
	public ResponseEntity<JsonResult<Void>> delete(@RequestParam String fileName) {
		if(!fileName.endsWith(".tar.gz"))
			throw new IllegalArgumentException("알수없는 파일 확장자입니다.");

		this.fileSystemStorageService.delete(Paths.get(savePath, g.getUser().getCompanyId()).toString(), fileName);

		cacheService.pbxServerList(g.getUser().getCompanyId()).stream()
				.findFirst()
				.ifPresent(e -> targetHost = e.getServer().getIp());

		final String fetch = IpccUrlConnection.fetch("http://" + targetHost + "/ipcc/multichannel/remote/record_day_del_exe.jsp?"
						+ "flag=true"
						+ "&company_id=" + g.getUser().getCompanyId()
						+ "&filename=" + fileName
				, Collections.emptyMap());
		if (fetch == null)
			throw new RuntimeException("서버가 정상인지 확인후 사용해주세요.");

		return ResponseEntity.ok((create(fetch.startsWith("NOK") ? failure : success, fetch.startsWith("NOK") ? fetch : "")));
	}

	/**
	 * 녹취파일관리(PBX/WEB 서버 분리형일때) 파일 다운로드
	 */
	@GetMapping(value = "resource", params = {"token"})
	public ResponseEntity<Resource> resource(@RequestParam String fileName) {
		if (isEmpty(fileName))
			throw new IllegalArgumentException();
		if (!fileName.endsWith(".tar.gz"))
			throw new IllegalArgumentException("알수없는 파일 확장자입니다.");

		if (fileName.indexOf("../") > 0)
			throw new IllegalArgumentException();

		final Path path = Paths.get(savePath, g.getUser().getCompanyId());
		final String argStr = " " + targetHost + " " + fileName + " " + path.toString() + " &";

		Resource resource;

		if (!Files.exists(path.resolve(fileName))) {
			try {
				Runtime.getRuntime().exec("/home/ippbxmng/lib/scp_get_single.sh " + argStr);
				try {
					TimeUnit.MILLISECONDS.sleep(1200);
				} catch (InterruptedException ignore) {
				}
			} catch (IOException ignore) {
			}
		}

		resource = this.fileSystemStorageService.loadAsResource(path, fileName);

		return ResponseEntity.ok()
				.contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
				.headers(header -> header.add(HttpHeaders.CONTENT_DISPOSITION,
						ContentDisposition.builder("attachment").filename(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8).build().toString()))
				.body(resource);
	}

	/**
	 * 녹취파일관리(PBX/WEB 서버 분리형일때) 디스크 사용량
	 */
	@GetMapping("disk")
	public ResponseEntity<JsonResult<DiskResponse>> disk() {
		cacheService.pbxServerList(g.getUser().getCompanyId()).stream()
				.findFirst()
				.ifPresent(e -> targetHost = e.getServer().getIp());

		final DiskResponse disk = new DiskResponse();

		final String fetch = IpccUrlConnection.fetch("http://" + targetHost + "/ipcc/multichannel/remote/record_server.jsp"
				, Collections.emptyMap());

		if (fetch != null) {
			final String trimData = trim(fetch);
			final String[] split = trimData.split("/");
			if (split.length > 1) {
				disk.setUsed(split[0]);
				disk.setAvail(split[1]);
				disk.setUse(split[2]);
			}
		}

		return ResponseEntity.ok().body(data(disk));
	}
}
