package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonKakaoProfile;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonKakaoSkillMsg;
import kr.co.eicn.ippbx.model.entity.customdb.ChatbotHistoryEntity;
import kr.co.eicn.ippbx.model.search.ChatbotHistorySearchRequest;
import kr.co.eicn.ippbx.model.search.ChatbotProfileMsgSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkServiceInfo.WTALK_SERVICE_INFO;

@Getter
public class KakaoSkillMessageRepository extends CustomDBBaseRepository<CommonKakaoSkillMsg, ChatbotHistoryEntity, String> {
    protected final Logger logger = LoggerFactory.getLogger(KakaoSkillMessageRepository.class);

    private final CommonKakaoSkillMsg TABLE;
    private final CommonKakaoProfile KAKAO_PROFILE_TABLE;

    public KakaoSkillMessageRepository(String companyId) {
        super(new CommonKakaoSkillMsg(companyId), new CommonKakaoSkillMsg(companyId).BOT_NAME, ChatbotHistoryEntity.class);
        TABLE = new CommonKakaoSkillMsg(companyId);
        KAKAO_PROFILE_TABLE = new CommonKakaoProfile(companyId);

        addField(TABLE);
        addField(KAKAO_PROFILE_TABLE.NICKNAME);

        orderByFields.add(TABLE.INSERT_DATE.desc());
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query.leftJoin(KAKAO_PROFILE_TABLE)
                .on(KAKAO_PROFILE_TABLE.REQUEST_USER_PLUSFRIEND_USERKEY.eq(TABLE.REQUEST_USER_PLUSFRIEND_USERKEY))
                .where(KAKAO_PROFILE_TABLE.NICKNAME.isNotNull());
    }

    @Override
    protected RecordMapper<Record, ChatbotHistoryEntity> getMapper() {
        return record -> {
            final ChatbotHistoryEntity entity = record.into(TABLE).into(ChatbotHistoryEntity.class);
            entity.setNickname(record.into(KAKAO_PROFILE_TABLE.NICKNAME).value1());
            return entity;
        };
    }

    public List<ChatbotHistoryEntity> getChatbotMessageHistory(ChatbotProfileMsgSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(new java.util.Date());
        cal.add(Calendar.MINUTE, -10);

        String botId = dsl.select(WTALK_SERVICE_INFO.BOT_ID)
                .from(WTALK_SERVICE_INFO)
                .where(compareCompanyId())
                .and(WTALK_SERVICE_INFO.SENDER_KEY.eq(search.getSenderKey()))
                .fetchOneInto(String.class);

        conditions.add(TABLE.BOT_ID.eq(botId).and(TABLE.INSERT_DATE.ge(DSL.timestamp(new Date(System.currentTimeMillis()))))
                .and(TABLE.INSERT_DATE.le(DSL.timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime()))))
                .and(KAKAO_PROFILE_TABLE.REQUEST_USER_PLUSFRIEND_USERKEY.eq(search.getUserKey())));

        return findAll(conditions);
    }

    public Pagination<ChatbotHistoryEntity> pagination(ChatbotHistorySearchRequest search) {
        return super.pagination(search, conditions(search));
    }

    private List<Condition> conditions(ChatbotHistorySearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (StringUtils.isNotEmpty(search.getRequestBlockName()))
            conditions.add(TABLE.REQUEST_BLOCK_NAME.like("%" + search.getRequestBlockName() + "%"));

        if (Objects.nonNull(search.getCreatedStartDate()))
            conditions.add(TABLE.INSERT_DATE.ge(DSL.timestamp(search.getCreatedStartDate())));

        if (Objects.nonNull(search.getCreatedEndDate()))
            conditions.add(TABLE.INSERT_DATE.le(DSL.timestamp(search.getCreatedEndDate())));

        return conditions;
    }
}

