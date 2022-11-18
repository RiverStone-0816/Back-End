package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.dto.customdb.PersonLastStatusInfoResponse;
import kr.co.eicn.ippbx.server.repository.customdb.MemberStatusRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class MemberStatusService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(StatInboundService.class);
    private final Map<String, MemberStatusRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;

    public MemberStatusRepository getRepository() {
        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final MemberStatusRepository repository = new MemberStatusRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<PersonLastStatusInfoResponse> getAllPersonStatusInfo() {
        final MemberStatusRepository repository = getRepository();

        return repository.findAllMemberStatusTime();
    }
}
