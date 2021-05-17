package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.sql.Date;


@Data
public class ConfInfoSummaryResponse {
	private Integer seq;                  // SEQUENCE KEY
	private String  confName;     // 회의명
	private Date reserveDate;            // 회의예약날짜

	/**
	 * 시간 : (int)reserveFromTime/60, 분 : (int)reserveFromTime%60
	 * 예제 : reserveFromTime이 610이면 10시10분
	 * 회의종료시간도 동일
	 */
	private Integer  reserveFromTime;        // 회의예약시작시간(분)
	private Integer  reservetoTime;            // 회의예약종료시간(분)
}
