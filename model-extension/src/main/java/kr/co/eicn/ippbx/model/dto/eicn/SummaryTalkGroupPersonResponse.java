package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SummaryTalkGroupPersonResponse extends SummaryPersonResponse {
	private String id;        // 상담원아이디

	private OrganizationSummaryResponse organization; // 조직정보
}
