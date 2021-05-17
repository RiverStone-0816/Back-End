package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class TalkMemberGroupSummaryResponse {
	private Integer groupId;                    // 상담톡아이디
	private String  groupName;                  // 상담톡그룹명
	private String  kakaoServiceName;           // 상담톡 서비스명
	private long memberCnt;                     // 멤버수
	private List<SummaryTalkGroupPersonResponse> persons; // 그룹멤버
}
