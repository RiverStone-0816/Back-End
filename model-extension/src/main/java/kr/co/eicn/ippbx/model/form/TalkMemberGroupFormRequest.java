package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkMemberGroupFormRequest extends BaseForm {
	@NotNull("상담톡그룹명")
	private String groupName;
	@NotNull("관련상담톡서비스")
	private String senderKey;
	@NotNull("추가 사용자")
	private Set<String> personIds; // 추가 사용자

	private String	channelType;				// 채널
	private String	talkStrategy;				// 분배정책
	private String  initMent;					// 인사멘트
	private Integer autoWarnMin;				// 자동종료안내시간
	private String  autoWarnMent;				// 자동중료안내맨트
	private Integer autoExpireMin;				// 자동종료시간
	private String  autoExpireMent;				// 자동종료멘트
	private Integer unassignCnt;				// 비접수 건수
	private String  unassignMent;				// 비접수 초과시 멘트
	private Integer memberUnanswerMin;			// 상담원 무응답시간
	private String  memberUnanswerMent;			// 상담원 무응답시 멘트

}
