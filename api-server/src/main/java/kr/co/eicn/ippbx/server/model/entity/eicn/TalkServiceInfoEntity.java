package kr.co.eicn.ippbx.server.model.entity.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TalkServiceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkServiceInfoEntity extends TalkServiceInfo {
	private List<TalkScheduleInfoEntity> scheduleInfos; // 스케쥴러 목록
}
