package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonWtalkRoom;
import kr.co.eicn.ippbx.model.entity.customdb.WtalkRoomEntity;
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
public class TalkRoomRepository extends CustomDBBaseRepository<CommonWtalkRoom, WtalkRoomEntity, Integer> {
    protected Logger logger = LoggerFactory.getLogger(TalkRoomRepository.class);

    private CommonWtalkRoom TABLE;

    public TalkRoomRepository(String companyId) {
        super(new CommonWtalkRoom(companyId), new CommonWtalkRoom(companyId).SEQ, WtalkRoomEntity.class);
        this.TABLE = new CommonWtalkRoom(companyId);
    }

    public Pagination<WtalkRoomEntity> pagination(TalkRoomSearchRequest search) {
        return pagination(search, conditions(search));
    }

    public List<Condition> conditions(TalkRoomSearchRequest search) {
        List<Condition> conditions = new ArrayList<>();

        if (Objects.nonNull(search.getStartDate()))
            conditions.add(TABLE.ROOM_START_TIME.ge(DSL.timestamp(search.getStartDate() + " 00:00:00"))
                    .or(TABLE.ROOM_LAST_TIME.le(DSL.timestamp(search.getStartDate() + " 23:59:59"))));

        if (Objects.nonNull(search.getEndDate()))
            conditions.add(TABLE.ROOM_START_TIME.ge(DSL.timestamp(search.getEndDate() + " 00:00:00"))
                    .or(TABLE.ROOM_LAST_TIME.le(DSL.timestamp(search.getEndDate() + " 23:59:59"))));

        if (StringUtils.isNotEmpty(search.getId()))
            conditions.add(TABLE.USERID.eq(search.getId()));
        if (StringUtils.isNotEmpty(search.getRoomName()))
            conditions.add(TABLE.ROOM_NAME.like("%" + search.getRoomName() + "%"));

        orderByFields.clear();
        if (search.getSequence().equals("asc"))
            orderByFields.add(TABLE.field(name(search.getSorts())).asc());
        else if (search.getSequence().equals("desc"))
            orderByFields.add(TABLE.field(name(search.getSorts())).desc());

        return conditions;
    }
}
