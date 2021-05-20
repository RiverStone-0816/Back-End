package kr.co.eicn.ippbx.server;

import kr.co.eicn.ippbx.model.dto.eicn.DiskResponse;
import kr.co.eicn.ippbx.model.dto.eicn.FileSummaryResponse;
import kr.co.eicn.ippbx.server.service.IpccUrlConnection;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.UrlResource;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.MalformedURLException;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.trim;

@Slf4j
@SpringBootTest
public class UrlConnectionTest {
	private long startTime;
	private long endTime;
	@BeforeEach
	public void onBefore() {
		startTime = System.currentTimeMillis();
	}

	@AfterEach
	public void onAfter() {
		endTime = System.currentTimeMillis();
		log.info("소요시간: {}ms", endTime - startTime);
	}

	//	@Test
	public void recordDayManageRemoteStream() {
		String f_urlString = "http://122.49.74.240/ipcc/multichannel/remote/record_file_server.jsp?company_id=primium";

		final String line = IpccUrlConnection.fetch(f_urlString, Collections.emptyMap());
//		final String line = "" + "|";

//		2019_12_11.tar.gz&52,221,700/2019_12_12.tar.gz&67,447,113/|
		final String replace = line.replace("|", "");

		if (StringUtils.isNotEmpty(replace)) {
			final String[] strings = StringUtils.split(replace.substring(0, replace.lastIndexOf("/")), "/");
			final List<FileSummaryResponse> collect = Arrays.stream(strings)
					.peek(e -> log.info(e))
					.map(e -> {
						final FileSummaryResponse summaryResponse = new FileSummaryResponse();
						final String[] split1 = e.split("&");
						summaryResponse.setFileName(StringUtils.trim(split1[0]));
						summaryResponse.setSize(Long.valueOf(split1[1].replaceAll(",", "")));
						return summaryResponse;
					})
					.collect(Collectors.toList());

			collect.forEach(e -> log.info(e.toString()));
		}
	}

//	@Test
	public void getPbxDiskInfo() {

		final DiskResponse disk = new DiskResponse();

		final String fetch = IpccUrlConnection.fetch("http://122.49.74.240/ipcc/multichannel/remote/record_server.jsp"
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

		log.info("disk {}", disk.toString());
	}

//	@Test
	public void getPbxFileData() {
		try {
//			final UrlResource urlResource = new UrlResource(URI.create("http://122.49.74.240/ipcc/multichannel/remote/record_gz_down_test1.jsp?company_id=primium&file=2019_12_11_test.tar.gz"));
			final UrlResource urlResource = new UrlResource("http://122.49.74.240/ipcc/multichannel/remote/record_gz_down_test2.jsp?company_id=primium&file=2019_12_11_test.tar.gz");
//			final UrlResource urlResource = new FileUrlResource("D:\\uploads\\master_20191128162100.tar.gz");

//			final String fetch = IpccUrlConnection.fetch("http://122.49.74.240/ipcc/multichannel/remote/record_gz_down_test2.jsp?company_id=primium&file=logen.zip"
//				, Collections.emptyMap());

			try {
				final InputStream inputStream = urlResource.getInputStream();

				File f = new File("D:\\sssss.tar.gz");
				final FileOutputStream fileOutputStream = new FileOutputStream(f);
				IOUtils.copy(inputStream, fileOutputStream);

//				File f1 = new File("D:\\sssss1.zip");

//				final FileOutputStream outputStream = new FileOutputStream(f1);
//				final BufferedInputStream bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(trim(fetch).getBytes()));
//				IOUtils.copy(bufferedInputStream, outputStream);

			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

//	@Test
	public void deleteByPbxRecordFileWithRestTemplate() {
		final RestTemplate restTemplate = new RestTemplate();
		final String s = restTemplate.getForObject("http://122.49.74.240/ipcc/multichannel/remote/record_day_del_exe.jsp?flag=true&company_id=primium&filename=2019_12_11_test222222222", String.class);

		log.info(s);
	}

//	@Test
	public void deleteByPbxRecordFileIpccUrlConnection() {
		final String fetch = IpccUrlConnection.fetch("http://localhost/ipcc/multichannel/remote/record_day_del_exe.jsp?flag=true&company_id=primium&filename=2019_12_11_test222222222.tar.gz", Collections.emptyMap());
		log.info(fetch);

	}
}
