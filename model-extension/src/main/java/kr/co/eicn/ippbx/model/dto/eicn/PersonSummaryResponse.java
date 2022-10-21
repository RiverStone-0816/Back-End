package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.enums.IdType;
import lombok.Data;
import lombok.val;

import java.util.Arrays;
import java.util.List;

@Data
public class PersonSummaryResponse {
	private String id;      // 아이디
	private String idType;  // 아이디유형구분
	private String peer;
	private String idName;  // 한글이름
	private String extension; // 내선번호
	private String companyId; // 고객사 아이디
	private String idStatus; //id 상태

	private List<OrganizationSummaryResponse> companyTrees; // 조직 정보
	private String listeningRecordingAuthority; // 녹취권한 듣기
	private String downloadRecordingAuthority; // 녹취권한 다운
	private String removeRecordingAuthority; // 녹취권한 삭제
	private String dataSearchAuthority;

	private String licenseList;	// license 통합
	private String isPds;       // pds 사용여부
	private String isStat;      // 통계,모니터링,상담원연결여부
	private String isTalk;      // 상담톡 여부
	private String isEmail;     // 이메일상담 여부
	private String isChatt;		// 메신저 사용 여부
	private String isLoginChatt; //채팅 로그인 상태

	public boolean admin() {
		val ADMIN_TYPES = Arrays.asList(IdType.MASTER, IdType.SUPER_ADMIN, IdType.ADMIN);
		return ADMIN_TYPES.contains(IdType.of(idType));
	}

	public String getIsPds(){
		return this.licenseList.contains("PDS") ? "Y" : "N";
	}

	public String getIsStat(){
		return this.licenseList.contains("STAT") ? "Y" : "N";
	}

	public String getIsTalk(){
		return this.licenseList.contains("TALK") ? "Y" : "N";
	}

	public String getIsEmail(){
		return this.licenseList.contains("EMAIL") ? "Y" : "N";
	}

	public String getIsChatt(){
		return this.licenseList.contains("CHATT") ? "Y" : "N";
	}
}
