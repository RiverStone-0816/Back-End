package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.form.TalkTemplateFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.TalkTemplateRepository;
import kr.co.eicn.ippbx.util.UrlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static org.apache.commons.io.FilenameUtils.getFullPath;
import static org.apache.commons.io.FilenameUtils.getName;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.springframework.util.StringUtils.cleanPath;

@Slf4j
@RequiredArgsConstructor
@Service
public class TalkTemplateFileUploadService extends ApiBaseService {

    private final TalkTemplateRepository repository;
    private final ImageFileStorageService imageFileStorageService;
    @Value("${file.path.chatbot}")
    private String savePath;

    public String insertTalkTemplateFileUpload(TalkTemplateFormRequest form) {
        String fileName = storeFile(form);
        repository.insert(form);

        return fileName;
    }

    public String updateTalkTemplateFileUpload(TalkTemplateFormRequest form, Integer seq) {
        String fileName = storeFile(form);
        repository.update(seq, form);

        return fileName;
    }

    public String storeFile(TalkTemplateFormRequest form) {
        if (TalkTemplateFormRequest.MentType.PHOTO.equals(form.getTypeMent())) {
            if (form.getFile() != null && !form.getFile().isEmpty()) {
                final MultipartFile file = form.getFile();
                final Path path = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()));

                if (Files.notExists(path)) {
                    try {
                        Files.createDirectories(path);
                    } catch (IOException ignored) {
                    }
                }

                final String originalFileName = UrlUtils.decode(cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
                final String saveFileName = System.currentTimeMillis() + "_" + System.nanoTime() + "_" + originalFileName.replaceAll("[ ()]", "");

                form.setOriginalFileName(originalFileName);
                form.setFilePath(saveFileName);

                this.imageFileStorageService.store(path, saveFileName, file);

                return saveFileName;
            } else if (form.getFile() == null && form.getFilePath().startsWith("http")) {
                final Path path = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()));
                form.setFilePath(path.resolve(form.getFilePath()).toString());

                return form.getFilePath();
            }
        }

        return "";
    }

    public Resource getImage(String fileName) {
        final Path path = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()));
        final String filePath = path.resolve(fileName).toString();

        return this.imageFileStorageService.loadImage(Paths.get(getFullPath(filePath)), getName(filePath));
    }
}
