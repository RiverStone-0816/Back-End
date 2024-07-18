package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.repository.statdb.StatInboundForHuntStatRepository;
import kr.co.eicn.ippbx.server.repository.statdb.StatInboundRepository;
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
public class StatInboundService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(StatInboundService.class);
    private final Map<String, StatInboundRepository> repositories = new HashMap<>();
    private final Map<String, StatInboundForHuntStatRepository> huntRepositories = new HashMap<>();
    private ApplicationContext applicationContext;

    public StatInboundRepository getRepository() {
        if (g.getUser() == null || StringUtils.isEmpty(g.getUser().getCompanyId()))
            throw new IllegalStateException(message.getText("messages.company.notfound"));

        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final StatInboundRepository repository = new StatInboundRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    public StatInboundForHuntStatRepository getRepositoryForHuntStat() {
        if (g.getUser() == null || StringUtils.isEmpty(g.getUser().getCompanyId()))
            throw new IllegalStateException(message.getText("messages.company.notfound"));

        return huntRepositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
//            final StatInboundRepository repository = new StatInboundRepository(companyId, true);
            final StatInboundForHuntStatRepository huntRepositories = new StatInboundForHuntStatRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(huntRepositories);
            return huntRepositories;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
