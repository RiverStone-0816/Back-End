package kr.co.eicn.ippbx.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LicenseInfo {
	private Integer licence;
	private Integer currentLicence;

	@JsonIgnore
	public boolean isUseLicense() {
		return currentLicence < licence;
	}
}
