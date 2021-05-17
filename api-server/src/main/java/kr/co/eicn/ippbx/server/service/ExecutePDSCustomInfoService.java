package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.server.model.dto.pds.ExecutePDSCustomInfoCountResponse;
import kr.co.eicn.ippbx.server.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.server.model.enums.ShellCommand;
import kr.co.eicn.ippbx.server.repository.pds.ExecutePDSCustomInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExecutePDSCustomInfoService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(ExecutePDSCustomInfoService.class);
    private final Map<String, ExecutePDSCustomInfoRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;
    private final CacheService cacheService;
    private final PBXServerInterface pbxServerInterface;

    public ExecutePDSCustomInfoRepository getRepository(String executeId) {
        if (g.getUser() == null || StringUtils.isEmpty(g.getUser().getCompanyId()))
            throw new IllegalStateException(message.getText("messages.company.notfound"));

        return repositories.computeIfAbsent(g.getUser().getCompanyId() + "_" + executeId , name -> {
            final ExecutePDSCustomInfoRepository repository = new ExecutePDSCustomInfoRepository(name);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ExecutePDSCustomInfoCountResponse findAllCount(String executeId, String runHost) {
        final ExecutePDSCustomInfoRepository repository = getRepository(executeId);
        if (!Constants.LOCAL_HOST.equals(runHost)) {
            CompanyServerEntity companyServerEntity = cacheService.pbxServerList(g.getUser().getCompanyId()).stream().filter(e -> e.getHost().equals(runHost)).findFirst().orElse(null);

            return companyServerEntity != null ? repository.findAllCount(executeId, pbxServerInterface.using(companyServerEntity.getHost(), "PDS")) : new ExecutePDSCustomInfoCountResponse();
        }

        return repository.findAllCount(executeId);
    }
}
