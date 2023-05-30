package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMessageData;
import kr.co.eicn.ippbx.model.dto.eicn.SendMessageHistoryResponse;
import kr.co.eicn.ippbx.model.search.SendMessageSearchRequest;
import kr.co.eicn.ippbx.server.repository.customdb.MessageDataRepository;
import kr.co.eicn.ippbx.util.page.Pagination;
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
public class MessageDataService extends ApiBaseService implements ApplicationContextAware {
    protected final Logger logger = LoggerFactory.getLogger(MessageDataService.class);

    private final Map<String, MessageDataRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;

    public MessageDataRepository getRepository() {
        if (g.getUser() == null || StringUtils.isEmpty(g.getUser().getCompanyId()))
            throw new IllegalStateException(message.getText("messages.company.notfound"));

        return  repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final MessageDataRepository repository = new MessageDataRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Pagination<SendMessageHistoryResponse> pagination(SendMessageSearchRequest search) {
        Pagination<CommonMessageData> pagination = getRepository().pagination(search);
        List<SendMessageHistoryResponse> rows = pagination.getRows().stream()
                .map(e -> {
                    final SendMessageHistoryResponse response = new SendMessageHistoryResponse();
                    response.setId(e.getSeq());
                    response.setSendDate(e.getConfirmTime());
                    response.setTarget(e.getPhoneNumber());
                    response.setResMessage(e.getConfirmMessage());
                    response.setContent(e.getMessage());
                    response.setSendType(e.getService());

                    return response;
                })
                .collect(Collectors.toList());

        return new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit());
    }
}
