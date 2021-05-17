package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.VocMemberList;
import lombok.Getter;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.VocMemberList.VOC_MEMBER_LIST;

@Getter
@Repository
public class VocMemberListRepository extends EicnBaseRepository<VocMemberList, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.VocMemberList, Integer> {
    protected final Logger logger = LoggerFactory.getLogger(VocMemberListRepository.class);

    public VocMemberListRepository() {
        super(VOC_MEMBER_LIST, VOC_MEMBER_LIST.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.VocMemberList.class);
    }

    public Integer nextSequence() {
        final VocMemberList memberList = VOC_MEMBER_LIST.as("ID");
        return dsl.select(DSL.ifnull(DSL.max(memberList.SEQ), 0).add(1)).from(memberList).fetchOneInto(Integer.class);
    }
}
