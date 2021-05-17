package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.repository.customdb.PrvResultCustomInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PrvResultCustomInfoService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(ResultCustomInfoService.class);
    private final Map<String, PrvResultCustomInfoRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;

    public PrvResultCustomInfoRepository getRepository(Integer groupId) {
        return repositories.computeIfAbsent(g.getUser().getCompanyId() + "_" + groupId + "_prv", tablePostfix -> {
            final PrvResultCustomInfoRepository repository = new PrvResultCustomInfoRepository(g.getUser().getCompanyId(), groupId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            repository.createTableIfNotExists();
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
