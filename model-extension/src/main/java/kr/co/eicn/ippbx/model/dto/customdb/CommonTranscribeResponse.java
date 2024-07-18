package kr.co.eicn.ippbx.model.dto.customdb;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.entity.customdb.TranscribeGroupEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonTranscribeResponse extends TranscribeGroupEntity {
    private PersonList personList; // 상담원 정보
    private String grade;

}
