package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class WtalkMemberGroupSummaryResponse {
	private Integer groupId;                    // 상담톡아이디
	private String groupName;                  // 상담톡그룹명
	private String talkStrategy;				// 분배정책
	private long memberCnt;                     // 멤버수
	private List<SummaryWtalkGroupPersonResponse> persons; // 그룹멤버
}
