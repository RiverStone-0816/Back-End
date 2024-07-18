package kr.co.eicn.ippbx.model.entity.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonTranscribeGroup;
import kr.co.eicn.ippbx.model.enums.TranscribeGroupResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TranscribeGroupEntity extends CommonTranscribeGroup {
    private TranscribeGroupResultCode transcribeGroupResultCode;
}
