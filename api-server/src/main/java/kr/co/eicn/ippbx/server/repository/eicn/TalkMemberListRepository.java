package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkMemberList;
import kr.co.eicn.ippbx.model.entity.eicn.TalkMemberListEntity;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PersonList.PERSON_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkMemberList.TALK_MEMBER_LIST;

@Getter
@Repository
public class TalkMemberListRepository extends EicnBaseRepository<TalkMemberList, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkMemberList, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(TalkMemberListRepository.class);

	public TalkMemberListRepository() {
		super(TALK_MEMBER_LIST, TALK_MEMBER_LIST.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkMemberList.class);
	}

	public List<TalkMemberListEntity> getTalkMemberListEntities() {
		return dsl.select(TALK_MEMBER_LIST.fields())
					.select(PERSON_LIST.fields())
					.from(TALK_MEMBER_LIST)
					.join(PERSON_LIST)
					.on(TALK_MEMBER_LIST.USERID.eq(PERSON_LIST.ID))
					.where(compareCompanyId())
					.fetch(record -> {
						final TalkMemberListEntity entity = record.into(TALK_MEMBER_LIST).into(TalkMemberListEntity.class);
						entity.setPerson(record.into(PERSON_LIST).into(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList.class));
						return entity;
					});
	}
}
