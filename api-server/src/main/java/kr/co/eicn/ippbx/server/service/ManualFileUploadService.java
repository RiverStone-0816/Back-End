package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.BoardInfo;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ManualFileEntity;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ManualXFile;
import kr.co.eicn.ippbx.server.model.enums.BoardType;
import kr.co.eicn.ippbx.server.model.enums.IdType;
import kr.co.eicn.ippbx.server.model.form.ManualFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.ManualFileEntityRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ManualRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ManualXFileRepository;
import kr.co.eicn.ippbx.server.util.UrlUtils;
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
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.replaceEach;
import static org.springframework.util.StringUtils.cleanPath;

@Slf4j
@RequiredArgsConstructor
@Service
public class ManualFileUploadService extends ApiBaseService {

    private final ManualFileEntityRepository repository;
    private final ManualXFileRepository xFileRepository;
    private final ManualRepository manualRepository;
    private final FileSystemStorageService fileSystemStorageService;
    @Value("${file.path.manual}")
    private String savePath;

    public ManualFileEntity findOneIfNullThrow(Long id) {
        return repository.findOneIfNullThrow(id);
    }

    public void insertManualWithFileStore(ManualFormRequest form) {
        final BoardInfo boardInfoRecord = new BoardInfo();
        final Long manualId = manualRepository.nextId();
        boardInfoRecord.setId(manualId);
        boardInfoRecord.setTitle(form.getTitle());
        boardInfoRecord.setBoardType(BoardType.MANUAL.getCode());
        boardInfoRecord.setCreatorId(g.getUser().getId().equals("") ? "master" : g.getUser().getId());
        boardInfoRecord.setCompanyId(g.getUser().getCompanyId());

        for (MultipartFile file : form.getFiles()) {
            final Path path = Paths.get(replaceEach(savePath, new String[] {"{0}", "{1}"}, new String[] {g.getUser().getCompanyId(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))}));

            if (file.getSize() >  5 * 1024 * 1024)
                throw new IllegalArgumentException("최대 파일 사이즈는 5MB 까지입니다.");
//            if (!StringUtils.endsWithAny(file.getOriginalFilename(), ".jpg", ".xml"))
//                throw new IllegalArgumentException("알수없는 파일 확장자입니다.");

            if (Files.notExists(path)) {
                try {
                    Files.createDirectories(path);
                } catch (IOException ignored) {
                }
            }
            manualRepository.insert(boardInfoRecord);

            final ManualFileEntity fileEntityRecord = new ManualFileEntity();
            final Long fileId = repository.nextFileId();
            final String originalFileName = UrlUtils.decode(cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
            final String saveFileName =  System.currentTimeMillis() + "_" + System.nanoTime() + "_" + originalFileName;

            fileEntityRecord.setId(fileId);
            fileEntityRecord.setOriginalName(originalFileName);
            fileEntityRecord.setPath(path.resolve(saveFileName).toString());
            fileEntityRecord.setSize(file.getSize());
            fileEntityRecord.setCompanyId(g.getUser().getCompanyId());

            repository.insert(fileEntityRecord);

            final ManualXFile xFileRecord = new ManualXFile();
            xFileRecord.setManual(manualId);
            xFileRecord.setFile(fileId);
            xFileRecord.setCompanyId(g.getUser().getCompanyId());

            xFileRepository.insert(xFileRecord);

            this.fileSystemStorageService.store(path, saveFileName, file);
        }
    }

    public void updateManualWithFileStore(ManualFormRequest form, Long id) {
        final BoardInfo boardInfoRecord = manualRepository.findOneCheckBoardType(id, false);
        if (!g.getUser().getIdType().equals(IdType.MASTER.getCode()) && !g.getUser().getIdType().equals(IdType.SUPER_ADMIN.getCode())
                && !boardInfoRecord.getCreatorId().equals(g.getUser().getId()))
            throw new IllegalArgumentException("해당 게시글을 수정할 수 없습니다.");

        if (Objects.nonNull(form.getFiles())) {
            for (MultipartFile file : form.getFiles()) {
                final Path newPath = Paths.get(replaceEach(savePath, new String[] {"{0}", "{1}"}, new String[] {g.getUser().getCompanyId(), LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"))}));
                final String originalFileName = UrlUtils.decode(cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
                final String saveFileName =  System.currentTimeMillis() + "_" + System.nanoTime() + "_" + originalFileName;

                if (file.getSize() > 5 * 1024 * 1024)
                    throw new IllegalArgumentException("최대 파일 사이즈는 5MB 까지입니다.");
//                if (!StringUtils.endsWithAny(file.getOriginalFilename(), ".jpg", ".xml", ".txt", ".hwp"))
//                    throw new IllegalArgumentException("알수없는 파일 확장자입니다.");

                if (Files.notExists(newPath)) {
                    try {
                        Files.createDirectories(newPath);
                    } catch (IOException ignored) {
                    }
                }

                final ManualFileEntity fileEntityRecord = new ManualFileEntity();
                final Long fileId = repository.nextFileId();

                fileEntityRecord.setId(fileId);
                fileEntityRecord.setOriginalName(originalFileName);
                fileEntityRecord.setPath(newPath.resolve(saveFileName).toString());
                fileEntityRecord.setSize(file.getSize());
                fileEntityRecord.setCompanyId(g.getUser().getCompanyId());

                repository.insert(fileEntityRecord);

                final ManualXFile xFileRecord = new ManualXFile();
                xFileRecord.setManual(id);
                xFileRecord.setFile(fileId);
                xFileRecord.setCompanyId(g.getUser().getCompanyId());

                xFileRepository.insert(xFileRecord);

                this.fileSystemStorageService.store(newPath, saveFileName, file);
            }
        }
        boardInfoRecord.setTitle(form.getTitle());
        manualRepository.updateByKey(boardInfoRecord, id);
    }

    public void deleteFileEntityWithFileStore(Long manualId) {
        manualRepository.deleteCheckBoardType(manualId);
        final List<Long> fileIds = xFileRepository.findAllManualXFile(manualId);

        if (fileIds.size() > 0) {
            xFileRepository.delete(manualId);

            for (Long fileId : fileIds) {
                final ManualFileEntity entity = repository.findOneFile(fileId);
                final Path path = Paths.get(entity.getPath());

                this.fileSystemStorageService.delete(path);
                repository.delete(fileId);
            }
        }
    }

    public void deleteSpecificFile(Long fileId) {
        final ManualFileEntity entity = repository.findOneFile(fileId);

        final Path path = Paths.get(entity.getPath());
        this.fileSystemStorageService.delete(path);
        repository.deleteOnIfNullThrow(fileId);

        xFileRepository.deleteOneFile(fileId);
    }
}
