package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.model.enums.Bool;
import lombok.Data;

@Data
public class NumberSummaryResponse {
	private String number;
	private Byte    type;
	private String svcCid;
	private String useService;
	private Bool isSchedule;
	private String hostName;
	private Bool isTypeChange;
	private String originalNumber;
}
