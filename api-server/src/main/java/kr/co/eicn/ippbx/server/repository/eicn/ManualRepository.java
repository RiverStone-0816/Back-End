package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.BoardNoticeInfo;
import kr.co.eicn.ippbx.model.enums.BoardType;
import kr.co.eicn.ippbx.model.form.ManualFormRequest;
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
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.BoardNoticeInfo.BOARD_NOTICE_INFO;

@Getter
@Repository
public class ManualRepository extends EicnBaseRepository<BoardNoticeInfo,kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.BoardNoticeInfo, Long>{
    protected final Logger logger = LoggerFactory.getLogger(ManualRepository.class);

    private final ManualXFileRepository manualXFileRepository;

    public ManualRepository(ManualXFileRepository manualXFileRepository) {
        super(BOARD_NOTICE_INFO,BOARD_NOTICE_INFO.ID,kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.BoardNoticeInfo.class);
        this.manualXFileRepository = manualXFileRepository;

        orderByFields.add(BOARD_NOTICE_INFO.CREATED_AT.desc());
    }

    public Long nextId() {
        final BoardNoticeInfo Id = BOARD_NOTICE_INFO.as("ID");
        return dsl.select(DSL.ifnull(DSL.max(Id.ID), 0).add(1)).from(Id).fetchOneInto(Long.class);
    }

    public Pagination<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.BoardNoticeInfo> pagination(BoardSearchRequest search) {
        return super.pagination(search, conditions(search));
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
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.BoardNoticeInfo boardInfo = findOneIfNullThrow(id);

        if (!BoardType.MANUAL.getCode().equals(boardInfo.getBoardType()))
            throw new IllegalArgumentException("매뉴얼의 게시글이 아닙니다.");
        if (detailYn)
            updateViewCnt(id);

        return boardInfo;
    }

    public void deleteCheckBoardType(Long id) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.BoardNoticeInfo boardInfo = findOneIfNullThrow(id);

        if (!BoardType.MANUAL.getCode().equals(boardInfo.getBoardType()))
            throw new IllegalArgumentException("매뉴얼의 게시글이 아닙니다.");
        if (!boardInfo.getCreatorId().equals(g.getUser().getId()))
            throw new IllegalArgumentException("해당 게시글을 삭제할 수 없습니다.");

        delete(id);
    }

    public void insertOnGeneratedKey(ManualFormRequest form) {
        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.BoardNoticeInfo boardInfoRecord = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.BoardNoticeInfo();
        boardInfoRecord.setId(nextId());
        boardInfoRecord.setTitle(form.getTitle());
        boardInfoRecord.setCreatorId(g.getUser().getId().equals("") ? "master" : g.getUser().getId());
        boardInfoRecord.setBoardType(BoardType.MANUAL.getCode());
        boardInfoRecord.setCompanyId(getCompanyId());
        super.insertOnGeneratedKey(boardInfoRecord);
    }

    private List<Condition> conditions(BoardSearchRequest search) {
        final List<Condition> conditions = new ArrayList<>();
        conditions.add(BOARD_NOTICE_INFO.BOARD_TYPE.eq(BoardType.MANUAL.getCode()));

        if (search.getStartDate() != null)
            conditions.add(BOARD_NOTICE_INFO.CREATED_AT.ge(DSL.timestamp(search.getStartDate() + " 00:00:00")));

        if(search.getEndDate() != null)
            conditions.add(BOARD_NOTICE_INFO.CREATED_AT.le(DSL.timestamp(search.getEndDate() + " 23:59:59")));

        if(StringUtils.isNotEmpty(search.getSearchText()))
            conditions.add(BOARD_NOTICE_INFO.TITLE.like("%" + search.getSearchText() + "%"));


        return conditions;
    }
}
