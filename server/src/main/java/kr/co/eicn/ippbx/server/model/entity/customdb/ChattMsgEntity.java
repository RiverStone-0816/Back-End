package kr.co.eicn.ippbx.server.model.entity.customdb;

import kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonChattMsg;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChattMsgEntity extends CommonChattMsg {
    private String  userName;
    private Integer unreadMessageCount = 0;  //안읽은 메시지 개수
}
