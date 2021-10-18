package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkServiceInfo;
import kr.co.eicn.ippbx.model.dto.customdb.ChatbotKakaoCustomerProfileResponse;
import kr.co.eicn.ippbx.model.dto.customdb.ChatbotSendEventDataResponse;
import kr.co.eicn.ippbx.model.entity.customdb.ChatbotKakaoCustomerProfileEntity;
import kr.co.eicn.ippbx.model.search.ChatbotKakaoCustomerProfileSearchRequest;
import kr.co.eicn.ippbx.server.repository.customdb.KakaoProfileRepository;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Service
public class ChatbotKakakoCustomerProfileService extends ApiBaseService implements ApplicationContextAware {
    private final Logger logger = LoggerFactory.getLogger(ChatbotKakakoCustomerProfileService.class);
    private final Map<String, KakaoProfileRepository> repositories = new HashMap<>();
    private ApplicationContext applicationContext;

    private final TalkServiceInfoService talkServiceInfoService;

    public KakaoProfileRepository getRepository() {
        return repositories.computeIfAbsent(g.getUser().getCompanyId(), companyId -> {
            final KakaoProfileRepository repository = new KakaoProfileRepository(companyId);
            applicationContext.getAutowireCapableBeanFactory().autowireBean(repository);
            return repository;
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Pagination<ChatbotKakaoCustomerProfileResponse> getPagination(ChatbotKakaoCustomerProfileSearchRequest search) {
        final Pagination<ChatbotKakaoCustomerProfileEntity> pagination = getRepository().getPagination(search);
        final Map<String, String> botNameByIdMap = talkServiceInfoService.getAllTalkServiceList().stream().collect(Collectors.toMap(TalkServiceInfo::getBotId, TalkServiceInfo::getBotName));

        final List<ChatbotKakaoCustomerProfileResponse> rows = pagination.getRows().stream().map(e -> convertDto(e, botNameByIdMap)).collect(Collectors.toList());

        return new Pagination<>(rows, search.getPage(), pagination.getTotalCount(), search.getLimit());
    }

    public ChatbotSendEventDataResponse get(Integer seq) {
        final Map<String, String> botNameByIdMap = talkServiceInfoService.getAllTalkServiceList().stream().collect(Collectors.toMap(TalkServiceInfo::getBotId, TalkServiceInfo::getBotName));
        final ChatbotKakaoCustomerProfileEntity result = getRepository().findOneIfNullThrow(seq);
        final ChatbotSendEventDataResponse response = convertDto(result, ChatbotSendEventDataResponse.class);

        response.setBotName(botNameByIdMap.get(response.getBotId()));

        return response;
    }

    private ChatbotKakaoCustomerProfileResponse convertDto(ChatbotKakaoCustomerProfileEntity entity, Map<String, String> botNameByIdMap) {
        final ChatbotKakaoCustomerProfileResponse response = new ChatbotKakaoCustomerProfileResponse();

        response.setSeq(entity.getSeq());
        response.setAuthenticationDate(entity.getInsertDate());
        response.setChatbotName(botNameByIdMap.get(entity.getBotId()));
        response.setRequestUserPlusFriendUserKey(entity.getRequestUserPlusfriendUserkey());
        response.setProfileName(entity.getNickname());
        response.setPhoneNumber(entity.getPhoneNumber());
        response.setMaindbCustomName(entity.getMaindbCustomName());

        return response;
    }
}
