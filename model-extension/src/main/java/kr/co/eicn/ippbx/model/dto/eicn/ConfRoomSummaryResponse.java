package kr.co.eicn.ippbx.model.dto.eicn;

//import kr.co.eicn.ippbx.server.model.enums.MailProtocolType;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ConfRoomSummaryResponse extends ConfRoomDetailResponse {
    private String hostName;        //소속교환기명
}
