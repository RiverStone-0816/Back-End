package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.form.TalkTemplateFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.TalkTemplateRepository;
import kr.co.eicn.ippbx.util.UrlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkTemplate.TALK_TEMPLATE;
import static org.apache.commons.lang3.StringUtils.replaceEach;
import static org.springframework.util.StringUtils.cleanPath;

@Slf4j
@RequiredArgsConstructor
@Service
public class TalkTemplateFileUploadService extends ApiBaseService {

    private final TalkTemplateRepository repository;
    private final FileSystemStorageService fileSystemStorageService;
    @Value("${file.path.notice}")
    private String savePath;

    public Integer insertTalkTemplateFileUpload(TalkTemplateFormRequest form) {
        final MultipartFile file = form.getFile();
        final Path path = Paths.get(replaceEach(savePath, new String[]{"{0}", "{1}"}, new String[]{g.getUser().getCompanyId(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))}));

        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException ignored) {
            }
        }

        final String originalFileName = UrlUtils.decode(cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
        final String saveFileName = System.currentTimeMillis() + "_" + System.nanoTime() + "_" + originalFileName;

        form.setOriginalFileName(originalFileName);
        form.setFilePath(path.resolve(saveFileName).toString());

        this.fileSystemStorageService.store(path, saveFileName, file);

        return repository.insertOnGeneratedKey(form).getValue(TALK_TEMPLATE.SEQ);
    }

    public void updateTalkTemplateFileUpload(TalkTemplateFormRequest form, Integer seq) {
        if (form.getFile() != null && !form.getFile().isEmpty()) {
            final MultipartFile file = form.getFile();
            final Path path = Paths.get(replaceEach(savePath, new String[]{"{0}", "{1}"}, new String[]{g.getUser().getCompanyId(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))}));

            if (Files.notExists(path)) {
                try {
                    Files.createDirectories(path);
                } catch (IOException ignored) {
                }
            }

            final String originalFileName = UrlUtils.decode(cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
            final String saveFileName = System.currentTimeMillis() + "_" + System.nanoTime() + "_" + originalFileName;

            form.setOriginalFileName(originalFileName);
            form.setFilePath(path.resolve(saveFileName).toString());

            this.fileSystemStorageService.store(path, saveFileName, file);
        }
        repository.updateByKey(form, seq);
    }

}
