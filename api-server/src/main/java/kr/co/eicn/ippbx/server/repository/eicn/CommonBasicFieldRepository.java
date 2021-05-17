package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.CommonBasicField;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.EicnServiceKind;
import lombok.Getter;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.CommonBasicField.COMMON_BASIC_FIELD;

@Getter
@Repository
public class CommonBasicFieldRepository extends EicnBaseRepository<CommonBasicField, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonBasicField, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(CommonBasicFieldRepository.class);

	private final EicnServiceKindRepository eicnServiceKindRepository;

	public CommonBasicFieldRepository(EicnServiceKindRepository eicnServiceKindRepository) {
		super(COMMON_BASIC_FIELD, COMMON_BASIC_FIELD.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonBasicField.class);
		this.eicnServiceKindRepository = eicnServiceKindRepository;
	}

	public List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonBasicField> findAllServiceKindAndInfo(String kind) {
		final Optional<EicnServiceKind> any = eicnServiceKindRepository.findAll().stream().findAny();
		final String serviceKind = any.isPresent() ? any.get().getServiceKind() : "SC";

		return findAll(COMMON_BASIC_FIELD.SERVICE_KIND.eq(serviceKind)
				.and(DSL.and(COMMON_BASIC_FIELD.INFO.eq("ALL").or(COMMON_BASIC_FIELD.INFO.like("%_" + kind + "_%")))));
	}
}
