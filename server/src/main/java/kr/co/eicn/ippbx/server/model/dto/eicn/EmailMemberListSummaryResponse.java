package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmailMemberListSummaryResponse extends SummaryPersonResponse {
    private String id;        // 상담원아이디

    private OrganizationSummaryResponse organization;
}
