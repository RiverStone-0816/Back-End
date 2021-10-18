package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonKakaoProfile;
import kr.co.eicn.ippbx.model.dto.customdb.ChatKaKaoProfileInfoResponse;
import kr.co.eicn.ippbx.model.entity.customdb.ChatbotKakaoCustomerProfileEntity;
import kr.co.eicn.ippbx.model.search.ChatbotKakaoCustomerProfileSearchRequest;
import kr.co.eicn.ippbx.model.search.ChatbotProfileMsgSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkServiceInfo.TALK_SERVICE_INFO;

@Getter
public class KakaoProfileRepository extends CustomDBBaseRepository<CommonKakaoProfile, ChatbotKakaoCustomerProfileEntity, Integer> {
    private final Logger logger = LoggerFactory.getLogger(KakaoProfileRepository.class);

    private final CommonKakaoProfile TABLE;

    public KakaoProfileRepository(String companyId) {
        super(new CommonKakaoProfile(companyId), new CommonKakaoProfile(companyId).SEQ, ChatbotKakaoCustomerProfileEntity.class);
        TABLE = new CommonKakaoProfile(companyId);
    }

    public ChatKaKaoProfileInfoResponse getProfileInfo(ChatbotProfileMsgSearchRequest search) {
        String botId = dsl.select(TALK_SERVICE_INFO.BOT_ID)
                .from(TALK_SERVICE_INFO)
                .where(compareCompanyId())
                .and(TALK_SERVICE_INFO.SENDER_KEY.eq(search.getSenderKey()))
                .fetchOneInto(String.class);

        return dsl.selectFrom(TABLE)
                .where(TABLE.BOT_ID.eq(botId))
                .and(TABLE.REQUEST_USER_PLUSFRIEND_USERKEY.eq(search.getUserKey()))
                .fetchOneInto(ChatKaKaoProfileInfoResponse.class);
    }

    public Pagination<ChatbotKakaoCustomerProfileEntity> getPagination(ChatbotKakaoCustomerProfileSearchRequest search) {
        return super.pagination(search, getConditions(search));
    }

    public List<Condition> getConditions(ChatbotKakaoCustomerProfileSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (Objects.nonNull(search.getStartDate()))
            conditions.add(TABLE.INSERT_DATE.ge(Timestamp.valueOf(search.getStartDate() + " " + search.getStartHour() + ":00:00")));

        if (Objects.nonNull(search.getEndDate()))
            conditions.add(TABLE.INSERT_DATE.le(Timestamp.valueOf(search.getEndDate() + " " + search.getEndHour() + ":59:59")));

        if (StringUtils.isNotEmpty(search.getChatbotId()))
            conditions.add(TABLE.BOT_ID.eq(search.getChatbotId()));

        if (StringUtils.isNotEmpty(search.getProfileName()))
            conditions.add(TABLE.NICKNAME.like("%" + search.getProfileName() + "%"));

        if (StringUtils.isNotEmpty(search.getPhoneNumber()))
            conditions.add(TABLE.PHONE_NUMBER.like("%" + search.getPhoneNumber() + "%"));

        return conditions;
    }
}
