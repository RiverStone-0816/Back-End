package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.dto.customdb.WtalkMsgResponse;
import lombok.Data;

import java.util.List;

@Data
public class WtalkRoomMessageResponse {
    private String roomName;
    private String customName;

    private List<WtalkMsgResponse> wtalkMsgResponse;
}
