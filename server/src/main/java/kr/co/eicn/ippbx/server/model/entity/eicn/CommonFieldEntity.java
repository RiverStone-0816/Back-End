package kr.co.eicn.ippbx.server.model.entity.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CommonFieldEntity extends CommonField {
	private List<CommonCodeEntity> codes;
}
