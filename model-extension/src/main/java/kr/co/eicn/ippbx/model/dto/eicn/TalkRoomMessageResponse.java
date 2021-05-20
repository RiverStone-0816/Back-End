package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.dto.customdb.TalkMsgResponse;
import lombok.Data;

import java.util.List;

@Data
public class TalkRoomMessageResponse {
    private String roomName;
    private String customName;

    private List<TalkMsgResponse> talkMsgResponse;
}
