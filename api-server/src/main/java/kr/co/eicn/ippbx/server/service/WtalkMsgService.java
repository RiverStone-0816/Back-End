package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonWtalkMsg;
import kr.co.eicn.ippbx.model.entity.customdb.WtalkMsgEntity;
import kr.co.eicn.ippbx.server.repository.customdb.WtalkMsgRepository;
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
public class WtalkMsgService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(WtalkMsgService.class);
    private final Map<String, WtalkMsgRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;

    public WtalkMsgRepository getRepository() {
        if (g.getUser() == null || StringUtils.isEmpty(g.getUser().getCompanyId()))
            throw new IllegalStateException(message.getText("messages.company.notfound"));

        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final WtalkMsgRepository repository = new WtalkMsgRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Map<String, WtalkMsgEntity> getAllLastMessageByRoomId(Set<String> roomIdList) {
        return getRepository().getAllLastMessagesInRoomIds(roomIdList).stream().collect(Collectors.toMap(CommonWtalkMsg::getRoomId, e -> e));
    }
}
