package kr.co.eicn.ippbx.server.service;

import diotts.Pttsnet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apache.commons.lang3.StringUtils.defaultString;

public class TTSService {
	private static final Logger logger = LoggerFactory.getLogger(TTSService.class);

	public static Integer created(String filePath, String fileName, String text, Integer speed) {
		int ret;
		final Path path = Paths.get(filePath, fileName);

		logger.info("TTSService created INFO[file name={}, path={}]", fileName, path.toString());

		if (Files.exists(path)) {
			throw new IllegalArgumentException("동일한 파일명이 존재합니다.");
		} else {
			try {
				if (Files.notExists(path.getParent())) {
					Files.createDirectories(path.getParent());
				}
			} catch (IOException ignored) {}

			final Pttsnet ttsEngine = new Pttsnet();
			try {
				ret = ttsEngine.PTTSNET_FILE_A_EX("TTS_SERVER", "6789", 20, 120, defaultString(text), path.toString(), null, null, 0, 0, 296, 100, speed, 100, 0, 0, 1, -1, null, -1);
			} catch (IOException e) {
				logger.error("TTSService created ERROR[error={}]", e.getMessage());
				throw new RuntimeException("음원 만들기 에러");
			}
		}

		return ret;
	}

	public static Integer createTemporaryFile(String path, String fileName, String text, Integer speed) {
		try {
			Files.deleteIfExists(Paths.get(path, fileName));
		} catch (IOException ignored) {}

		return created(path, fileName, text, speed);
	}
}
