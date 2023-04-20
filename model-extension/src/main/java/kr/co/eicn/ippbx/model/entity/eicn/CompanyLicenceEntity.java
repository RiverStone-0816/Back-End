package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.model.LicenseInfo;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class CompanyLicenceEntity {
	private LicenseInfo recordLicense;  // 레코드 라이센스 정보
	private LicenseInfo pdsLicense;     // PDS 라이센스 정보
	private LicenseInfo statLicence;    // 통계,모니터링
	private LicenseInfo ctiLicence;    // 상담원연결여부 정보
	private LicenseInfo talkLicense;    // 상담톡 라이센스 정보
	private LicenseInfo emailLicense;   // 이메일상담여부 라이센스 정보
	private LicenseInfo chattLicense;	// 채팅 라이센스 정보
	private LicenseInfo sttLicense;		// stt 라이센스 정보
	private LicenseInfo softPhoneLicense; // 소프트폰 라이선스 정보
}
