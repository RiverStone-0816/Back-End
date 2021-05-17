package kr.co.eicn.ippbx.server.model.dto.eicn;

//import kr.co.eicn.ippbx.server.model.enums.MailProtocolType;
import lombok.Data;

@Data
public class ConfRoomSummaryResponse extends ConfRoomDetailResponse{
    private String hostName;        //소속교환기명
}
