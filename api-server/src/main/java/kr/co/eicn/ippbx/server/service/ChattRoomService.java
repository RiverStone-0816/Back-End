package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattRoom;
import kr.co.eicn.ippbx.model.dto.customdb.ChattRoomResponse;
import kr.co.eicn.ippbx.model.entity.customdb.ChattRoomEntity;
import kr.co.eicn.ippbx.model.search.ChattingRoomSearchRequest;
import kr.co.eicn.ippbx.model.search.ChattingSearchRequest;
import kr.co.eicn.ippbx.server.repository.customdb.ChattRoomRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChattRoomService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(ChattRoomService.class);
    private final Map<String, ChattRoomRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;
    private final ChattMsgReadService msgReadService;
    private final PersonListRepository personListRepository;

    public ChattRoomService(ChattMsgReadService msgReadService, PersonListRepository personListRepository) {
        this.msgReadService = msgReadService;
        this.personListRepository = personListRepository;
    }

    public ChattRoomRepository getRepository() {
        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final ChattRoomRepository repository = new ChattRoomRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<ChattRoomResponse> getChattingRoomList(ChattingRoomSearchRequest search) {
        final Map<String, Integer> unreadMessageMap = msgReadService.getRepository().getUnreadMessageTotalCount();
        return this.getRepository().findAllByUserId(search).stream()
                .map(e -> {
                    final ChattRoomResponse response = convertDto(e, ChattRoomResponse.class);

                    if (Objects.nonNull(unreadMessageMap.get(e.getRoomId())))
                        response.setUnreadMessageTotalCount(unreadMessageMap.get(e.getRoomId()));

                    return response;
                })
                .collect(Collectors.toList());
    }

    public ChattRoomEntity getChattingRoom(String roomId, ChattingSearchRequest search) {
        return this.getRepository().findOneByRoomId(roomId, search);
    }

    public CommonChattRoom findOneByRoomId(String roomId) {
        return this.getRepository().findOneByRoomId(roomId);
    }

    public ChattRoomEntity findOneChattingRoom(String roomId) {
        final ChattRoomEntity entity = new ChattRoomEntity();
        ReflectionUtils.copy(entity, findOneByRoomId(roomId));

        return entity;
    }

    public String getChattingRoomIfExists(List<String> chattingMembers) {
        return this.getRepository().getChattingRoomByMemberMD5(newChattingMemberMD5("", chattingMembers));
    }

    public String getNewChattingRoomName(String oldRoomName, List<String> chattingMembers) {
        if (StringUtils.isNotEmpty(oldRoomName) && oldRoomName.contains(".."))
            return oldRoomName;
        else
            return kr.co.eicn.ippbx.util.StringUtils.subStringBytes(newChattingMemberMD5(oldRoomName, chattingMembers), 40);
    }

    public String newChattingMemberMD5(String oldRoomName, List<String> chattingMembers) {
        Collections.sort(chattingMembers);
        String chattingMemberMD5 = StringUtils.isNotEmpty(oldRoomName) ? oldRoomName : "";
        for (String member : chattingMembers) {
            final String personName = personListRepository.findOneById(member).getIdName();
            if (StringUtils.isEmpty(chattingMemberMD5))
                chattingMemberMD5 = personName;
            else
                chattingMemberMD5 = chattingMemberMD5 + "," + personName;
        }
        return chattingMemberMD5;
    }
}
