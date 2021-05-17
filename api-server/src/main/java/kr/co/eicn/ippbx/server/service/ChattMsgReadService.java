package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.repository.customdb.ChattMsgReadRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChattMsgReadService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(ChattMsgReadService.class);
    private final Map<String, ChattMsgReadRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;

    public ChattMsgReadRepository getRepository() {
        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final ChattMsgReadRepository repository = new ChattMsgReadRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
