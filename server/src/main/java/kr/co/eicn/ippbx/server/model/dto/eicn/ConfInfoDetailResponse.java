package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class ConfInfoDetailResponse extends ConfInfoSummaryResponse {
	/**
	 * @see kr.co.eicn.ippbx.server.model.enums.ConfInfoStatusType
	 */
	private String status;	//회의 상태(A:미진행(회의시작전...), B:진행중(회의진행중...), C:진행완료, P:예약날짜만료)
	private Timestamp inputDate;     //입력 날짜
	private Timestamp startDate;     //시작 날짜
	private Timestamp endDate;     //종료 날짜
	private String roomNumber;	//회의실 번호
	private String confType;	//회의 타입
	private String confPasswd;	//회의 비밀번호
	private String reserveAdmin;	//회의실 예약자
	private String reserveAdminName;	//회의실예약자 이름
	private Integer confSound;	//회의참석시음원
	private String confCid;	//초대시Rid번호
	/**
	 * @see kr.co.eicn.ippbx.server.model.enums.ConfInfoIsRecord
	 */
	private String isRecord;	//녹취여부(M:녹취함, S:녹취안함)
	/**
	 * @see kr.co.eicn.ippbx.server.model.enums.ConfInfoIsMachineDetect
	 */
	private String isMachineDetect;	//머신디텍트(N:전화버튼을 눌러서 참여함, Y:머신 디텍트해서 머신은 끊음)
	private Integer totalMemberCnt;	//총 참여자 수
	private Integer attendedMemberCnt;	//
	private String recordDir;	//녹취파일경로
	private String host;	//호스트
	private String companyId;	//회사아이디

	private List<SummaryPersonResponse> inMemberList;
	private List<ConfMemberOutPersonsResponse> outMemberList;
}
