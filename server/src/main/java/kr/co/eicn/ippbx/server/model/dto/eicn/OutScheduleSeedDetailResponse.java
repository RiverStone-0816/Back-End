package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class OutScheduleSeedDetailResponse {
	private Integer parent; // SEQUENCE KEY
	private String name; // 일정명
	private List<SummaryPhoneInfoResponse> extensions; // 추가된번호
	private List<String> weeks; // 요일 목록
	private Integer fromhour; // 시작시간 분 (09:00 -> 540)"
	private Integer tohour; // 종료시간 분 (18:00 -> 1080)"
	private Timestamp fromtime; // 발신 스케쥴러 시작일(일별에서 사용)
	private Timestamp totime;   // 발신 스케쥴러 종료일(일별에서 사용)
	private String soundCode; // 음원 참조 키
	private String soundName; // 음원명
}
