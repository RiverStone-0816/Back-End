package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.ArsAuth;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.server.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.server.model.enums.ServerType;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import lombok.Getter;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static kr.co.eicn.ippbx.server.jooq.eicn.tables.ArsAuth.ARS_AUTH;

@Getter
@Repository
public class ArsAuthRepository extends EicnBaseRepository<ArsAuth, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ArsAuth, Integer> {
	protected final Logger logger = LoggerFactory.getLogger(ArsAuthRepository.class);
	private final CacheService cacheService;
	private final PBXServerInterface pbxServerInterface;

    private final CompanyInfoRepository companyInfoRepository;
    private final CompanyServerRepository companyServerRepository;
    private final PersonListRepository personListRepository;

    public ArsAuthRepository(CacheService cacheService, PBXServerInterface pbxServerInterface, CompanyInfoRepository companyInfoRepository,
							 CompanyServerRepository companyServerRepository, PersonListRepository personListRepository) {
		super(ARS_AUTH, ARS_AUTH.SEQ, kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ArsAuth.class);

		this.cacheService = cacheService;
		this.pbxServerInterface = pbxServerInterface;
        this.companyInfoRepository = companyInfoRepository;
        this.companyServerRepository = companyServerRepository;
		this.personListRepository = personListRepository;

		orderByFields.add(ARS_AUTH.INSERT_DATE.desc());
	}

	public void insert(final String userId, final String number, final String authNum, final String sessionId, final String companyId) {
		insert(dsl, userId, number, authNum, sessionId, companyId);
	}

	public void insert(DSLContext dslContext, final String userId, final String number, final String authNum, final String sessionId, final String companyId) {
		dslContext.insertInto(ARS_AUTH)
				.set(ARS_AUTH.USERID, userId)
				.set(ARS_AUTH.NUMBER, number)
				.set(ARS_AUTH.SESSION_ID, sessionId)
				.set(ARS_AUTH.AUTH_NUM, authNum)
				.set(ARS_AUTH.ARS_STATUS, "A")
				.set(ARS_AUTH.COMPANY_ID, companyId)
				.set(ARS_AUTH.INSERT_DATE, DSL.now())
				.execute();
	}

	public void update(final String userId, final String number, final String authNum, final String sessionId, final String companyId) {
		dsl.update(ARS_AUTH)
				.set(ARS_AUTH.USERID, userId)
				.set(ARS_AUTH.NUMBER, number)
				.set(ARS_AUTH.SESSION_ID, sessionId)
				.set(ARS_AUTH.AUTH_NUM, authNum)
				.set(ARS_AUTH.ARS_STATUS, "A")
				.set(ARS_AUTH.COMPANY_ID, companyId)
				.where(getCompanyId())
				.and(ARS_AUTH.SESSION_ID.eq(sessionId))
				.execute();
	}

	public kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ArsAuth findOneBySessionId(final String sessionId) {
			return findAll(ARS_AUTH.SESSION_ID.eq(sessionId)).stream().findFirst().orElse(null);
    }

    public kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ArsAuth findOneByUserId(final String userId) {

        final Optional<CompanyServerEntity> optionalPbxServer = companyServerRepository.findAllType(ServerType.PBX).stream().findAny();
        if (!optionalPbxServer.isPresent())
            throw new IllegalStateException("PBX ERROR");

        final CompanyServerEntity pbxServer = optionalPbxServer.get();
		Optional<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ArsAuth> arsAuth;

        if (Constants.LOCAL_HOST.equals(pbxServer.getHost())) {
			arsAuth = findAll(ARS_AUTH.USERID.eq(userId)).stream().findFirst();
		} else {
            try (final DSLContext pbxDsl = pbxServerInterface.using(pbxServer.getHost())) {
            	orderByFields.add(ARS_AUTH.INSERT_DATE.desc());
				arsAuth = findAll(pbxDsl, ARS_AUTH.USERID.eq(userId)).stream().findFirst();
			}
		}

        arsAuth.ifPresent(e -> e.setNumber(personListRepository.findOneIfNullThrow(userId).getHpNumber()));

		return arsAuth.orElse(null);
	}
}
