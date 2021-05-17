package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TalkScheduleGroupList;
import kr.co.eicn.ippbx.server.model.entity.eicn.TalkScheduleGroupEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkScheduleGroupListDetailResponse extends TalkScheduleGroupList {
}
