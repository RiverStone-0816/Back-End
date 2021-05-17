package kr.co.eicn.ippbx.server.model.dto.eicn.search;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TalkServiceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchTalkServiceInfoResponse extends TalkServiceInfo {
}
