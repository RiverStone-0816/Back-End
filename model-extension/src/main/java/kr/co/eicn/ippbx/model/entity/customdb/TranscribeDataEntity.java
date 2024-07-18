package kr.co.eicn.ippbx.model.entity.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonTranscribeData;
import kr.co.eicn.ippbx.model.enums.TranscribeDataResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TranscribeDataEntity extends CommonTranscribeData {
    private TranscribeDataResultCode transcribeDataResultCode;
}
