package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonKakaoChatbotBlock;
import kr.co.eicn.ippbx.model.dto.customdb.ChatbotEventResponse;
import kr.co.eicn.ippbx.model.dto.customdb.ChatbotOpenBuilderInfoResponse;
import kr.co.eicn.ippbx.model.form.ChatbotOpenBuilderBlockFormRequest;
import kr.co.eicn.ippbx.model.search.ChatbotOpenBuilderBlockSearchRequest;
import kr.co.eicn.ippbx.server.repository.customdb.KakaoChatbotBlockRepository;
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
import java.util.stream.Collectors;

@Service
public class ChatbotOpenBuilderBlockService extends ApiBaseService implements ApplicationContextAware {
    private final Logger logger = LoggerFactory.getLogger(ChatbotOpenBuilderBlockService.class);
    private final Map<String, KakaoChatbotBlockRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;

    public KakaoChatbotBlockRepository getRepository() {
        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final KakaoChatbotBlockRepository repository = new KakaoChatbotBlockRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Pagination<ChatbotOpenBuilderInfoResponse> getPagination(ChatbotOpenBuilderBlockSearchRequest search) {
        Pagination<CommonKakaoChatbotBlock> pagination = getRepository().pagination(search);

        List<ChatbotOpenBuilderInfoResponse> rows = pagination.getRows().stream().map(e -> convertDto(e, ChatbotOpenBuilderInfoResponse.class)).collect(Collectors.toList());

        return new Pagination<>(rows, search.getPage(), pagination.getTotalCount(), search.getLimit());
    }

    public ChatbotOpenBuilderInfoResponse get(Integer seq) {
        final CommonKakaoChatbotBlock result = getRepository().findOneIfNullThrow(seq);

        return convertDto(result, ChatbotOpenBuilderInfoResponse.class);
    }

    public void post(ChatbotOpenBuilderBlockFormRequest form) {
        getRepository().insert(form);
    }

    public void put(Integer seq, ChatbotOpenBuilderBlockFormRequest form) {
        getRepository().updateByKey(seq, form);
    }

    public void delete(Integer seq) {
        getRepository().deleteOnIfNullThrow(seq);
    }

    public List<ChatbotEventResponse> getEventList(String botId) {
        return getRepository().findAllEventList(botId).stream().map(e -> {
            ChatbotEventResponse response = new ChatbotEventResponse();

            response.setChatbotId(e.getBotId());
            response.setChatbotName(e.getBotName());
            response.setBlockName(e.getBlockName());
            response.setEventName(e.getEventName());

            return response;
        }).collect(Collectors.toList());
    }
}
