package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.BoardInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.NoticeFileEntity;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.NoticeXFile;
import kr.co.eicn.ippbx.model.enums.BoardType;
import kr.co.eicn.ippbx.model.enums.IdType;
import kr.co.eicn.ippbx.model.form.NoticeFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.NoticeFileEntityRepository;
import kr.co.eicn.ippbx.server.repository.eicn.NoticeRepository;
import kr.co.eicn.ippbx.server.repository.eicn.NoticeXFileRepository;
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
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.replaceEach;
import static org.springframework.util.StringUtils.cleanPath;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeFileUploadService extends ApiBaseService {

    private final NoticeFileEntityRepository repository;
    private final NoticeXFileRepository xFileRepository;
    private final NoticeRepository noticeRepository;
    private final FileSystemStorageService fileSystemStorageService;
    @Value("${file.path.notice}")
    private String savePath;

    public NoticeFileEntity findOneIfNullThrow(Long id) {
        return repository.findOneIfNullThrow(id);
    }

    public void insertNoticeWithFileStore(NoticeFormRequest form) {
        final BoardInfo boardInfoRecord = new BoardInfo();
        final Long noticeId = noticeRepository.nextId();
        boardInfoRecord.setId(noticeId);
        boardInfoRecord.setTitle(form.getTitle());
        boardInfoRecord.setContent(form.getContent());
        boardInfoRecord.setBoardType(BoardType.NOTICE.getCode());
        boardInfoRecord.setCreatorId(g.getUser().getId().equals("") ? "master" : g.getUser().getId());
        boardInfoRecord.setNoticeType(form.getNoticeType());
        boardInfoRecord.setCompanyId(g.getUser().getCompanyId());

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
            noticeRepository.insert(boardInfoRecord);

            final NoticeFileEntity fileEntityRecord = new NoticeFileEntity();
            final Long fileId = repository.nextFileId();
            final String originalFileName = UrlUtils.decode(cleanPath(Objects.requireNonNull(file.getOriginalFilename())));
            final String saveFileName =  System.currentTimeMillis() + "_" + System.nanoTime() + "_" + originalFileName;

            fileEntityRecord.setId(fileId);
            fileEntityRecord.setOriginalName(originalFileName);
            fileEntityRecord.setPath(path.resolve(saveFileName).toString());
            fileEntityRecord.setSize(file.getSize());
            fileEntityRecord.setCompanyId(g.getUser().getCompanyId());

            repository.insert(fileEntityRecord);

            final NoticeXFile xFileRecord = new NoticeXFile();
            xFileRecord.setNotice(noticeId);
            xFileRecord.setFile(fileId);
            xFileRecord.setCompanyId(g.getUser().getCompanyId());

            xFileRepository.insert(xFileRecord);

            this.fileSystemStorageService.store(path, saveFileName, file);
        }
    }

    public void updateNoticeWithFileStore(NoticeFormRequest form, Long noticeId) {
        final BoardInfo boardInfoRecord = noticeRepository.findOneCheckBoardType(noticeId, false);
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
                //TODO: 파일 확장자
//                if (!StringUtils.endsWithAny(file.getOriginalFilename(), ".jpg", ".xml"))
//                    throw new IllegalArgumentException("알수없는 파일 확장자입니다.");

                if (Files.notExists(newPath)) {
                    try {
                        Files.createDirectories(newPath);
                    } catch (IOException ignored) {
                    }
                }

                final NoticeFileEntity fileEntityRecord = new NoticeFileEntity();
                final Long fileId = repository.nextFileId();

                fileEntityRecord.setId(fileId);
                fileEntityRecord.setOriginalName(originalFileName);
                fileEntityRecord.setPath(newPath.resolve(saveFileName).toString());
                fileEntityRecord.setSize(file.getSize());
                fileEntityRecord.setCompanyId(g.getUser().getCompanyId());

                repository.insert(fileEntityRecord);

                final NoticeXFile xFileRecord = new NoticeXFile();
                xFileRecord.setNotice(noticeId);
                xFileRecord.setFile(fileId);
                xFileRecord.setCompanyId(g.getUser().getCompanyId());

                xFileRepository.insert(xFileRecord);

                this.fileSystemStorageService.store(newPath, saveFileName, file);
            }
        }
        boardInfoRecord.setTitle(form.getTitle());
        boardInfoRecord.setContent(form.getContent());
        boardInfoRecord.setNoticeType(form.getNoticeType());
        noticeRepository.updateByKey(boardInfoRecord, noticeId);
    }

    public void deleteNoticeWithFileStore(Long noticeId) {
        noticeRepository.deleteCheckBoardType(noticeId);
        final List<Long> fileIds = xFileRepository.findAllNoticeXFile(noticeId);

        if (fileIds.size() > 0) {
            xFileRepository.delete(noticeId);

            for (Long fileId : fileIds) {
                final NoticeFileEntity entity = repository.findOneFile(fileId);

                final Path path = Paths.get(entity.getPath());
                this.fileSystemStorageService.delete(path);
                repository.delete(fileId);
            }
        }
    }

    public void deleteSpecificFile(Long fileId) {
        final NoticeFileEntity entity = repository.findOneFile(fileId);

        final Path path = Paths.get(entity.getPath());
        this.fileSystemStorageService.delete(path);
        repository.deleteOnIfNullThrow(fileId);

        xFileRepository.deleteOneFile(fileId);
    }
}
