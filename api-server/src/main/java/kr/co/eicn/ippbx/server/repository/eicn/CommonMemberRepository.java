package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CommonMember;
import lombok.Getter;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CommonMember.COMMON_MEMBER;

@Getter
@Repository
public class CommonMemberRepository extends EicnBaseRepository<CommonMember, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonMember, Integer>  {
    protected final Logger logger = LoggerFactory.getLogger(CommonMemberRepository.class);

    public CommonMemberRepository() {
        super(COMMON_MEMBER,COMMON_MEMBER.SEQ,kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonMember.class);
    }

    public Integer nextSEQ() {
        final CommonMember member = COMMON_MEMBER.as("SEQ");
        return dsl.select(DSL.ifnull(DSL.max(member.SEQ), 0).add(1)).from(member).fetchOneInto(Integer.class);
    }
}
