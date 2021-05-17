package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.IvrTree;
import kr.co.eicn.ippbx.server.model.dto.statdb.StatCategoryIvrPathResponse;
import kr.co.eicn.ippbx.server.model.dto.statdb.StatInboundResponse;
import kr.co.eicn.ippbx.server.model.entity.statdb.StatInboundEntity;
import kr.co.eicn.ippbx.server.repository.statdb.StatCategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatCategoryService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(StatCategoryService.class);
    private final Map<String, StatCategoryRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;

    public StatCategoryRepository getRepository() {
        if (g.getUser() == null || StringUtils.isEmpty(g.getUser().getCompanyId()))
            throw new IllegalStateException(message.getText("messages.company.notfound"));

        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final StatCategoryRepository repository = new StatCategoryRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<StatCategoryIvrPathResponse> convertIvrPath(List<IvrTree> ivrTrees, List<StatInboundEntity> statInboundList) {
        final List<IvrTree> firstTrees = ivrTrees.stream().filter(e -> !e.getButton().equals("") && e.getButton() != null).collect(Collectors.toList());

        return firstTrees.stream()
                .map(e -> {
                    final StatCategoryIvrPathResponse ivrPathResponse = convertDto(e, StatCategoryIvrPathResponse.class);

                    ivrPathResponse.setRecord(
                            statInboundList.stream().filter(stat -> e.getTreeName().equals(stat.getIvrTreeName()))
                                    .map(stat -> modelMapper.map(stat, StatInboundResponse.class))
                                    .findFirst().orElse(new StatInboundResponse())
                    );

                    return ivrPathResponse;
                }).collect(Collectors.toList());
    }
}