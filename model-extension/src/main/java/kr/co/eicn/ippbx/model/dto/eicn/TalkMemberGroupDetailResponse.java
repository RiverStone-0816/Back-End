package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class TalkMemberGroupDetailResponse {
	private Integer groupId;                    // 상담톡아이디
	private String  groupName;                  // 상담톡그룹명
	private String  kakaoServiceName;           // 상담톡 서비스명
	private String  senderKey;                  // 관련상담톡서비스
	private List<SummaryTalkGroupPersonResponse> persons;      // 그룹멤버
}
