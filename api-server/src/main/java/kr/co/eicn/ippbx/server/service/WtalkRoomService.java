package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.repository.customdb.WtalkRoomRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WtalkRoomService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(WtalkRoomService.class);
    private final Map<String, WtalkRoomRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;

    public WtalkRoomRepository getRepository() {
        if (g.getUser() == null || StringUtils.isEmpty(g.getUser().getCompanyId()))
            throw new IllegalStateException(message.getText("messages.company.notfound"));

        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final WtalkRoomRepository repository = new WtalkRoomRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}