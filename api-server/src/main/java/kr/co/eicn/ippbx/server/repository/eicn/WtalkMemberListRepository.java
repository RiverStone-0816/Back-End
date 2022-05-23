package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkMemberList;
import kr.co.eicn.ippbx.model.entity.eicn.WtalkMemberListEntity;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PersonList.PERSON_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkMemberList.WTALK_MEMBER_LIST;

@Getter
@Repository
public class WtalkMemberListRepository extends EicnBaseRepository<WtalkMemberList, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkMemberList, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(WtalkMemberListRepository.class);

	public WtalkMemberListRepository() {
		super(WTALK_MEMBER_LIST, WTALK_MEMBER_LIST.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkMemberList.class);
	}

	public List<WtalkMemberListEntity> getTalkMemberListEntities() {
		return dsl.select(WTALK_MEMBER_LIST.fields())
				.select(PERSON_LIST.fields())
				.from(WTALK_MEMBER_LIST)
				.join(PERSON_LIST)
				.on(WTALK_MEMBER_LIST.USERID.eq(PERSON_LIST.ID))
				.where(compareCompanyId())
				.orderBy(WTALK_MEMBER_LIST.DIST_SEQUENCE)
				.fetch(record -> {
					final WtalkMemberListEntity entity = record.into(WTALK_MEMBER_LIST).into(WtalkMemberListEntity.class);
					entity.setPerson(record.into(PERSON_LIST).into(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList.class));
					return entity;
				});
	}
}
