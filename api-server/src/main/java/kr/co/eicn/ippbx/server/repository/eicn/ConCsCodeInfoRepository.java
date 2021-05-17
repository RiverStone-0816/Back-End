package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.ConCsCodeInfo;
import kr.co.eicn.ippbx.server.model.form.ConCodeFormRequest;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.CON_CS_CODE_INFO;

@Getter
@Repository
public class ConCsCodeInfoRepository extends EicnBaseRepository<ConCsCodeInfo, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConCsCodeInfo, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(ConCsCodeInfoRepository.class);

    public ConCsCodeInfoRepository() {
        super(CON_CS_CODE_INFO, CON_CS_CODE_INFO.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConCsCodeInfo.class);
    }

    public void insertCsCode(ConCodeFormRequest form) {
        dsl.insertInto(CON_CS_CODE_INFO)
                .set(CON_CS_CODE_INFO.TYPE, form.getType())
                .set(CON_CS_CODE_INFO.FIELD_TYPE, "CONCODE")
                .set(CON_CS_CODE_INFO.FIELD_ID, form.getFieldId())
                .set(CON_CS_CODE_INFO.CON_GROUP_ID, form.getConGroupId())
                .set(CON_CS_CODE_INFO.CS_URL, "")
                .set(CON_CS_CODE_INFO.COMPANY_ID, getCompanyId())
                .execute();
    }

    public void updateCsCode(Integer seq, ConCodeFormRequest form) {
        dsl.update(CON_CS_CODE_INFO)
                .set(CON_CS_CODE_INFO.CON_GROUP_ID, form.getConGroupId())
                .where(CON_CS_CODE_INFO.SEQ.eq(seq))
                .execute();
    }

    public kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConCsCodeInfo findOne(Integer type, String fieldId) {
        return findOne(CON_CS_CODE_INFO.TYPE.eq(type).and(CON_CS_CODE_INFO.FIELD_ID.eq(fieldId)));
    }
}
