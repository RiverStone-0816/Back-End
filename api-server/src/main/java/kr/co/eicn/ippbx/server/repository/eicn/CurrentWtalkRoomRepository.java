package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CurrentWtalkRoom;
import kr.co.eicn.ippbx.model.entity.customdb.WtalkRoomEntity;
import kr.co.eicn.ippbx.model.enums.TalkRoomMode;
import kr.co.eicn.ippbx.model.form.TalkAutoEnableFormRequest;
import kr.co.eicn.ippbx.model.form.TalkCurrentListSearchRequest;
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

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CurrentWtalkRoom.CURRENT_WTALK_ROOM;

@Getter
@Repository
public class CurrentWtalkRoomRepository extends EicnBaseRepository<CurrentWtalkRoom, WtalkRoomEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(CurrentWtalkRoomRepository.class);

    CurrentWtalkRoomRepository() {
        super(CURRENT_WTALK_ROOM, CURRENT_WTALK_ROOM.SEQ, WtalkRoomEntity.class);
    }

    @Override
    protected void postProcedure(List<WtalkRoomEntity> entities) {
        entities.forEach(e -> {
            if ("G".equals(e.getRoomStatus())) {
                if (e.getUserid().equals(g.getUser().getId()))
                    e.setTalkRoomMode(TalkRoomMode.MINE);
                else if (StringUtils.isEmpty(e.getUserid()))
                    e.setTalkRoomMode(TalkRoomMode.TOT);
                else if (StringUtils.isNotEmpty(e.getUserid()) && !e.getUserid().equals(g.getUser().getId()))
                    e.setTalkRoomMode(TalkRoomMode.OTHER);
            } else if ("E".equals(e.getRoomStatus()))
                e.setTalkRoomMode(TalkRoomMode.END);
        });
    }

    public Pagination<WtalkRoomEntity> pagination(TalkRoomSearchRequest search) {
        return pagination(search, conditions(search));
    }

    public List<WtalkRoomEntity> findAll(TalkCurrentListSearchRequest search) {
        return findAll(conditions(search));
    }

    private List<Condition> conditions(TalkCurrentListSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();

        if (StringUtils.isNotEmpty(search.getRoomId()))
            conditions.add(CURRENT_WTALK_ROOM.ROOM_ID.eq(search.getRoomId()));

        conditions.add(CURRENT_WTALK_ROOM.SCHEDULE_KIND.eq("G"));

        return conditions;
    }

    private List<Condition> conditions(TalkRoomSearchRequest search) {
        List<Condition> conditions = new ArrayList<>();

        if (Objects.nonNull(search.getStartDate()))
            conditions.add(DSL.date(CURRENT_WTALK_ROOM.ROOM_START_TIME).ge(search.getStartDate()));
        if (Objects.nonNull(search.getEndDate()))
            conditions.add(DSL.date(CURRENT_WTALK_ROOM.ROOM_LAST_TIME).le(search.getEndDate()));
        if (StringUtils.isNotEmpty(search.getId()))
            conditions.add(CURRENT_WTALK_ROOM.USERID.eq(search.getId()));
        if (StringUtils.isNotEmpty(search.getSenderKey()))
            conditions.add(CURRENT_WTALK_ROOM.SENDER_KEY.eq(search.getSenderKey()));
        if (StringUtils.isNotEmpty(search.getRoomName()))
            conditions.add(CURRENT_WTALK_ROOM.ROOM_NAME.like("%" + search.getRoomName() + "%"));
        orderByFields.clear();
        if (search.getSequence().equals("asc"))
            orderByFields.add(CURRENT_WTALK_ROOM.field(search.getSorts().field()).asc());
        else
            orderByFields.add(CURRENT_WTALK_ROOM.field(search.getSorts().field()).desc());

        return conditions;
    }

    public void updateGroupIdCustomIdCustomName(int groupId, String customId, String customName, String roomId) {
        dsl.update(CURRENT_WTALK_ROOM)
                .set(CURRENT_WTALK_ROOM.MAINDB_GROUP_ID, groupId)
                .set(CURRENT_WTALK_ROOM.MAINDB_CUSTOM_ID, customId)
                .set(CURRENT_WTALK_ROOM.MAINDB_CUSTOM_NAME, customName)
                .where(CURRENT_WTALK_ROOM.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .and(CURRENT_WTALK_ROOM.ROOM_ID.eq(roomId))
                .execute();

        dsl.update(CURRENT_WTALK_ROOM)
                .set(CURRENT_WTALK_ROOM.MAINDB_CUSTOM_NAME, customName)
                .where(CURRENT_WTALK_ROOM.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .and(CURRENT_WTALK_ROOM.MAINDB_GROUP_ID.eq(groupId))
                .and(CURRENT_WTALK_ROOM.MAINDB_CUSTOM_ID.eq(customId))
                .and(CURRENT_WTALK_ROOM.ROOM_ID.notEqual(roomId))
                .execute();
    }

    public void updateRoomNameByRoomId(String roomId, String roomName) {
        dsl.update(CURRENT_WTALK_ROOM)
                .set(CURRENT_WTALK_ROOM.ROOM_NAME, roomName)
                .where(CURRENT_WTALK_ROOM.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .and(CURRENT_WTALK_ROOM.ROOM_ID.eq(roomId))
                .execute();
    }

    public void updateAutoEnableByRoomId(String roomId, TalkAutoEnableFormRequest form) {
        dsl.update(CURRENT_WTALK_ROOM)
                .set(CURRENT_WTALK_ROOM.IS_AUTO_ENABLE, form.getIsAutoEnable())
                .where(CURRENT_WTALK_ROOM.COMPANY_ID.eq(g.getUser().getCompanyId()))
                .and(CURRENT_WTALK_ROOM.ROOM_ID.eq(roomId))
                .execute();
    }
}
