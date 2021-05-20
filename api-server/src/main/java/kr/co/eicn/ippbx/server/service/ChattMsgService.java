package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattMsgRead;
import kr.co.eicn.ippbx.model.entity.customdb.ChattMsgEntity;
import kr.co.eicn.ippbx.model.entity.customdb.ChattRoomEntity;
import kr.co.eicn.ippbx.model.entity.customdb.ChattRoomMemberEntity;
import kr.co.eicn.ippbx.model.search.ChattingSearchRequest;
import kr.co.eicn.ippbx.server.repository.customdb.ChattMsgRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ChattMsgService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(ChattMsgService.class);
    private final Map<String, ChattMsgRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;
    private final ChattMsgReadService chattMsgReadService;
    private final PersonListRepository personListRepository;

    public ChattMsgService(ChattMsgReadService chattMsgReadService, PersonListRepository personListRepository) {
        this.chattMsgReadService = chattMsgReadService;
        this.personListRepository = personListRepository;
    }

    public ChattMsgRepository getRepository() {
        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final ChattMsgRepository repository = new ChattMsgRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<ChattMsgEntity> findAllMsgByRoomId(ChattRoomEntity roomId, ChattingSearchRequest search, Map<String, ChattRoomMemberEntity> memberEntityMap) {
        return this.getRepository().findAllMsgByRoomId(roomId, search, memberEntityMap);
    }

    public void setLastReadMessage(List<ChattRoomMemberEntity> entities, List<CommonChattMsgRead> msgReadList, String messageId) {
        final Map<String, CommonChattMsgRead> msgRead = msgReadList.stream().filter(e -> e.getMessageId().equals(messageId)).collect(Collectors.toMap(CommonChattMsgRead::getUserid, e -> e));
        for (ChattRoomMemberEntity entity : entities) {
            if (Objects.isNull(entity.getLastReadMessageId()) && Objects.isNull(msgRead.get(entity.getUserid())))
                entity.setLastReadMessageId(messageId);
        }
    }

    public void convertRoomMessage(ChattRoomEntity roomEntity, ChattingSearchRequest search, Map<String, ChattRoomMemberEntity> personMap) {
        final Map<String, ChattRoomMemberEntity> memberEntityMap = roomEntity.getChattingMembers().stream().collect(Collectors.toMap(ChattRoomMemberEntity::getUserid, e -> e));
        final Map<String, Integer> unreadMessageMap = chattMsgReadService.getRepository().getUnreadMessageCount(roomEntity.getRoomId());
        final List<CommonChattMsgRead> msgReadList = chattMsgReadService.getRepository().findAll(roomEntity.getRoomId());

        roomEntity.setChattingMessages(
                findAllMsgByRoomId(roomEntity, search, memberEntityMap).stream()
                        .map(e -> {
                            final ChattMsgEntity entity = convertDto(e, ChattMsgEntity.class);

                            if (Objects.nonNull(unreadMessageMap.get(e.getMessageId())))
                                entity.setUnreadMessageCount(unreadMessageMap.get(e.getMessageId()));
                            entity.setUserName(Objects.nonNull(personMap.get(e.getUserid())) ? personMap.get(e.getUserid()).getUserName() :
                                    (Objects.nonNull(personListRepository.findOneById(e.getUserid())) ? personListRepository.findOneById(e.getUserid()).getIdName() : e.getUserid()));

                            setLastReadMessage(roomEntity.getChattingMembers(), msgReadList, entity.getMessageId());
                            return entity;
                        })
                        .collect(Collectors.toList())
        );
    }
}
