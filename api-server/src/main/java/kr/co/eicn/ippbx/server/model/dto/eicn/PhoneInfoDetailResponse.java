package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.Number_070;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PhoneInfoDetailResponse extends PhoneInfoSummaryResponse {
	private String  outboundGw;
	private String  prevent;
	private String  passwd;

	private Number_070 number070;
}
