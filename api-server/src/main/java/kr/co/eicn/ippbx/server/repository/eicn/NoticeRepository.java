package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.BoardNoticeInfo;
import kr.co.eicn.ippbx.model.enums.BoardType;
import kr.co.eicn.ippbx.model.enums.IdType;
import kr.co.eicn.ippbx.model.form.NoticeFormRequest;
import kr.co.eicn.ippbx.model.search.BoardSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.BoardNoticeInfo.BOARD_NOTICE_INFO;

@Getter
@Repository
public class NoticeRepository extends EicnBaseRepository<BoardNoticeInfo,kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.BoardNoticeInfo, Long> {
    protected final Logger logger = LoggerFactory.getLogger(NoticeRepository.class);

    private final NoticeXFileRepository NoticeXFileRepository;

    public NoticeRepository(kr.co.eicn.ippbx.server.repository.eicn.NoticeXFileRepository noticeXFileRepository) {
        super(BOARD_NOTICE_INFO,BOARD_NOTICE_INFO.ID,kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.BoardNoticeInfo.class);
        NoticeXFileRepository = noticeXFileRepository;
    }

    public Long nextId() {
        final BoardNoticeInfo Id = BOARD_NOTICE_INFO.as("ID");
        return dsl.select(DSL.ifnull(DSL.max(Id.ID), 0).add(1)).from(Id).fetchOneInto(Long.class);
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.BoardNoticeInfo> pagination(BoardSearchRequest search) {
        return pagination(search, conditions(search), Arrays.asList(BOARD_NOTICE_INFO.NOTICE_TYPE.desc(), BOARD_NOTICE_INFO.CREATED_AT.desc()));
    }

    public void updateViewCnt(Long id) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.BoardNoticeInfo record = findOne(id);
        dsl.update(BOARD_NOTICE_INFO)
                .set(BOARD_NOTICE_INFO.VIEW_CNT, record.getViewCnt()+1)
                .where(compareCompanyId())
                .and(BOARD_NOTICE_INFO.ID.eq(id))
                .execute();
    }

    public kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.BoardNoticeInfo findOneCheckBoardType(Long id, Boolean detailYn) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.BoardNoticeInfo boardNoticeInfo = findOneIfNullThrow(id);

        if (!BoardType.NOTICE.getCode().equals(boardNoticeInfo.getBoardType()))
            throw new IllegalArgumentException("공지사항의 게시글이 아닙니다.");
        if (detailYn)
            updateViewCnt(id);

        return boardNoticeInfo;
    }

    public void deleteCheckBoardType(Long id) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.BoardNoticeInfo boardNoticeInfo = findOneIfNullThrow(id);

        if (!BoardType.NOTICE.getCode().equals(boardNoticeInfo.getBoardType()))
            throw new IllegalArgumentException("공지사항의 게시글이 아닙니다.");
        if (!g.getUser().getIdType().equals(IdType.MASTER.getCode()) && !g.getUser().getIdType().equals(IdType.SUPER_ADMIN.getCode())
                && !boardNoticeInfo.getCreatorId().equals(g.getUser().getId()))
            throw new IllegalArgumentException("해당 게시글을 삭제할 수 없습니다.");

        deleteOnIfNullThrow(id);
    }

    public void insertOnGeneratedKey(NoticeFormRequest form) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.BoardNoticeInfo boardNoticeInfoRecord = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.BoardNoticeInfo();
        boardNoticeInfoRecord.setId(nextId());
        boardNoticeInfoRecord.setTitle(form.getTitle());
        boardNoticeInfoRecord.setContent(form.getContent());
        boardNoticeInfoRecord.setCreatorId(g.getUser().getId().equals("") ? "master" : g.getUser().getId());
        boardNoticeInfoRecord.setBoardType(BoardType.NOTICE.getCode());
        boardNoticeInfoRecord.setNoticeType(form.getNoticeType());
        boardNoticeInfoRecord.setCompanyId(getCompanyId());
        super.insertOnGeneratedKey(boardNoticeInfoRecord);
    }

    private List<Condition> conditions(BoardSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();
        conditions.add(BOARD_NOTICE_INFO.BOARD_TYPE.eq(BoardType.NOTICE.getCode()));

        if (search.getStartDate() != null)
            conditions.add(BOARD_NOTICE_INFO.CREATED_AT.ge(DSL.timestamp(search.getStartDate() + " 00:00:00")));

        if(search.getEndDate() != null)
            conditions.add(BOARD_NOTICE_INFO.CREATED_AT.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));

        if(StringUtils.isNotEmpty(search.getSearchText()))
            conditions.add(BOARD_NOTICE_INFO.TITLE.like("%" + search.getSearchText() + "%"));

        if (g.getUser().getIdType().contains(IdType.USER.getCode()))
            conditions.add(BOARD_NOTICE_INFO.NOTICE_TYPE.eq("Y"));

        return conditions;
    }
}
