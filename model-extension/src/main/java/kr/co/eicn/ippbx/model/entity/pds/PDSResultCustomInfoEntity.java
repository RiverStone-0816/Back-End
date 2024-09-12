package kr.co.eicn.ippbx.model.entity.pds;

import kr.co.eicn.ippbx.meta.jooq.pds.tables.pojos.ResultCustomInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PDSResultCustomInfoEntity extends ResultCustomInfo {
    private PDSCustomInfoEntity customInfo;
    private String userName;
}
