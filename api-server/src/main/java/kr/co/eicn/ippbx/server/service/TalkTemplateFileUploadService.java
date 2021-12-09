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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static org.apache.commons.io.FilenameUtils.getFullPath;
import static org.apache.commons.io.FilenameUtils.getName;
import static org.apache.commons.lang3.StringUtils.replaceEach;
import static org.springframework.util.StringUtils.cleanPath;

@Slf4j
@RequiredArgsConstructor
@Service
public class TalkTemplateFileUploadService extends ApiBaseService {

    private final TalkTemplateRepository repository;
    private final ImageFileStorageService imageFileStorageService;
    @Value("${file.path.chatt}")
    private String savePath;

    public Integer insertTalkTemplateFileUpload(TalkTemplateFormRequest form) {
        storeFile(form);
        return repository.insert(form);
    }

    public void updateTalkTemplateFileUpload(TalkTemplateFormRequest form, Integer seq) {
        storeFile(form);
        repository.update(seq, form);
    }

    public void storeFile(TalkTemplateFormRequest form) {
        if (TalkTemplateFormRequest.MentType.PHOTO.equals(form.getTypeMent())) {
            if (form.getFile() != null && !form.getFile().isEmpty()) {
                final MultipartFile file = form.getFile();
                final Path path = Paths.get(replaceEach(savePath, new String[]{"{0}", "{1}", "{2}"}, new String[]{g.getUser().getCompanyId(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM")), LocalDate.now().format(DateTimeFormatter.ofPattern("MMdd"))}));

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

                this.imageFileStorageService.store(path, saveFileName, file);
            } else if (form.getFile() == null && form.getFilePath().startsWith("http")) {
                final String[] dirInfo = form.getFilePath().substring(form.getFilePath().indexOf("path=") + 5, form.getFilePath().indexOf("&")).split("/");

                if (dirInfo.length == 2) {
                    final Path path = Paths.get(replaceEach(savePath, new String[]{"{0}", "{1}", "{2}"}, new String[]{g.getUser().getCompanyId(), dirInfo[0], dirInfo[1]}));
                    form.setFilePath(path.resolve(form.getOriginalFileName()).toString());
                }
            }
        }
    }

    public Resource getImage(String filePath) {
        return this.imageFileStorageService.loadImage(Paths.get(getFullPath(filePath)), getName(filePath));
    }
}
