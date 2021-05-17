package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class SummaryNumber070Response {
	private String    number; // 070번호
	private Byte    type; // 번호타입(0:큐, 1:개인, 2:대표, 3:회의)
	private String    hanName; //
}
