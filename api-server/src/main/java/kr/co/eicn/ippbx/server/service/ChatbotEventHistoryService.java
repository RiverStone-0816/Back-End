package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonKakaoEvent;
import kr.co.eicn.ippbx.model.dto.customdb.ChatbotEventHistoryResponse;
import kr.co.eicn.ippbx.model.form.ChatbotSendEventFormRequest;
import kr.co.eicn.ippbx.model.search.ChatbotEventHistorySearchRequest;
import kr.co.eicn.ippbx.server.repository.customdb.KakaoEventRepository;
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
public class ChatbotEventHistoryService extends ApiBaseService implements ApplicationContextAware {
    private final Logger logger = LoggerFactory.getLogger(ChatbotEventHistoryService.class);
    private final Map<String, KakaoEventRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;

    private KakaoEventRepository getRepository() {
        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final KakaoEventRepository repository = new KakaoEventRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Pagination<ChatbotEventHistoryResponse> getPagination(ChatbotEventHistorySearchRequest search) {
        final Pagination<CommonKakaoEvent> pagination = getRepository().getPagination(search);
        final List<ChatbotEventHistoryResponse> rows = pagination.getRows().stream().map(e -> {
            final ChatbotEventHistoryResponse response = new ChatbotEventHistoryResponse();

            response.setSendTime(e.getInsertDate());
            response.setChatbotName(e.getBotName());
            response.setEventName(e.getEventName());
            response.setProfileName(e.getUserName());
            response.setEventData(e.getUserData());
            response.setBlockName(e.getResponseBlockName());
            response.setIsReceive(StringUtils.isEmpty(e.getUserData()) || StringUtils.isEmpty(e.getResponseBlockName()) ? "" : "수신완료");

            return response;
        }).collect(Collectors.toList());

        return new Pagination<>(rows, search.getPage(), pagination.getTotalCount(), search.getLimit());
    }

    public Integer insert(ChatbotSendEventFormRequest form) {
        return getRepository().insert(form);
    }
}
