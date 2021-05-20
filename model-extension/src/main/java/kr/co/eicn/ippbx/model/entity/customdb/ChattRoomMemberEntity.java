package kr.co.eicn.ippbx.model.entity.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattRoomMember;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChattRoomMemberEntity extends CommonChattRoomMember {
    private String userName;
    private String lastReadMessageId;
}
