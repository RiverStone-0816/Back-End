package kr.co.eicn.ippbx.server.model.entity.customdb;

import kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonPrvCustomInfo;
import kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonResultCustomInfo;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonField;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PrvGroup;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CommonType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PrvCustomInfoEntity extends CommonPrvCustomInfo {
    private CommonResultCustomInfo result;
    private String prvSysDamdangName;
}
