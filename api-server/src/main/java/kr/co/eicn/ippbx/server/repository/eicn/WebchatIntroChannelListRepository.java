package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatIntroChannelList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.records.WebchatIntroChannelListRecord;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatServiceInfoResponse;
import kr.co.eicn.ippbx.model.enums.IntroChannelType;
import lombok.Getter;
import org.jooq.InsertValuesStep4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebchatIntroChannelList.WEBCHAT_INTRO_CHANNEL_LIST;

@Getter
@Repository
public class WebchatIntroChannelListRepository extends EicnBaseRepository<WebchatIntroChannelList, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatIntroChannelList, Integer> {
    private final Logger logger = LoggerFactory.getLogger(WebchatIntroChannelListRepository.class);

    WebchatIntroChannelListRepository() {
        super(WEBCHAT_INTRO_CHANNEL_LIST, WEBCHAT_INTRO_CHANNEL_LIST.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WebchatIntroChannelList.class);
    }

    public List<WebchatServiceInfoResponse.IntroChannel> findAllByIntroId(Integer introId) {
        return dsl.selectFrom(WEBCHAT_INTRO_CHANNEL_LIST)
                .where(compareCompanyId())
                .and(WEBCHAT_INTRO_CHANNEL_LIST.INTRO_ID.eq(introId))
                .orderBy(WEBCHAT_INTRO_CHANNEL_LIST.ID)
                .fetch(e -> {
                    WebchatServiceInfoResponse.IntroChannel result = new WebchatServiceInfoResponse.IntroChannel();

                    result.setId(e.getId());
                    result.setIntroId(e.getIntroId());
                    result.setChannelType(IntroChannelType.of(e.getChannelType()));
                    result.setChannelId(e.getChannelId());

                    return result;
                });
    }

    public void insertAll(List<WebchatIntroChannelListRecord> recordList) {
        if (recordList.contains(null))
            return;

        InsertValuesStep4<WebchatIntroChannelListRecord, Integer, String, String, String> insertQuery = dsl.insertInto(WEBCHAT_INTRO_CHANNEL_LIST, WEBCHAT_INTRO_CHANNEL_LIST.INTRO_ID, WEBCHAT_INTRO_CHANNEL_LIST.COMPANY_ID, WEBCHAT_INTRO_CHANNEL_LIST.CHANNEL_TYPE, WEBCHAT_INTRO_CHANNEL_LIST.CHANNEL_ID);

        recordList.forEach(e -> insertQuery.values(e.getIntroId(), getCompanyId(), e.getChannelType(), e.getChannelId()));

        insertQuery.execute();
    }

    public void deleteByIntroId(Integer introId) {
        dsl.deleteFrom(WEBCHAT_INTRO_CHANNEL_LIST)
                .where(WEBCHAT_INTRO_CHANNEL_LIST.INTRO_ID.eq(introId))
                .and(compareCompanyId())
                .execute();
    }
}
