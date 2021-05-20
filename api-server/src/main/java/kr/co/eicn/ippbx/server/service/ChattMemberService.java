package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattRoomMember;
import kr.co.eicn.ippbx.server.repository.customdb.ChattMemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChattMemberService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(ChattMemberService.class);
    private final Map<String, ChattMemberRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;

    public ChattMemberRepository getRepository() {
        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final ChattMemberRepository repository = new ChattMemberRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<CommonChattRoomMember> findAllByRoomId(String roomId) {
        return this.getRepository().findAllByRoomId(roomId);
    }

    public List<String> findMemberByRoomId(String roomId) {
        return this.getRepository().findMemberByRoomId(roomId);
    }
}
