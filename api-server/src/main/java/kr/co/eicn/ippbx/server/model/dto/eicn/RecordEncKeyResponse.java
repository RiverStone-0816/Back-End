package kr.co.eicn.ippbx.server.model.dto.eicn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RecordEncKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecordEncKeyResponse extends RecordEncKey {

	@JsonIgnore
	@Override
	public String getCompanyId() {
		return super.getCompanyId();
	}
}
