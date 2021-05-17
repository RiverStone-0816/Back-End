package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PersonDetailResponse extends PersonSummaryResponse {
	private String companyName;
	private String groupCode;   // 조직코드
	private String groupTreeName;
	private String idStatus;    // 근무상태
	private String hpNumber;    // 휴대전화번호
	private String emailInfo;   // 이메일

	private String profilePhoto; //프로필 사진 정보
}
