package kr.co.eicn.ippbx.model.entity.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonTranscribeLearn;
import kr.co.eicn.ippbx.model.enums.LearnGroupResultCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TranscribeLearnEntity extends CommonTranscribeLearn {
    private LearnGroupResultCode learnGroupResultCode;
}
