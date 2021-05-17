package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.MemberStatusCode;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.*;

@Getter
@Repository
public class MemberStatusCodeRepository extends EicnBaseRepository<MemberStatusCode,kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.MemberStatusCode, Integer>{
    protected final Logger logger = LoggerFactory.getLogger(MemberStatusCodeRepository.class);


    public MemberStatusCodeRepository() {
        super(MEMBER_STATUS_CODE,MEMBER_STATUS_CODE.SEQ,kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.MemberStatusCode.class);
    }
}
