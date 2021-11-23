package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkScheduleGroupList;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkScheduleGroupListDetailResponse extends TalkScheduleGroupList {
    private String chatBot; //챗봇
    private String talkGroup; //채팅상담그룹
}
