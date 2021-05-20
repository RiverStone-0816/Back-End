package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonChattRoomMember;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattRoom;
import kr.co.eicn.ippbx.model.enums.ChattingJoinStatus;
import kr.co.eicn.ippbx.model.enums.ChattingRoomNameYn;
import kr.co.eicn.ippbx.model.form.ChattingMemberFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.service.ChattRoomService;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

@Getter
public class ChattMemberRepository extends CustomDBBaseRepository<CommonChattRoomMember, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattRoomMember, String> {
    protected final Logger logger = LoggerFactory.getLogger(ChattMemberRepository.class);

    private final CommonChattRoomMember TABLE;
    @Autowired
    private ChattRoomService chattRoomService;
    @Autowired
    private PersonListRepository personListRepository;

    public ChattMemberRepository(String companyId) {
        super(new CommonChattRoomMember(companyId), new CommonChattRoomMember(companyId).ROOM_ID, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattRoomMember.class);
        this.TABLE = new CommonChattRoomMember(companyId);

        addField(TABLE);
    }

    public String addChattingMembers(String roomId, ChattingMemberFormRequest form) {
        String memberNames = "";
        final CommonChattRoom chattingRoom = chattRoomService.findOneByRoomId(roomId);
        final String newChattingRoomName = chattRoomService.getNewChattingRoomName(chattingRoom.getRoomName(), form.getMemberList());
        if (chattingRoom.getRoomNameChange().contains(ChattingRoomNameYn.CHANGE_N.getCode())) {
            dsl.update(TABLE)
                    .set(TABLE.ROOM_NAME, newChattingRoomName)
                    .where(TABLE.ROOM_ID.eq(roomId))
                    .execute();
        }

        for (String memberId : form.getMemberList()) {
            final String memberName = personListRepository.findOneById(memberId).getIdName();
            if (StringUtils.isEmpty(memberNames))
                memberNames = memberName;
            else
                memberNames = memberNames + ", " + memberName;

            dsl.insertInto(TABLE)
                    .set(TABLE.ROOM_ID, roomId)
                    .set(TABLE.ROOM_NAME, chattingRoom.getRoomNameChange().contains(ChattingRoomNameYn.CHANGE_N.getCode()) ? newChattingRoomName : chattingRoom.getRoomName())
                    .set(TABLE.MEMBER_MD5, chattingRoom.getMemberMd5())
                    .set(TABLE.IS_JOIN, ChattingJoinStatus.INACTIVE.getCode())
                    .set(TABLE.INVITE_TIME, new Timestamp(System.currentTimeMillis()))
                    .set(TABLE.USERID, memberId)
                    .execute();
        }
        chattRoomService.getRepository().updateChattingRoomMemberCount(roomId, newChattingRoomName, form.getMemberList().size());
        return memberNames;
    }

    public void updateChattingMemberIsJoin(CommonChattRoom chattingRoom, String newChattingRoomName) {
        dsl.update(TABLE)
                .set(TABLE.IS_JOIN, ChattingJoinStatus.LEAVE.getCode())
                .set(TABLE.ROOM_NAME, chattingRoom.getRoomNameChange().contains(ChattingRoomNameYn.CHANGE_N.getCode()) ? newChattingRoomName : chattingRoom.getRoomName())
                .where(TABLE.ROOM_ID.eq(chattingRoom.getRoomId()))
                .and(TABLE.USERID.eq(g.getUser().getId()))
                .execute();
    }

    public List<kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattRoomMember> findAllByRoomId(String roomId) {
        return findAll(TABLE.ROOM_ID.eq(roomId), Collections.singletonList(TABLE.USERID.desc()));
    }

    public List<String> findMemberByRoomId(String roomId) {
        return dsl.select(TABLE.USERID)
                .from(TABLE)
                .where(TABLE.ROOM_ID.eq(roomId))
                .and(TABLE.IS_JOIN.ne(ChattingJoinStatus.LEAVE.getCode()))
                .fetchInto(String.class);
    }
}
