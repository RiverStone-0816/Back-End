package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.enums.TalkMemberDistributionType;
import lombok.Data;

import java.util.List;

@Data
public class WtalkMemberGroupDetailResponse {
	private Integer groupId;                    // 상담톡아이디
	private String  groupName;                  // 상담톡그룹명
	private String	channelType;				// 채널 eicn,kakao,navertt,naverline
	private TalkMemberDistributionType distributionPolicy;				// 분배전략 NO,RR,LC,LR,CL
	private String  initMent;					// 인사멘트
	private Integer autoWarnMin;				// 자동종료안내시간
	private String  autoWarnMent;				// 자동중료안내맨트
	private Integer autoExpireMin;				// 자동종료시간
	private String  autoExpireMent;				// 자동종료멘트
	private Integer unassignCnt;				// 비접수 건수
	private String  unassignMent;				// 비접수 초과시 멘트
	private Integer memberUnanswerMin;			// 상담원 무응답시간
	private String  memberUnanswerMent;			// 상담원 무응답시 멘트

	private List<SummaryWtalkGroupPersonResponse> persons;      // 그룹멤버
}
