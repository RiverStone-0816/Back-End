package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.repository.customdb.MaindbCustomInfoRepository;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.replace;

@Slf4j
@Service
public class MaindbCustomInfoService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(MaindbCustomInfoService.class);
    private final Map<String, MaindbCustomInfoRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;
    private final StorageService fileSystemStorageService;
    @Value("${file.path.custom}")
    private String savePath;
    @Value("${file.path.temporary.default}")
    private String temporaryPath;

    public MaindbCustomInfoService(StorageService fileSystemStorageService) {
        this.fileSystemStorageService = fileSystemStorageService;
    }

    public MaindbCustomInfoRepository getRepository() {
        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final MaindbCustomInfoRepository repository = new MaindbCustomInfoRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @SneakyThrows
    public void uploadImgWithFileStore(String fileName, String oldFileName) {
        final Path filePath = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()));
        final File tmpFile = new File(temporaryPath + "/" + fileName);

        if (tmpFile.isFile()) {
            if (!StringUtils.endsWithAny(fileName.toLowerCase(), ".jpg", ".png"))
                throw new IllegalArgumentException("알 수 없는 파일 확장자입니다.");

            if (oldFileName != null) {
                final Path oldPath = Paths.get(filePath.toString(), oldFileName);
                this.fileSystemStorageService.delete(oldPath);
            }
            final ProcessBuilder builder = new ProcessBuilder("mv", tmpFile.getAbsolutePath() , filePath.toString());

            try {
                final Process process = builder.start();

                final int exitCode = process.waitFor();

                if (exitCode == 0)
                    log.info("log -> INFO[info=tmp 파일 이동 성공^, file={}]", filePath);
                else
                    log.error("log -> ERROR[error=프로세스 실행 실패, exitValue={}, file={}]", exitCode, tmpFile.getPath());
            } catch (IOException e) {
                log.error("Process.execute ERROR[error={}]", e.getMessage());
            }
        } else
            log.error("log -> ERROR[error=파일 정보를 찾을 수 없습니다., tmp_file={}]", tmpFile.getPath());
    }

    public void deleteWithFileStore(String id) {
        final MaindbCustomInfoService.customInfo files = this.getRepository().getCustomImgPathById(id);
        this.getRepository().delete(id);

        if (Objects.nonNull(files))
            for (String file : files.customImgList) {
                final Path path = Paths.get(file);
                this.fileSystemStorageService.delete(path);
            }
    }

    @Data
    public static class customInfo {
        private List<String> customImgList = new ArrayList<>();
    }
}
