package kr.co.eicn.ippbx.server.repository.statdb;

import kr.co.eicn.ippbx.meta.jooq.statdb.tables.CommonStatUserRanking;
import kr.co.eicn.ippbx.model.dto.eicn.ExcellentConsultant;
import kr.co.eicn.ippbx.model.dto.eicn.ExcellentConsultantTopTen;
import kr.co.eicn.ippbx.model.dto.statdb.StatUserRankingResponse;
import kr.co.eicn.ippbx.model.enums.LicenseListType;
import lombok.Getter;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.PERSON_LIST;
import static org.jooq.impl.DSL.sum;

@Getter
public class StatUserRankingRepository extends StatDBBaseRepository<CommonStatUserRanking, kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos.CommonStatUserRanking, Integer> {
    private final Logger logger = LoggerFactory.getLogger(StatUserRankingRepository.class);

    private final CommonStatUserRanking TABLE;

    public StatUserRankingRepository(String companyId) {
        super(new CommonStatUserRanking(companyId), new CommonStatUserRanking(companyId).SEQ, kr.co.eicn.ippbx.meta.jooq.statdb.tables.pojos.CommonStatUserRanking.class);
        TABLE = new CommonStatUserRanking(companyId);
    }

    public StatUserRankingResponse getExcellentConsultantList(ExcellentConsultant.Type field) {
        return dsl.select(PERSON_LIST.ID_NAME,
                sum(TABLE.IN_SUCCESS).as(TABLE.IN_SUCCESS),
                sum(TABLE.OUT_SUCCESS).as(TABLE.OUT_SUCCESS),
                sum(TABLE.IN_BILLSEC_SUM).as(TABLE.IN_BILLSEC_SUM),
                sum(TABLE.OUT_BILLSEC_SUM).as(TABLE.OUT_BILLSEC_SUM),
                sum(TABLE.TOTAL_BILLSEC_SUM).as(TABLE.TOTAL_BILLSEC_SUM),
                sum(TABLE.TOTAL_SUCCESS).as(TABLE.TOTAL_SUCCESS),
                sum(TABLE.CALLBACK_SUCCESS).as(TABLE.CALLBACK_SUCCESS))
                .from(TABLE)
                .innerJoin(PERSON_LIST)
                .on(TABLE.USERID.eq(PERSON_LIST.ID))
                .where(compareCompanyId())
                .and(TABLE.STAT_DATE.eq(DSL.date(DSL.now())))
                .and(TABLE.USERID.notEqual("master"))
                .and(PERSON_LIST.LICENSE_LIST.like("%" + LicenseListType.STAT.getCode() + "%"))
                .groupBy(TABLE.USERID)
                .orderBy(TABLE.field(field.getFieldName()).desc())
                .limit(1)
                .fetchOneInto(StatUserRankingResponse.class);
    }

    public List<StatUserRankingResponse> getTopTenToDay(ExcellentConsultantTopTen.Type field){
        return dsl.select(PERSON_LIST.ID,PERSON_LIST.ID_NAME,
                sum((Field<? extends Number>) TABLE.field(field.getFieldName())).as((Field<? extends Number>) TABLE.field(field.getFieldName())))
                .from(TABLE)
                .innerJoin(PERSON_LIST)
                .on(TABLE.USERID.eq(PERSON_LIST.ID))
                .where(compareCompanyId())
                .and(TABLE.STAT_DATE.eq(DSL.date(DSL.now())))
                .and(TABLE.USERID.notEqual("master"))
                .groupBy(TABLE.USERID)
                .orderBy(TABLE.field(field.getFieldName()).desc())
                .limit(10)
                .fetchInto(StatUserRankingResponse.class);

    }
}
