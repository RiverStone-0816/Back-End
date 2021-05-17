package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.exception.DuplicateKeyException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.HolyInfo;
import kr.co.eicn.ippbx.server.model.form.HolyInfoFormRequest;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.HolyInfo.HOLY_INFO;

@Getter
@Repository
public class HolyInfoRepository extends EicnBaseRepository<HolyInfo, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HolyInfo, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(HolyInfoRepository.class);

	public HolyInfoRepository() {
		super(HOLY_INFO, HOLY_INFO.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HolyInfo.class);

		orderByFields.add(HOLY_INFO.LUNAR_YN.asc());
		orderByFields.add(DSL.field("str_to_date({0}, '%m-%d')", String.class, HOLY_INFO.HOLY_DATE).asc());
	}

	public void insert(HolyInfoFormRequest form) {
		final String holyDate = form.getHolyDate();
		final String lunarYn = StringUtils.defaultString(form.getLunarYn(), "N");

		final Optional<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HolyInfo> optionalDuplicatedCheck = findAll(
				DSL.and(HOLY_INFO.HOLY_NAME.eq(form.getHolyName()).or(HOLY_INFO.HOLY_DATE.eq(holyDate).and(HOLY_INFO.LUNAR_YN.eq(lunarYn))))
		).stream().findAny();

		if (optionalDuplicatedCheck.isPresent())
			throw new DuplicateKeyException("중복된 휴일명이나 공휴일이 있습니다.");

		final kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HolyInfo record = new kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HolyInfo();

		record.setHolyName(form.getHolyName());
		record.setHolyDate(holyDate);
		record.setLunarYn(lunarYn);
		record.setCompanyId(getCompanyId());

		super.insert(record);
	}

	public void updateByKey(HolyInfoFormRequest form, Integer seq) {
		final String holyDate = form.getHolyDate();
		final String lunarYn = StringUtils.defaultString(form.getLunarYn(), "N");

		final Optional<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HolyInfo> optionalDuplicatedCheck = findAll(
				HOLY_INFO.SEQ.ne(seq).and(DSL.and(HOLY_INFO.HOLY_NAME.eq(form.getHolyName()).or(HOLY_INFO.HOLY_DATE.eq(holyDate).and(HOLY_INFO.LUNAR_YN.eq(lunarYn)))))
		).stream().findAny();

		if (optionalDuplicatedCheck.isPresent())
			throw new DuplicateKeyException("중복된 휴일명이나 공휴일이 있습니다.");

		super.updateByKey(form, seq);
	}
}
