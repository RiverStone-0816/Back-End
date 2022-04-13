package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.CompanyServer;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServerInfo;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.entity.eicn.ServerInfoEntity;
import kr.co.eicn.ippbx.model.entity.eicn.WebrtcServerInfoEntity;
import kr.co.eicn.ippbx.model.enums.ServerType;
import lombok.Getter;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.SelectConditionStep;
import org.jooq.SelectJoinStep;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CompanyServer.COMPANY_SERVER;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ServerInfo.SERVER_INFO;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.WebrtcServerInfo.WEBRTC_SERVER_INFO;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Getter
@Repository
public class CompanyServerRepository extends EicnBaseRepository<CompanyServer, CompanyServerEntity, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(CompanyServerRepository.class);

	public CompanyServerRepository() {
		super(COMPANY_SERVER, COMPANY_SERVER.SEQ, CompanyServerEntity.class);
	}

	@Override
	protected SelectConditionStep<Record> query(SelectJoinStep<Record> query) {
		return query
				.leftJoin(SERVER_INFO).on(SERVER_INFO.HOST.eq(COMPANY_SERVER.HOST))
				.where();
	}

	@Override
	protected RecordMapper<Record, CompanyServerEntity> getMapper() {
		return record -> {
			final CompanyServerEntity entity = record.into(COMPANY_SERVER).into(CompanyServerEntity.class);
			entity.setServer(record.into(SERVER_INFO).into(ServerInfoEntity.class));
			entity.setWebrtcServerInfo(record.into(WEBRTC_SERVER_INFO).into(WebrtcServerInfoEntity.class));
			return entity;
		};
	}

	public List<CompanyServerEntity> findAllCompanyId(final String companyId) {
		return dsl.select(COMPANY_SERVER.fields())
				.select(SERVER_INFO.fields())
				.from(COMPANY_SERVER)
				.leftJoin(SERVER_INFO).on(SERVER_INFO.HOST.eq(COMPANY_SERVER.HOST))
				.where(DSL.trueCondition())
				.and(COMPANY_SERVER.COMPANY_ID.eq(companyId))
				.fetch(record -> {
					final CompanyServerEntity entity = record.into(COMPANY_SERVER).into(CompanyServerEntity.class);
					entity.setServer(record.into(SERVER_INFO).into(ServerInfoEntity.class));
					return entity;
				});
	}

	public List<CompanyServerEntity> findAllType(final ServerType type) {
		return findAll(COMPANY_SERVER.TYPE.eq(type.getCode()))
				.stream()
				.filter(e -> isNotEmpty(e.getHost()) && e.getServer() != null)
				.sorted(Comparator.comparing(CompanyServerEntity::getHost))
				.collect(Collectors.toList());
	}

	public ServerInfo findCompanyInfo() {
		return dsl.selectDistinct(SERVER_INFO.fields())
				.from(SERVER_INFO)
				.leftJoin(COMPANY_SERVER)
				.on(SERVER_INFO.HOST.eq(COMPANY_SERVER.HOST))
				.where(COMPANY_SERVER.COMPANY_ID.eq(getCompanyId()))
				.and(COMPANY_SERVER.TYPE.eq(ServerType.PBX.getCode()))
				.and(SERVER_INFO.TYPE.like("%" + ServerType.PBX.getCode() + "%"))
				.fetchOneInto(ServerInfo.class);
	}

	public List<CompanyServerEntity> findSoftPhoneInfo() {
		return dsl.select(COMPANY_SERVER.fields())
				.select(SERVER_INFO.fields())
				.select(WEBRTC_SERVER_INFO.fields())
				.from(SERVER_INFO)
				.leftJoin(COMPANY_SERVER)
				.on(SERVER_INFO.HOST.eq(COMPANY_SERVER.HOST))
				.leftJoin(WEBRTC_SERVER_INFO)
				.on(WEBRTC_SERVER_INFO.WEBRTC_HOST.eq(COMPANY_SERVER.HOST))
				.where(COMPANY_SERVER.COMPANY_ID.eq(getCompanyId()))
				.and(SERVER_INFO.TYPE.like("%" + ServerType.WEBRTC_SERVER.getCode() + "%"))
				.fetch(record -> {
					final CompanyServerEntity entity = record.into(COMPANY_SERVER).into(CompanyServerEntity.class);
					entity.setServer(record.into(SERVER_INFO).into(ServerInfoEntity.class));
					entity.setWebrtcServerInfo(record.into(WEBRTC_SERVER_INFO).into(WebrtcServerInfoEntity.class));
					return entity;
				});
	}
}
