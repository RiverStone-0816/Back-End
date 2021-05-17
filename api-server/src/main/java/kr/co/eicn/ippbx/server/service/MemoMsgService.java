package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.server.model.entity.customdb.MemoMsgEntity;
import kr.co.eicn.ippbx.server.model.enums.Bool;
import kr.co.eicn.ippbx.server.model.enums.ChattingSendReceive;
import kr.co.eicn.ippbx.server.model.search.MemoMsgSearchRequest;
import kr.co.eicn.ippbx.server.repository.customdb.MemoMsgRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.replace;

@Service
public class MemoMsgService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(MemoMsgService.class);
    private final Map<String, MemoMsgRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;
    private final PersonListRepository personListRepository;
    private final FileSystemStorageService fileSystemStorageService;
    @Value("${file.path.profile}")
    private String savePath;
    @Value("${file.path.temporary.default")
    private String temporaryPath;

    public MemoMsgService(PersonListRepository personListRepository, FileSystemStorageService fileSystemStorageService) {
        this.personListRepository = personListRepository;
        this.fileSystemStorageService = fileSystemStorageService;
    }

    public MemoMsgRepository getRepository() {
        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final MemoMsgRepository repository = new MemoMsgRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Integer getUnreadMessageCount() {
        return this.getRepository().getUnreadMessageCount();
    }

    public Pagination<MemoMsgEntity> getSendMemoList(MemoMsgSearchRequest search) {
        return this.getRepository().getSendMemoList(search);
    }

    public Pagination<MemoMsgEntity> getReceiveMemoList(MemoMsgSearchRequest search) {
        return this.getRepository().getReceiveMemoList(search);
    }

    public MemoMsgEntity getMemoMessage(Integer seq) {
        return convertToMemoUserName(this.getRepository().findOne(seq));
    }

    public String getProfilePhotoSavePath(String fileName) {
        if (fileName.contains(temporaryPath))
            return fileName;
        else
            return String.valueOf(Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()), fileName));
    }

    public String updateProfilePhoto(MultipartFile file) {
        final String oldFileName = personListRepository.findOneById(g.getUser().getId()).getProfilePhoto();
        final String saveFileName = g.getUser().getId().concat("_") + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).concat("_") + file.getOriginalFilename();
        final Path newPath = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()));
        final Path oldFile = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()), oldFileName);

        if (!StringUtils.endsWithAny(Objects.requireNonNull(file.getOriginalFilename()).toLowerCase(), ".jpg", ".png"))
            throw new IllegalArgumentException("알 수 없는 파일 확장자입니다.");

        if (Files.exists(oldFile))
            this.fileSystemStorageService.delete(oldFile);

        if (Files.notExists(newPath)) {
            try {
                Files.createDirectories(newPath);
            } catch (IOException ignored) {
            }
        }

        personListRepository.updateProfilePhoto(saveFileName);
        this.fileSystemStorageService.store(newPath, saveFileName, file);

        return saveFileName;
    }

    public void deleteProfilePhoto() {
        final String fileName = personListRepository.findOneById(g.getUser().getId()).getProfilePhoto();
        final Path filePath = Paths.get(replace(savePath, "{0}", g.getUser().getCompanyId()), fileName);
        personListRepository.updateProfilePhoto("");
        this.fileSystemStorageService.delete(filePath);
    }

    public List<MemoMsgEntity> convertToMemoUserName(List<MemoMsgEntity> entities) {
        final Map<String, String> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, PersonList::getIdName));
        final Map<String, List<MemoMsgEntity>> memoMessageMap = this.getRepository().getReceiveUsers();

        return entities.stream()
                .peek((e) -> {
                    e.setSendUserName(Objects.nonNull(personListMap.get(e.getSendUserid())) ? personListMap.get(e.getSendUserid()) : e.getSendUserid());
                    if (Objects.nonNull(memoMessageMap.get(e.getMessageId())))
                        e.setReceiveUserNames(
                                memoMessageMap.get(e.getMessageId()).stream()
                                        .map((m) -> {
                                            final UserInfo userInfo = new UserInfo();

                                            if (Bool.Y.name().equals(m.getReadYn()))
                                                e.getReadReceiveUserInfos().add(convertToUserInfo(userInfo, m.getReceiveUserid(), personListMap));
                                            if (Bool.N.name().equals(m.getReadYn()))
                                                e.getUnreadReceiveUserInfos().add(convertToUserInfo(userInfo, m.getReceiveUserid(), personListMap));
                                            return Objects.nonNull(personListMap.get(m.getReceiveUserid())) ? personListMap.get(m.getReceiveUserid()) : m.getReceiveUserid();
                                        })
                                        .collect(Collectors.toList())
                        );
                })
                .collect(Collectors.toList());
    }

    public MemoMsgEntity convertToMemoUserName(MemoMsgEntity entity) {
        final UserInfo userInfo = new UserInfo();
        final Map<String, String> personListMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, PersonList::getIdName));
        final List<MemoMsgEntity> receiveUsers = this.getRepository().getReceiveUserByMessageId(entity.getMessageId());
        if (!entity.getSendReceive().equals(ChattingSendReceive.SEND.getCode()) && entity.getReadYn().equals(Bool.N.name()))
            this.getRepository().updateReadYn(entity.getSeq());

        entity.setSendUserName(Objects.nonNull(personListMap.get(entity.getSendUserid())) ? personListMap.get(entity.getSendUserid()) : entity.getSendUserid());

        if (Objects.nonNull(receiveUsers))
            entity.setReceiveUserNames(
                    receiveUsers.stream()
                            .map(e -> {
                                if (e.getReadYn().equals(Bool.Y.name()))
                                    entity.getReadReceiveUserInfos().add(convertToUserInfo(userInfo, e.getReceiveUserid(), personListMap));
                                if (e.getReadYn().equals(Bool.N.name()))
                                    entity.getUnreadReceiveUserInfos().add(convertToUserInfo(userInfo, e.getReceiveUserid(), personListMap));

                                return Objects.nonNull(personListMap.get(e.getReceiveUserid())) ? personListMap.get(e.getReceiveUserid()) : e.getReceiveUserid();
                            })
                            .collect(Collectors.toList())
            );
        return entity;
    }

    public MemoMsgEntity.UserInfo convertToUserInfo(UserInfo userInfo, String userId, Map<String, String> personListMap) {
        userInfo.setUserId(userId);
        userInfo.setUserName(Objects.nonNull(personListMap.get(userId)) ? personListMap.get(userId) : userId);
        return convertDto(userInfo, MemoMsgEntity.UserInfo.class);
    }

    @Data
    static class UserInfo {
        private String userId;
        private String userName;
    }
}
