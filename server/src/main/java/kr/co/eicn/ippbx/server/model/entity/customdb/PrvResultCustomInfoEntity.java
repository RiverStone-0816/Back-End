package kr.co.eicn.ippbx.server.model.entity.customdb;

import kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonPrvCustomInfo;
import kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.CommonResultCustomInfo;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PrvResultCustomInfoEntity extends CommonResultCustomInfo {
    private CommonPrvCustomInfo customInfo;
    private String userName;
    private String userOrgName;
    private String userTrName;
    private PersonList personList; // 상담원 정보
}
