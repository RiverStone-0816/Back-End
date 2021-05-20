package kr.co.eicn.ippbx.model.entity.customdb;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonPrvCustomInfo;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonResultCustomInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PrvCustomInfoEntity extends CommonPrvCustomInfo {
    private CommonResultCustomInfo result;
    private String prvSysDamdangName;
}
