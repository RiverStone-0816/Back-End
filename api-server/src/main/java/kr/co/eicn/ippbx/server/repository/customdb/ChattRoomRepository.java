package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonChattMsg;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonChattRoom;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonChattRoomMember;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.entity.customdb.ChattRoomEntity;
import kr.co.eicn.ippbx.model.entity.customdb.ChattRoomMemberEntity;
import kr.co.eicn.ippbx.model.enums.ChattingJoinStatus;
import kr.co.eicn.ippbx.model.enums.ChattingRoomNameYn;
import kr.co.eicn.ippbx.model.form.ChattingMemberFormRequest;
import kr.co.eicn.ippbx.model.search.ChattingRoomSearchRequest;
import kr.co.eicn.ippbx.model.search.ChattingSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.service.ChattMemberService;
import kr.co.eicn.ippbx.server.service.ChattMsgService;
import kr.co.eicn.ippbx.server.service.ChattRoomService;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Record;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.jooq.impl.DSL.md5;
import static org.jooq.impl.DSL.noCondition;

@Getter
public class ChattRoomRepository extends CustomDBBaseRepository<CommonChattRoom, ChattRoomEntity, String> {
    protected final Logger logger = LoggerFactory.getLogger(ChattRoomRepository.class);

    private final CommonChattRoom TABLE;
    private final CommonChattRoomMember chatMemberTable;
    private final CommonChattMsg chatMsgTable;
    @Autowired
    private ChattRoomService chattRoomService;
    @Autowired
    private ChattMemberService chattMemberService;
    @Autowired
    private ChattMsgService chattMsgService;
    @Autowired
    private PersonListRepository personListRepository;

    public ChattRoomRepository(String companyId) {
        super(new CommonChattRoom(companyId), new CommonChattRoom(companyId).ROOM_ID, ChattRoomEntity.class);
        this.TABLE = new CommonChattRoom(companyId);
        chatMemberTable = new CommonChattRoomMember(companyId);
        chatMsgTable = new CommonChattMsg(companyId);

        addField(TABLE);
        addField(chatMemberTable);
        addOrderingField(TABLE.LAST_TIME.desc());
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        return query
                .join(chatMemberTable).on(chatMemberTable.ROOM_ID.eq(TABLE.ROOM_ID))
                .where(DSL.noCondition());
    }

    @Override
    protected RecordMapper<Record, ChattRoomEntity> getMapper() {
        return record -> record.into(TABLE).into(ChattRoomEntity.class);
    }

    protected ChattRoomEntity postProcedure(ChattRoomEntity entity, ChattingSearchRequest search) {
        final Map<String, String> personMap = personListRepository.getIdAndNameMap();
        if (entity != null) {
            final Map<String, List<ChattRoomMemberEntity>> chattingMembers = chattMemberService.findAllByRoomId(entity.getRoomId()).stream()
                    .map(e -> {
                        final ChattRoomMemberEntity memberEntity = modelMapper.map(e, ChattRoomMemberEntity.class);
                        memberEntity.setUserName(Objects.nonNull(personMap.get(e.getUserid())) ? personMap.get(e.getUserid()) : personListRepository.findOneById(e.getUserid()).getIdName());

                        return memberEntity;
                    })
                    .collect(Collectors.groupingBy(ChattRoomMemberEntity::getRoomId));
            entity.setChattingMembers(chattingMembers.get(entity.getRoomId()));

            chattMsgService.convertRoomMessage(entity, search, entity.getChattingMembers().stream().collect(Collectors.toMap(ChattRoomMemberEntity::getUserid, e -> e)));
        }
        return entity;
    }

    public String insertChattingRoom(ChattingMemberFormRequest form) {
        final String chattingRoomId = g.getUser().getId() + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        final List<String> memberList = form.getMemberList().stream().distinct().collect(Collectors.toList());
        if (memberList.size() == 1)
            form.getMemberList().remove(g.getUser().getId());
        final String chattingRoomName = chattRoomService.getNewChattingRoomName("", form.getMemberList());
        final String chattingMemberMD5 = chattRoomService.newChattingMemberMD5("", form.getMemberList());

        dsl.insertInto(TABLE)
                .set(TABLE.ROOM_ID, chattingRoomId)
                .set(TABLE.MAKE_USERID, g.getUser().getId())
                .set(TABLE.ORG_MEMBER_CNT, memberList.size())
                .set(TABLE.CUR_MEMBER_CNT, memberList.size())
                .set(TABLE.ROOM_NAME, chattingRoomName)
                .set(TABLE.ROOM_NAME_CHANGE, ChattingRoomNameYn.CHANGE_N.getCode())
                .set(TABLE.MEMBER_MD5, md5(chattingMemberMD5))
                .execute();

        for (String member : memberList) {
            dsl.insertInto(chatMemberTable)
                    .set(chatMemberTable.ROOM_ID, chattingRoomId)
                    .set(chatMemberTable.ROOM_NAME, chattingRoomName)
                    .set(chatMemberTable.MEMBER_MD5, md5(chattingMemberMD5))
                    .set(chatMemberTable.INVITE_TIME, new Timestamp(System.currentTimeMillis()))
                    .set(chatMemberTable.IS_JOIN, ChattingJoinStatus.PREPARE_ACTIVE.getCode())
                    .set(chatMemberTable.USERID, member)
                    .execute();
        }
        return chattingRoomId;
    }

