package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.TalkMent;
import kr.co.eicn.ippbx.server.model.form.TalkMentFormRequest;
import lombok.Getter;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.TalkMent.TALK_MENT;

@Getter
@Repository
public class TalkMentRepository extends EicnBaseRepository<TalkMent, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TalkMent, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(TalkMentRepository.class);

	TalkMentRepository() {
		super(TALK_MENT, TALK_MENT.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TalkMent.class);
	}

	public Record insertOnGeneratedKey(TalkMentFormRequest form) {
		final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TalkMent record = new kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TalkMent();
		record.setCompanyId(getCompanyId());
		record.setMent(form.getMent());
		record.setMentName(form.getMentName());

		return super.insertOnGeneratedKey(record);
	}
}
