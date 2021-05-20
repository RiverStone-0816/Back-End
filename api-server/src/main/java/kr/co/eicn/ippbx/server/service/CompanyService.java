package kr.co.eicn.ippbx.server.service;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.LicenseInfo;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyLicenceEntity;
import kr.co.eicn.ippbx.model.enums.IdStatus;
import kr.co.eicn.ippbx.model.enums.RecordType;
import kr.co.eicn.ippbx.server.repository.eicn.CompanyInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PhoneInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PersonList.PERSON_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PhoneInfo.PHONE_INFO;

/**
 * 고객사 관련 공통 서비스
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class CompanyService extends ApiBaseService {

	private final CompanyInfoRepository companyInfoRepository;
	private final PhoneInfoRepository phoneInfoRepository;
	private final PersonListRepository personListRepository;

	/**
	 * 고객사의 라이센스정보를 반환
	 */
	public CompanyLicenceEntity getCompanyLicenceInfo() {
		final CompanyInfo company = companyInfoRepository.findOneIfNullThrow(g.getUser().getCompanyId());
		final CompanyLicenceEntity licence = new CompanyLicenceEntity();
		// 퇴사자는 라이선스에서 제외
		final List<PersonList> personLists = personListRepository.findAll(PERSON_LIST.ID_STATUS.notEqual(IdStatus.RETIRE.getCode()));

		licence.setRecordLicense(
				LicenseInfo.builder()
						.licence(company.getRecordLicense())
						.currentLicence(phoneInfoRepository.fetchCount(PHONE_INFO.RECORD_TYPE.notEqual(RecordType.NONE_RECORDING.getCode())))
						.build()
		);
		licence.setPdsLicense(
				LicenseInfo.builder()
						.licence(company.getPdsLicense())
						.currentLicence(personLists.stream().filter(e -> "Y".equals(e.getIsPds())).mapToInt(e -> 1).sum())
						.build()
		);
		licence.setStatLicence(
				LicenseInfo.builder()
						.licence(company.getStatLicense())
						.currentLicence(personLists.stream().filter(e -> "Y".equals(e.getIsStat())).mapToInt(e -> 1).sum())
						.build()
		);
		licence.setTalkLicense(
				LicenseInfo.builder()
						.licence(company.getTalkLicense())
						.currentLicence(personLists.stream().filter(e -> "Y".equals(e.getIsTalk())).mapToInt(e -> 1).sum())
						.build()
		);
		licence.setEmailLicense(
				LicenseInfo.builder()
						.licence(company.getEmailLicense())
						.currentLicence(personLists.stream().filter(e -> "Y".equals(e.getIsEmail())).mapToInt(e -> 1).sum())
						.build()
		);
		licence.setChattLicense(
				LicenseInfo.builder()
						.licence(company.getChattLicense())
						.currentLicence(personLists.stream().filter(e -> "Y".equals(e.getIsChatt())).mapToInt(e -> 1).sum())
						.build()
		);

		return licence;
	}

	public CompanyInfo getCompanyInfo(String companyId) {
		return companyInfoRepository.findOne(companyId);
	}

	public boolean checkService(String companyId, String service) {
		return companyInfoRepository.findOneIfNullThrow(companyId).getService().contains(service);
	}

	public String getServices(String companyId){
		return companyInfoRepository.findOneIfNullThrow(companyId).getService();
	}
}