    public void updateChattingRoomMemberCount(String roomId, String newChattingRoomName, Integer memberSize) {
        final kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattRoom chattingRoom = findOneByRoomId(roomId);
        dsl.update(TABLE)
                .set(TABLE.ROOM_NAME, chattingRoom.getRoomNameChange().contains(ChattingRoomNameYn.CHANGE_N.getCode()) ? newChattingRoomName : chattingRoom.getRoomName())
                .set(TABLE.CUR_MEMBER_CNT, chattingRoom.getCurMemberCnt() + memberSize)
                .where(TABLE.ROOM_ID.eq(roomId))
                .execute();
    }

    public void updateChattingRoomName(String roomId, String newRoomName) {
        dsl.update(TABLE)
                .set(TABLE.ROOM_NAME, newRoomName)
                .set(TABLE.ROOM_NAME_CHANGE, ChattingRoomNameYn.CHANGE.getCode())
                .where(TABLE.ROOM_ID.eq(roomId))
                .execute();

        dsl.update(chatMemberTable)
                .set(chatMemberTable.ROOM_NAME, newRoomName)
                .where(chatMemberTable.ROOM_ID.eq(roomId))
                .execute();
    }

    public void deleteChattingRoom(String roomId) {
        final kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattRoom chattingRoom = findOneByRoomId(roomId);

        if (chattingRoom.getCurMemberCnt() > 1)
            dsl.deleteFrom(chatMemberTable)
                    .where(chatMemberTable.ROOM_ID.eq(roomId))
                    .and(chatMemberTable.USERID.eq(g.getUser().getId()))
                    .execute();

        final String newChattingRoomName = chattRoomService.getNewChattingRoomName("", chattMemberService.findMemberByRoomId(roomId));

        dsl.update(TABLE)
                .set(TABLE.ROOM_NAME, chattingRoom.getRoomNameChange().contains(ChattingRoomNameYn.CHANGE_N.getCode()) ? newChattingRoomName : chattingRoom.getRoomName())
                .set(TABLE.CUR_MEMBER_CNT, chattingRoom.getCurMemberCnt() > 1 ? chattingRoom.getCurMemberCnt() - 1 : chattingRoom.getCurMemberCnt())
                .where(TABLE.ROOM_ID.eq(roomId))
                .execute();

        if (Objects.equals(1, chattingRoom.getCurMemberCnt()))
            chattMemberService.getRepository().updateChattingMemberIsJoin(chattingRoom, newChattingRoomName);
    }

    public String getChattingRoomByMemberMD5(String memberMD5) {
        return dsl.select(TABLE.ROOM_ID)
                .from(TABLE)
                .where(TABLE.MEMBER_MD5.eq(md5(memberMD5)))
                .fetchOneInto(String.class);
    }

    public List<kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattRoom> findAllByUserId(ChattingRoomSearchRequest search) {
        Condition roomCondition = noCondition();
        if (StringUtils.isNotEmpty(search.getRoomName()))
            roomCondition = roomCondition.and(TABLE.ROOM_NAME.like("%" + search.getRoomName() + "%"));

        return dsl.select(TABLE.fields())
                .from(TABLE)
                .join(chatMemberTable).on(chatMemberTable.ROOM_ID.eq(TABLE.ROOM_ID))
                .where(roomCondition)
                .and(chatMemberTable.USERID.eq(g.getUser().getId()))
                .and(chatMemberTable.IS_JOIN.ne(ChattingJoinStatus.LEAVE.getCode()))
                .and(chatMemberTable.IS_JOIN.ne(ChattingJoinStatus.PREPARE_ACTIVE.getCode()))
                .fetchInto(kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattRoom.class);
    }

    public ChattRoomEntity findOneByRoomId(String roomId, ChattingSearchRequest search) {
        final ChattRoomEntity entity = dsl.selectFrom(TABLE)
                .where(TABLE.ROOM_ID.eq(roomId))
                .fetchOneInto(ChattRoomEntity.class);

        return postProcedure(entity, search);
    }

    public kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattRoom findOneByRoomId(String roomId) {
        final kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattRoom chatRoom = dsl.selectFrom(TABLE)
                .where(TABLE.ROOM_ID.eq(roomId))
                .fetchOneInto(kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonChattRoom.class);
        if (Objects.isNull(chatRoom))
            throw new IllegalArgumentException("존재하지 않는 채팅방입니다.");
        return chatRoom;
    }
}
