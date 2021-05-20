package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CmpMemberStatusCode;
import kr.co.eicn.ippbx.model.entity.eicn.CmpMemberStatusCodeEntity;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.CMP_MEMBER_STATUS_CODE;

@Getter
@Repository
public class CmpMemberStatusCodeRepository extends EicnBaseRepository<CmpMemberStatusCode, CmpMemberStatusCodeEntity, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(CmpMemberStatusCodeRepository.class);

    public CmpMemberStatusCodeRepository() {
        super(CMP_MEMBER_STATUS_CODE, CMP_MEMBER_STATUS_CODE.SEQ, CmpMemberStatusCodeEntity.class);

        orderByFields.add(CMP_MEMBER_STATUS_CODE.STATUS_NUMBER.asc());
    }
}
