package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.FileEntity;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TaskScript;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TaskScriptCategory;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TaskScriptXFile;
import kr.co.eicn.ippbx.server.model.form.TaskScriptFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.TaskFileEntityRepository;
import kr.co.eicn.ippbx.server.repository.eicn.TaskScriptCategoryRepository;
import kr.co.eicn.ippbx.server.repository.eicn.TaskScriptRepository;
import kr.co.eicn.ippbx.server.repository.eicn.TaskScriptXFileRepository;
import kr.co.eicn.ippbx.server.util.UrlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.FILE_ENTITY;
import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.TASK_SCRIPT_CATEGORY;
import static org.apache.commons.lang3.StringUtils.replaceEach;
import static org.springframework.util.StringUtils.cleanPath;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaskFileUploadService extends ApiBaseService {

    private final TaskFileEntityRepository repository;
    private final TaskScriptRepository taskScriptRepository;
    private final TaskScriptXFileRepository xFileRepository;
    private final TaskScriptCategoryRepository categoryRepository;
    private final FileSystemStorageService fileSystemStorageService;
    @Value("${file.path.task-script}")
    private String savePath;

    public FileEntity findOneIfNullThrow(Long id) {
        return repository.findOneIfNullThrow(id);
    }

    public void insertFileEntityWithFileStore(TaskScriptFormRequest form) {

        final TaskScriptCategory scriptCategory = categoryRepository.findOne(TASK_SCRIPT_CATEGORY.ID.eq(form.getCategoryId()));
        final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TaskScript scriptRecord = new kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TaskScript();
        final Long scriptId = taskScriptRepository.nextId();

        scriptRecord.setId(scriptId);
        scriptRecord.setCategory(scriptCategory.getId());
        scriptRecord.setTag(form.getTag());
        scriptRecord.setTitle(form.getTitle());
        scriptRecord.setContent(form.getContent());
        scriptRecord.setCompanyId(g.getUser().getCompanyId());

        for (MultipartFile file : form.getFiles()) {
            final Path path = Paths.get(replaceEach(savePath, new String[] {"{0}", "{1}"}, new String[] {g.getUser().getCompanyId(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))}));

            if (file.getSize() >  5 * 1024 * 1024)
                throw new IllegalArgumentException("최대 파일 사이즈는 5MB 까지입니다.");
            //TODO: 파일 확장자
//            if (!StringUtils.endsWithAny(file.getOriginalFilename(), ".jpg", ".xml"))
//                throw new IllegalArgumentException("알수없는 파일 확장자입니다.");

            if (Files.notExists(path)) {
                try {
                    Files.createDirectories(path);
                } catch (IOException ignored) {
                }
            }
            taskScriptRepository.insertOnGeneratedKey(scriptRecord);

            final FileEntity fileEntityRecord = new FileEntity();
            final Long fileId = repository.nextFileId();
            final String originalFileName = UrlUtils.decode(cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
            final String saveFileName =  System.currentTimeMillis() + "_" + System.nanoTime() + "_" + originalFileName;

            fileEntityRecord.setId(fileId);
            fileEntityRecord.setOriginalName(originalFileName);
            fileEntityRecord.setPath(path.resolve(saveFileName).toString());
            fileEntityRecord.setSize(file.getSize());
            fileEntityRecord.setCompanyId(g.getUser().getCompanyId());

            final Record record = repository.insertOnGeneratedKey(fileEntityRecord);

            final TaskScriptXFile xFileRecord = new TaskScriptXFile();
            xFileRecord.setTaskScript(scriptId);
            xFileRecord.setFile(record.getValue(FILE_ENTITY.ID));
            xFileRecord.setCompanyId(g.getUser().getCompanyId());

            xFileRepository.insert(xFileRecord);
            this.fileSystemStorageService.store(path, saveFileName, file);
        }
    }

    public void updateNoticeWithFileStore(TaskScriptFormRequest form, Long id) {
        final TaskScript scriptRecord = taskScriptRepository.findOneIfNullThrow(id);

        if (Objects.nonNull(form.getFiles())) {
            for (MultipartFile file : form.getFiles()) {
                final Path newPath = Paths.get(replaceEach(savePath, new String[] {"{0}", "{1}"}, new String[] {g.getUser().getCompanyId(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))}));
                final String originalFileName = UrlUtils.decode(cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
                final String saveFileName =  System.currentTimeMillis() + "_" + System.nanoTime() + "_" + originalFileName;

                if (file.getSize() > 5 * 1024 * 1024)
                    throw new IllegalArgumentException("최대 파일 사이즈는 5MB 까지입니다.");
                //TODO: 파일 확장자
//                if (!StringUtils.endsWithAny(file.getOriginalFilename(), ".jpg", ".xml"))
//                    throw new IllegalArgumentException("알수없는 파일 확장자입니다.");

                final List<FileEntity> duplicatedFileNameList = repository.getOriginalFileName(file.getOriginalFilename());
                if (duplicatedFileNameList.size() > 0)
                    throw new IllegalArgumentException(file.getOriginalFilename() + "은(는) 이미 존재하는 파일 입니다. 삭제후 다시 업로드해 주세요.");

                if (Files.notExists(newPath)) {
                    try {
                        Files.createDirectories(newPath);
                    } catch (IOException ignored) {
                    }
                }

                final FileEntity fileEntityRecord = new FileEntity();
                final Long fileId = repository.nextFileId();

                fileEntityRecord.setId(fileId);
                fileEntityRecord.setOriginalName(originalFileName);
                fileEntityRecord.setPath(newPath.resolve(saveFileName).toString());
                fileEntityRecord.setSize(file.getSize());
                fileEntityRecord.setCompanyId(g.getUser().getCompanyId());

                repository.insert(fileEntityRecord);

                final TaskScriptXFile xFileRecord = new TaskScriptXFile();
                xFileRecord.setTaskScript(id);
                xFileRecord.setFile(fileId);
                xFileRecord.setCompanyId(g.getUser().getCompanyId());

                xFileRepository.insert(xFileRecord);
                this.fileSystemStorageService.store(newPath, saveFileName, file);
            }
        }
        scriptRecord.setCategory(form.getCategoryId());
        scriptRecord.setTag(form.getTag());
        scriptRecord.setTitle(form.getTitle());
        scriptRecord.setContent(form.getContent());
        taskScriptRepository.updateByKey(scriptRecord, id);
    }

    public void deleteFileEntityWithFileStore(Long scriptId) {
        taskScriptRepository.findOneIfNullThrow(scriptId);
        final List<Long> fileIds = xFileRepository.findAllScriptXFile(scriptId);

        if (fileIds.size() > 0) {
            xFileRepository.delete(scriptId);

            for (Long fileId : fileIds) {
                final FileEntity entity = repository.findOneFile(fileId);

                final Path path = Paths.get(entity.getPath());
                this.fileSystemStorageService.delete(path);
                repository.delete(fileId);
            }
        }
        taskScriptRepository.deleteOnIfNullThrow(scriptId);
    }

    public void deleteSpecificFile(Long fileId) {
        final FileEntity entity = repository.findOneFile(fileId);

        final Path path = Paths.get(entity.getPath());
        this.fileSystemStorageService.delete(path);

        xFileRepository.deleteOneFile(fileId);
        repository.deleteOnIfNullThrow(fileId);
    }
}
