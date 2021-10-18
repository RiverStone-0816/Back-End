package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.model.dto.customdb.ChatbotHistoryResponse;
import kr.co.eicn.ippbx.model.entity.customdb.ChatbotHistoryEntity;
import kr.co.eicn.ippbx.model.search.ChatbotHistorySearchRequest;
import kr.co.eicn.ippbx.model.search.ChatbotProfileMsgSearchRequest;
import kr.co.eicn.ippbx.server.repository.customdb.KakaoSkillMessageRepository;
import kr.co.eicn.ippbx.util.page.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
public class ChatbotHistoryService extends ApiBaseService implements ApplicationContextAware {
    private final Logger logger = LoggerFactory.getLogger(ChatbotHistoryService.class);
    private final Map<String, KakaoSkillMessageRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;

    public KakaoSkillMessageRepository getRepository() {
        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final KakaoSkillMessageRepository repository = new KakaoSkillMessageRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Pagination<ChatbotHistoryResponse> getPagination(ChatbotHistorySearchRequest search) {
        final Pagination<ChatbotHistoryEntity> pagination = getRepository().pagination(search);

        final List<ChatbotHistoryResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final ChatbotHistoryResponse response = convertDto(e, ChatbotHistoryResponse.class);
                    response.setRequestUserPlusFriendUserKey(e.getRequestUserPlusfriendUserkey());
                    return response;
                })
                .collect(toList());

        return new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit());
    }

    public List<ChatbotHistoryResponse> getChatbotMessageHistory(ChatbotProfileMsgSearchRequest search) {
        final List<ChatbotHistoryEntity> list = getRepository().getChatbotMessageHistory(search);

        return list.stream()
                .map((e) -> {
                    final ChatbotHistoryResponse response = convertDto(e, ChatbotHistoryResponse.class);
                    response.setRequestUserPlusFriendUserKey(e.getRequestUserPlusfriendUserkey());
                    return response;
                })
                .collect(toList());
    }
}
