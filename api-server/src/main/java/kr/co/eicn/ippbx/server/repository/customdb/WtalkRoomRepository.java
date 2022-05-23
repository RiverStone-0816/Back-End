package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonWtalkRoom;
import kr.co.eicn.ippbx.model.entity.customdb.TalkRoomEntity;
import kr.co.eicn.ippbx.model.search.TalkRoomSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.jooq.impl.DSL.name;

@Getter
public class WtalkRoomRepository extends CustomDBBaseRepository<CommonWtalkRoom, TalkRoomEntity, Integer> {
    protected Logger logger = LoggerFactory.getLogger(WtalkRoomRepository.class);

    private CommonWtalkRoom TABLE;

    public WtalkRoomRepository(String companyId) {
        super(new CommonWtalkRoom(companyId), new CommonWtalkRoom(companyId).SEQ, TalkRoomEntity.class);
        this.TABLE = new CommonWtalkRoom(companyId);
    }

    public Pagination<TalkRoomEntity> pagination(TalkRoomSearchRequest search) {
        return pagination(search, conditions(search));
    }

    public List<Condition> conditions(TalkRoomSearchRequest search) {
        List<Condition> conditions = new ArrayList<>();

        if (Objects.nonNull(search.getStartDate()))
            conditions.add(DSL.date(TABLE.ROOM_START_TIME).ge(search.getStartDate()).or(DSL.date(TABLE.ROOM_LAST_TIME).le(search.getStartDate())));
        if (Objects.nonNull(search.getEndDate()))
            conditions.add(DSL.date(TABLE.ROOM_START_TIME).ge(search.getEndDate()).or(DSL.date(TABLE.ROOM_LAST_TIME).le(search.getEndDate())));
        if (StringUtils.isNotEmpty(search.getId()))
            conditions.add(TABLE.USERID.eq(search.getId()));
        if (StringUtils.isNotEmpty(search.getRoomName()))
            conditions.add(TABLE.ROOM_NAME.like("%" + search.getRoomName() + "%"));

        if (search.getSequence().equals("asc"))
            orderByFields.add(TABLE.field(name(search.getSort().field())).asc());
        else if (search.getSequence().equals("desc"))
            orderByFields.add(TABLE.field(name(search.getSort().field())).desc());

        return conditions;
    }
}
