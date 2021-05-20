package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SummaryPickUpPersonResponse extends SummaryPersonResponse {
	private String pickup;
	private String pickupName;
}
