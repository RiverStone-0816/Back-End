package kr.co.eicn.ippbx.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
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
