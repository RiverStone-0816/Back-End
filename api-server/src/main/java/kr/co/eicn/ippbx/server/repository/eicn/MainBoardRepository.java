package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.MainBoard;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MainBoardPopup;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.MainBoardTarget;
import kr.co.eicn.ippbx.model.entity.eicn.MainBoardEntity;
import kr.co.eicn.ippbx.model.enums.MainBoardNoticeType;
import kr.co.eicn.ippbx.model.enums.MainBoardPopupPoint;
import kr.co.eicn.ippbx.model.enums.MainBoardPopupTarget;
import kr.co.eicn.ippbx.model.enums.MainBoardTargetCompany;
import kr.co.eicn.ippbx.model.search.MainBoardRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.Getter;
import org.jooq.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;


import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.MainBoard.MAIN_BOARD;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.MainBoardPopup.MAIN_BOARD_POPUP;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.MainBoardTarget.MAIN_BOARD_TARGET;

@Getter
@Repository
public class MainBoardRepository extends EicnBaseRepository<MainBoard, MainBoardEntity, Long>{
    protected final Logger logger = LoggerFactory.getLogger(MainBoardRepository.class);

    public MainBoardRepository() {
        super(MAIN_BOARD, MAIN_BOARD.ID, MainBoardEntity.class);
        addField(MAIN_BOARD);
        addField(MAIN_BOARD_POPUP);
        addField(MAIN_BOARD_TARGET);
        addOrderingField(MAIN_BOARD.ID.desc());
    }

    @Override
    protected RecordMapper<Record, MainBoardEntity> getMapper() {
        return record -> {
            final MainBoardEntity entity = record.into(MAIN_BOARD).into(MainBoardEntity.class);
            entity.setMainBoardPopup(record.into(MAIN_BOARD_POPUP).into(MainBoardPopup.class));
            entity.setMainBoardTarget(record.into(MAIN_BOARD_TARGET).into(MainBoardTarget.class));
            return entity;
        };
    }

    @Override
    protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
        query.groupBy(getSelectingFields());

        return query
                .join(MAIN_BOARD_POPUP).on(MAIN_BOARD_POPUP.MAIN_BOARD_ID.eq(MAIN_BOARD.ID))
                .join(MAIN_BOARD_TARGET).on(MAIN_BOARD_TARGET.MAIN_BOARD_ID.eq(MAIN_BOARD.ID))
                .where();
    }

    public List<MainBoardEntity> findAllLoginBefore(){
        final List<Condition> conditions = new ArrayList<>();

        conditions.add(MAIN_BOARD_POPUP.POPUP_POINT.eq(MainBoardPopupPoint.BEFORE.getCode()));
        conditions.add(MAIN_BOARD_POPUP.START_DATE.le(new Timestamp(System.currentTimeMillis()))
                .and(MAIN_BOARD_POPUP.END_DATE.ge(new Timestamp(System.currentTimeMillis()))));

        return super.findAll(conditions);
    }

    public List<MainBoardEntity> findAllLoginAfter(){
        final List<Condition> conditions = new ArrayList<>();

        conditions.add(MAIN_BOARD_POPUP.POPUP_POINT.eq(MainBoardPopupPoint.AFTER.getCode()));
        conditions.add(MAIN_BOARD_TARGET.COMPANY_ID.eq(getCompanyId())
                .or(MAIN_BOARD_TARGET.COMPANY_ID.eq(MainBoardTargetCompany.TOTAL.getCode())));

        if ("J|A".contains(g.getUser().getIdType())){
            conditions.add(MAIN_BOARD_POPUP.TARGET.eq(MainBoardPopupTarget.ALL.getCode())
                    .or(MAIN_BOARD_POPUP.TARGET.eq(MainBoardPopupTarget.ADMIN.getCode())
                            .or(MAIN_BOARD_POPUP.TARGET.eq(MainBoardPopupTarget.CUSTOMER.getCode()))));
        } else {
            conditions.add(MAIN_BOARD_POPUP.TARGET.eq(MainBoardPopupTarget.ALL.getCode())
                    .or(MAIN_BOARD_POPUP.TARGET.eq(MainBoardPopupTarget.CUSTOMER.getCode())));
        }

        conditions.add(MAIN_BOARD_POPUP.START_DATE.le(new Timestamp(System.currentTimeMillis()))
                .and(MAIN_BOARD_POPUP.END_DATE.ge(new Timestamp(System.currentTimeMillis()))));

        return super.findAll(conditions);
    }

    public Pagination<MainBoardEntity> pagination(MainBoardRequest form){
        final List<Condition> conditions = new ArrayList<>();

        conditions.add(MAIN_BOARD_TARGET.COMPANY_ID.eq(getCompanyId())
                .or(MAIN_BOARD_TARGET.COMPANY_ID.eq(MainBoardTargetCompany.TOTAL.getCode())));

        return super.pagination(form,conditions);
    }

    public List<MainBoardEntity> topFixList(){
        final List<Condition> conditions = new ArrayList<>();

        conditions.add(MAIN_BOARD_TARGET.COMPANY_ID.eq(getCompanyId())
                .or(MAIN_BOARD_TARGET.COMPANY_ID.eq(MainBoardTargetCompany.TOTAL.getCode())));

        conditions.add(MAIN_BOARD.NOTICE_TYPE.eq(MainBoardNoticeType.TOP.getCode()));

        return super.findAll(conditions);
    }
}
