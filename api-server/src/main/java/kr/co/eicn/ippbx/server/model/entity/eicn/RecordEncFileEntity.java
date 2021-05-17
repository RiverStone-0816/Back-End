package kr.co.eicn.ippbx.server.model.entity.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RecordEncFile;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RecordEncKey;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RecordEncFileEntity extends RecordEncFile {
	private RecordEncKey recordEncKey;
}
