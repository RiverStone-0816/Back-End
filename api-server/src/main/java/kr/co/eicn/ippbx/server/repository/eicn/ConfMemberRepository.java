package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.ConfMember;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ConfMember.CONF_MEMBER;

@Getter
@Repository
public class ConfMemberRepository extends EicnBaseRepository<ConfMember, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ConfMember, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(ConfMemberRepository.class);

	public ConfMemberRepository() {
		super(CONF_MEMBER, CONF_MEMBER.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ConfMember.class);
	}
}
