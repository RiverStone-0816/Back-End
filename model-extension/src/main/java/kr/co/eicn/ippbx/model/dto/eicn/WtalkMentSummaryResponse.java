package kr.co.eicn.ippbx.model.dto.eicn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkMent;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WtalkMentSummaryResponse extends WtalkMent {

	@JsonIgnore
	@Override
	public String getCompanyId() {
		return super.getCompanyId();
	}
}
