package kr.co.eicn.ippbx.model.entity.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonMaindbCustomInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatResultEntity extends CommonMaindbCustomInfo {
    private Integer resultType;
    private Timestamp resultDate;
    private String rsCode_1;
}
