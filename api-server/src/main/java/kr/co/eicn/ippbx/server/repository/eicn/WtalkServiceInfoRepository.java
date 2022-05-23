package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkServiceInfo;
import kr.co.eicn.ippbx.model.form.TalkServiceInfoFormRequest;
import lombok.Getter;
import org.jooq.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WtalkServiceInfo.WTALK_SERVICE_INFO;

@Getter
@Repository
public class WtalkServiceInfoRepository extends EicnBaseRepository<WtalkServiceInfo, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkServiceInfo, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(WtalkServiceInfoRepository.class);

	WtalkServiceInfoRepository() {
		super(WTALK_SERVICE_INFO, WTALK_SERVICE_INFO.SEQ, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkServiceInfo.class);
	}

	public Record insertOnGeneratedKey(TalkServiceInfoFormRequest form) {
		final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkServiceInfo record = new kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkServiceInfo();

		record.setCompanyId(getCompanyId());
		record.setSenderKey(form.getSenderKey());
		record.setKakaoServiceId(form.getKakaoServiceId());
		record.setKakaoServiceName(form.getKakaoServiceName());
		record.setIsChattEnable(form.getIsChattEnable());
		record.setBotId(form.getBotId());
		record.setBotName(form.getBotName());

		return super.insertOnGeneratedKey(record);
	}
}
