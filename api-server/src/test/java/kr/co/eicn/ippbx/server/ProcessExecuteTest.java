package kr.co.eicn.ippbx.server;

import kr.co.eicn.ippbx.server.service.ProcessService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@SpringBootTest
public class ProcessExecuteTest {

	@Autowired
	private ProcessService processService;

	@SneakyThrows
//	@Test
	public void execute() {
		final ProcessBuilder builder = new ProcessBuilder("/Users/isang-ug/Source/work/primium-api-server/src/test/resources/process_test.sh", "1", "22");

		final Process start = builder.start();

		final Future<?> submit = Executors.newSingleThreadScheduledExecutor().submit(() -> new BufferedReader(new InputStreamReader(start.getInputStream())).lines()
				.forEach(log::info));

		final int exitCode = start.waitFor();

		log.warn("{}", exitCode);
	}
}
