package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkServiceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class WtalkServiceInfoEntity extends WtalkServiceInfo {
	private List<WtalkScheduleInfoEntity> scheduleInfos; // 스케쥴러 목록
}
