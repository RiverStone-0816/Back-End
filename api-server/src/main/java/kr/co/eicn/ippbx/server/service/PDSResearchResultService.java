package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.repository.eicn.HistoryPDSGroupRepository;
import kr.co.eicn.ippbx.server.repository.pds.PDSResearchResultRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PDSResearchResultService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(PDSResearchResultService.class);
    private final Map<String, PDSResearchResultRepository> repositories = new HashMap<>();
    private final PDSCustomInfoService pdsCustomInfoService;
    private final HistoryPDSGroupRepository historyPDSGroupRepository;
    private ApplicationContext applicationContext;

    public PDSResearchResultService(PDSCustomInfoService pdsCustomInfoService, HistoryPDSGroupRepository historyPDSGroupRepository) {
        this.pdsCustomInfoService = pdsCustomInfoService;
        this.historyPDSGroupRepository = historyPDSGroupRepository;
    }

    public PDSResearchResultRepository getRepository(String executeId) {
        return repositories.computeIfAbsent(executeId, name -> {
            final PDSResearchResultRepository repository = new PDSResearchResultRepository(name, pdsCustomInfoService, historyPDSGroupRepository);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
