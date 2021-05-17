package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.repository.customdb.MaindbKeyInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MaindbKeyInfoService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(MaindbCustomInfoService.class);
    private final Map<String, MaindbKeyInfoRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;

    public MaindbKeyInfoRepository getRepository() {
        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final MaindbKeyInfoRepository repository = new MaindbKeyInfoRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
