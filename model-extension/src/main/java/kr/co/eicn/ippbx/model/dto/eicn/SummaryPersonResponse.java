package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class SummaryPersonResponse {
	private String userId;	  // 사용자 ID
	private String idName;    // 한글이름
	private String peer;      // 전화기아이디
	private String extension; // 내선
	private String hostName;  // 호스트명
	private List<OrganizationSummaryResponse> companyTrees; // 조직 정보
}
