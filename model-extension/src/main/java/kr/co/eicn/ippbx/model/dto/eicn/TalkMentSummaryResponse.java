package kr.co.eicn.ippbx.model.dto.eicn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TalkMent;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkMentSummaryResponse extends TalkMent {

	@JsonIgnore
	@Override
	public String getCompanyId() {
		return super.getCompanyId();
	}
}
