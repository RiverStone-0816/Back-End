package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendCategory;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendFaxEmail;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SendFile;
import kr.co.eicn.ippbx.model.form.SendFileFormRequest;
import kr.co.eicn.ippbx.model.form.SendFileUpdateRequest;
import kr.co.eicn.ippbx.server.repository.eicn.SendFaxEmailCategoryRepository;
import kr.co.eicn.ippbx.server.repository.eicn.SendFaxEmailHistoryRepository;
import kr.co.eicn.ippbx.server.repository.eicn.SendFileRepository;
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
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.SendFaxEmail.SEND_FAX_EMAIL;

@Slf4j
@RequiredArgsConstructor
@Service
public class SendFileService extends ApiBaseService {

    private final SendFileRepository repository;
    private final SendFaxEmailCategoryRepository categoryRepository;
    private final SendFaxEmailHistoryRepository historyRepository;
    private final StorageService fileSystemStorageService;
    @Value("${file.path.send}")
    private String savePath;

    public SendFile findOneIfNullThrow(Long id) {
        return repository.findOneIfNullThrow(id);
    }

    public Long insertOnGeneratedKeyWithFileStore(SendFileFormRequest formRequest) throws IOException {
        final MultipartFile file = formRequest.getFile();
        final Path path = Paths.get(savePath, g.getUser().getCompanyId());

        final SendCategory category = categoryRepository.findOneIfNullThrow(formRequest.getCategoryCode());

        if (file.getSize() >  5 * 1024 * 1024)
            throw new IllegalArgumentException("최대 파일 사이즈는 5MB 까지입니다.");

        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException ignored) {
            }
        }

        final SendFile record = new SendFile();
        final Long nextSequence = repository.nextSequence();

        record.setId(nextSequence);
        record.setCategoryCode(category.getCategoryCode());
        record.setName(formRequest.getName());
        record.setDesc(formRequest.getDesc());
        record.setCompanyId(g.getUser().getCompanyId());
        record.setFileName(UrlUtils.decode(file.getOriginalFilename()));
        record.setPath(String.valueOf(path));

        repository.insert(record);

        this.fileSystemStorageService.store(path, file);

        return nextSequence;
    }

    public void updateWithFileStore(SendFileUpdateRequest updateRequest, Long id) throws IOException {
        final MultipartFile file = updateRequest.getFile();
        final SendFile entity = findOneIfNullThrow(id);
        final Path path = Paths.get(savePath, g.getUser().getCompanyId(), entity.getFileName());
        final Path newPath = Paths.get(savePath, g.getUser().getCompanyId());

        this.fileSystemStorageService.delete(path);

        if (Files.notExists(newPath)) {
            try {
                Files.createDirectories(newPath);
            } catch (IOException ignored) {
            }
        }

        final SendFile record = new SendFile();
        record.setName(updateRequest.getName());
        record.setDesc(updateRequest.getDesc());
        record.setFileName(UrlUtils.decode(file.getOriginalFilename()));
        record.setPath(String.valueOf(newPath));

        repository.updateByKey(record, id);
        this.fileSystemStorageService.store(newPath, file);
    }

    public void deleteWithFileStore(Long id) {
        final SendFile entity = findOneIfNullThrow(id);
        final List<SendFaxEmail> faxEmailEntity = historyRepository.findAll(SEND_FAX_EMAIL.FILE.eq(id));

        if (faxEmailEntity.size() > 0)
            throw new IllegalArgumentException("해당 발송 매체의 발송 이력이 있습니다. ");

        final Path path = Paths.get(savePath, g.getUser().getCompanyId(), entity.getFileName());
        this.fileSystemStorageService.delete(path);

        repository.deleteOnIfNullThrow(id);

    }
}
