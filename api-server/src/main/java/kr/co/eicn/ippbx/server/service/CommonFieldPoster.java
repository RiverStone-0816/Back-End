package kr.co.eicn.ippbx.server.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@Service
public class CommonFieldPoster extends ApiBaseService {
    private final static Logger logger = LoggerFactory.getLogger(CommonFieldPoster.class);
    private final static String COMMAND = "/home/ippbxmng/lib/common_upload.sh";
    private final static Long MAX_FILE_SIZE = 5L * 1024 * 1024;
    private final FileSystemStorageService fileSystemStorageService;

    @Async
    public CompletableFuture<Void> postByExcel(ExcelType type, Integer groupId, MultipartFile file) throws IOException {
        if (file.isEmpty())
            throw new IllegalArgumentException("비어있는 파일입니다.");

        if (file.getSize() > MAX_FILE_SIZE)
            throw new IllegalArgumentException("file size is over MAX_FILE_SIZE: " + MAX_FILE_SIZE);

        final Path tempDirectory = Files.createTempDirectory("ipcc-excel-upload-");
        final String saveFileName =  System.currentTimeMillis() + "_" + System.nanoTime() + "_" + type.name();

        fileSystemStorageService.store(tempDirectory, saveFileName, file);

        return CompletableFuture.runAsync(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                logger.info("CommonFieldPoster.postByExcel INFO [type={}, path={}]", type.name(), tempDirectory.resolve(saveFileName).toString());
                final ProcessBuilder processBuilder = new ProcessBuilder(COMMAND, type.name(), groupId.toString() , tempDirectory.resolve(saveFileName).toString());
                final Process process = processBuilder.start();
                final BufferedReader out = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String str;
                while ((str = out.readLine()) != null)
                    logger.trace(str);

                logger.info("result: " + process.exitValue());
            }
        });
    }

    @Async
    public CompletableFuture<Void> postByExcel(ExcelType type, String companyId, MultipartFile file) throws IOException {
        if (file.isEmpty())
            throw new IllegalArgumentException("비어있는 파일입니다.");

        if (file.getSize() > MAX_FILE_SIZE)
            throw new IllegalArgumentException("file size is over MAX_FILE_SIZE: " + MAX_FILE_SIZE);

        final Path tempDirectory = Files.createTempDirectory("ipcc-excel-upload-");
        final String saveFileName =  System.currentTimeMillis() + "_" + System.nanoTime() + "_" + type.name();

        fileSystemStorageService.store(tempDirectory, saveFileName, file);

        return CompletableFuture.runAsync(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                logger.info("CommonFieldPoster.postByExcel INFO [type={}, path={}]", type.name(), tempDirectory.resolve(saveFileName).toString());
                final ProcessBuilder processBuilder = new ProcessBuilder(COMMAND, type.name(), companyId, tempDirectory.resolve(saveFileName).toString());
                final Process process = processBuilder.start();
                final BufferedReader out = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String str;
                while ((str = out.readLine()) != null)
                    logger.trace(str);

                logger.info("result: " + process.exitValue());
            }
        });
    }

    public enum ExcelType {
        CST, PRV, PDS, CON, TALK, MAINDB, VIP, BLACK
    }
}
