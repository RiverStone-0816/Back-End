package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.model.enums.ShellCommand;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ProcessService extends ApiBaseService {
	private static final Logger logger = LoggerFactory.getLogger(ProcessService.class);

	@SneakyThrows
	public void execute(ShellCommand command, String... args) {
		final List<String> commands = new ArrayList<>();
		commands.add(command.getUrl());
		commands.addAll(Arrays.asList(args));

		logger.info("ProcessService.execute INFO[url={}, arguments={}]", command.getUrl(), String.join(",", commands));
		final ProcessBuilder builder = new ProcessBuilder(commands);
		try {
			final Process process = builder.start();

			final int exitCode = process.waitFor();

			logger.info("exitCode {}", exitCode);
		} catch (IOException e) {
			logger.error("ProcessService.execute ERROR[error={}]", e.getMessage());
		}
	}
}
