package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkServiceInfo;
import kr.co.eicn.ippbx.model.form.TalkServiceInfoFormRequest;
import lombok.Getter;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.TalkServiceInfo.TALK_SERVICE_INFO;

@Getter
@Repository
public class TalkServiceInfoRepository extends EicnBaseRepository<TalkServiceInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkServiceInfo, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(TalkServiceInfoRepository.class);

	TalkServiceInfoRepository() {
		super(TALK_SERVICE_INFO, TALK_SERVICE_INFO.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkServiceInfo.class);
	}

	public Record insertOnGeneratedKey(TalkServiceInfoFormRequest form) {
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkServiceInfo record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkServiceInfo();

		record.setCompanyId(getCompanyId());
		record.setSenderKey(form.getSenderKey());
		record.setKakaoServiceId(form.getKakaoServiceId());
		record.setKakaoServiceName(form.getKakaoServiceName());
		record.setIsChattEnable(form.getIsChattEnable());

		return super.insertOnGeneratedKey(record);
	}
}
