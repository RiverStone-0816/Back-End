package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonWtalkMsg;
import kr.co.eicn.ippbx.model.entity.customdb.TalkMsgEntity;
import kr.co.eicn.ippbx.server.repository.customdb.TalkMsgRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TalkMsgService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(TalkMsgService.class);
    private final Map<String, TalkMsgRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;

    public TalkMsgRepository getRepository() {
        if (g.getUser() == null || StringUtils.isEmpty(g.getUser().getCompanyId()))
            throw new IllegalStateException(message.getText("messages.company.notfound"));

        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final TalkMsgRepository repository = new TalkMsgRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Map<String, TalkMsgEntity> getAllLastMessageByRoomId(Set<String> roomIdList) {
        return getRepository().getAllLastMessagesInRoomIds(roomIdList).stream().collect(Collectors.toMap(CommonWtalkMsg::getRoomId, e -> e));
    }
}
