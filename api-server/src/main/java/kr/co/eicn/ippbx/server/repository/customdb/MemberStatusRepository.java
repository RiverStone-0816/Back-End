package kr.co.eicn.ippbx.server.repository.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.CommonMemberStatus;
import kr.co.eicn.ippbx.model.dto.customdb.PersonLastStatusInfoResponse;
import lombok.Getter;
import org.jooq.Record2;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import static org.jooq.impl.DSL.dateSub;
import static org.jooq.impl.DSL.max;

@Getter
public class MemberStatusRepository extends CustomDBBaseRepository<CommonMemberStatus, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMemberStatus, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(MemberStatusRepository.class);

    private final CommonMemberStatus TABLE;

    public MemberStatusRepository(String companyId) {
        super(new CommonMemberStatus(companyId), new CommonMemberStatus(companyId).SEQ, kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMemberStatus.class);
        this.TABLE = new CommonMemberStatus(companyId);
    }

    public List<PersonLastStatusInfoResponse> findAllMemberStatusTime() {
        Table<Record2<Timestamp, String>> TIME_TABLE = dsl.select(max(TABLE.END_DATE).as(TABLE.END_DATE), TABLE.PHONENAME)
                .from(TABLE).where(TABLE.END_DATE.gt(DSL.cast(dateSub(Date.valueOf(LocalDate.now()), 30),Timestamp.class))).groupBy(TABLE.PHONENAME).asTable("B");

        return dsl.select(
                TABLE.END_DATE,
                TABLE.PHONEID,
                TABLE.PHONENAME,
                TABLE.NEXT_STATUS,
                TABLE.IN_OUT
        ).from(TABLE)
                .join(TIME_TABLE)
                .on(TABLE.END_DATE.eq(TIME_TABLE.field(TABLE.END_DATE)).and(TABLE.PHONENAME.eq(TIME_TABLE.field(TABLE.PHONENAME))))
                .where(compareCompanyId())
                .fetchInto(PersonLastStatusInfoResponse.class);
    }
}
