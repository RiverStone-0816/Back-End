package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.VocGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class VOCGroupResponse extends VocGroup {
	private SummaryResearchListResponse research;	 // 설문정보
	private List<String> outboundMemberList = new ArrayList<>();
	private List<String> inboundMemberList = new ArrayList<>();
}
