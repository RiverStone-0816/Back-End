package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.form.TranscribeFileFormRequest;
import kr.co.eicn.ippbx.server.repository.customdb.TranscribeGroupRepository;
import kr.co.eicn.ippbx.util.UrlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.replaceEach;
import static org.springframework.util.StringUtils.cleanPath;

@Slf4j
@RequiredArgsConstructor
@Service
public class TranscribeGroupService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(TranscribeGroupService.class);
    private final Map<String, TranscribeGroupRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;
    private final FileSystemStorageService fileSystemStorageService;

    public TranscribeGroupRepository getRepository() {
        if (g.getUser() == null || StringUtils.isEmpty(g.getUser().getCompanyId()))
            throw new IllegalStateException(message.getText("messages.company.notfound"));

        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final TranscribeGroupRepository repository = new TranscribeGroupRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void uploadFile(TranscribeFileFormRequest form, Integer seq) {
        int count = 0;
        if (Objects.nonNull(form.getFiles())){
            for (MultipartFile file : form.getFiles()){
                final Path path = Paths.get(replaceEach("/stt/transcribe/"+g.getUser().getCompanyId()+"/"+seq+"/", new String[] {"{0}", "{1}"}, new String[] {g.getUser().getCompanyId(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))}));
                final String fileName = UrlUtils.decode(cleanPath(Objects.requireNonNull(file.getOriginalFilename())));

                if (file.getSize() >  10 * 1024 * 1024)
                    throw new IllegalArgumentException("최대 파일 사이즈는 10MB 까지입니다.");

                if (Files.notExists(path)) {
                    try {
                        Files.createDirectories(path);
                        count++;
                    } catch (IOException ignored) {
                    }
                }

                this.fileSystemStorageService.store(path, fileName, file);

            }
        }

        getRepository().updateCount(seq, count);
    }

}
