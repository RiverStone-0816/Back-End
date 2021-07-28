package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CurrentTalkRoom;
import kr.co.eicn.ippbx.model.entity.customdb.TalkRoomEntity;
import kr.co.eicn.ippbx.model.search.TalkRoomSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CurrentTalkRoom.CURRENT_TALK_ROOM;

@Getter
@Repository
public class CurrentTalkRoomRepository extends EicnBaseRepository<CurrentTalkRoom, TalkRoomEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(CurrentTalkRoomRepository.class);

    CurrentTalkRoomRepository() {
        super(CURRENT_TALK_ROOM, CURRENT_TALK_ROOM.SEQ, TalkRoomEntity.class);
    }

    public Pagination<TalkRoomEntity> pagination(TalkRoomSearchRequest search) {
        return pagination(search, conditions(search));
    }

    private List<Condition> conditions(TalkRoomSearchRequest search) {
        List<Condition> conditions = new ArrayList<>();

        if (Objects.nonNull(search.getStartDate()))
            conditions.add(DSL.date(CURRENT_TALK_ROOM.ROOM_START_TIME).ge(search.getStartDate()));
        if (Objects.nonNull(search.getEndDate()))
            conditions.add(DSL.date(CURRENT_TALK_ROOM.ROOM_LAST_TIME).le(search.getEndDate()));
        if (StringUtils.isNotEmpty(search.getId()))
            conditions.add(CURRENT_TALK_ROOM.USERID.eq(search.getId()));
        if (StringUtils.isNotEmpty(search.getSenderKey()))
            conditions.add(CURRENT_TALK_ROOM.SENDER_KEY.eq(search.getSenderKey()));
        if (StringUtils.isNotEmpty(search.getRoomName()))
            conditions.add(CURRENT_TALK_ROOM.ROOM_NAME.like("%" + search.getRoomName() + "%"));
        orderByFields.clear();
        if (search.getSequence().equals("asc"))
            orderByFields.add(CURRENT_TALK_ROOM.field(search.getSorts().field()).asc());
        else
            orderByFields.add(CURRENT_TALK_ROOM.field(search.getSorts().field()).desc());

        return conditions;
    }

    public void updateGroupIdCustomIdCustomName(int groupId, String customId, String customName, String roomId) {
        dsl.update(CURRENT_TALK_ROOM)
                .set(CURRENT_TALK_ROOM.MAINDB_GROUP_ID, groupId)
                .set(CURRENT_TALK_ROOM.MAINDB_CUSTOM_ID, customId)
                .set(CURRENT_TALK_ROOM.MAINDB_CUSTOM_NAME, customName)
                .where(CURRENT_TALK_ROOM.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .and(CURRENT_TALK_ROOM.ROOM_ID.eq(roomId))
                .execute();

        dsl.update(CURRENT_TALK_ROOM)
                .set(CURRENT_TALK_ROOM.MAINDB_CUSTOM_NAME, customName)
                .where(CURRENT_TALK_ROOM.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .and(CURRENT_TALK_ROOM.MAINDB_GROUP_ID.eq(groupId))
                .and(CURRENT_TALK_ROOM.MAINDB_CUSTOM_ID.eq(customId))
                .and(CURRENT_TALK_ROOM.ROOM_ID.notEqual(roomId))
                .execute();
    }

    public void updateRoomNameByRoomId(String roomId, String roomName) {
        dsl.update(CURRENT_TALK_ROOM)
                .set(CURRENT_TALK_ROOM.ROOM_NAME, roomName)
                .where(CURRENT_TALK_ROOM.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .and(CURRENT_TALK_ROOM.ROOM_ID.eq(roomId))
                .execute();
    }

}
