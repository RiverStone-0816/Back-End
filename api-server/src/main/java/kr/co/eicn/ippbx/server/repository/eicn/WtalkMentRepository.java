package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkMent;
import kr.co.eicn.ippbx.model.form.TalkMentFormRequest;
import lombok.Getter;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkMent.WTALK_MENT;

@Getter
@Repository
public class WtalkMentRepository extends EicnBaseRepository<WtalkMent, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkMent, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(WtalkMentRepository.class);

	WtalkMentRepository() {
		super(WTALK_MENT, WTALK_MENT.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkMent.class);
	}

	public Record insertOnGeneratedKey(TalkMentFormRequest form) {
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkMent record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkMent();
		record.setCompanyId(getCompanyId());
		record.setMent(form.getMent());
		record.setMentName(form.getMentName());

		return super.insertOnGeneratedKey(record);
	}
}
