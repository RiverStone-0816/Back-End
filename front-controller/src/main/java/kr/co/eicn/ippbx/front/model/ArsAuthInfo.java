package kr.co.eicn.ippbx.front.model;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ArsAuth;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ArsAuthInfo extends ArsAuth {
    private String pbxHost;

    @Override
    public String toString() {
        return "ArsAuthInfo (" + getSeq() +
                ", " + getInsertDate() +
                ", " + getUserid() +
                ", " + getNumber() +
                ", " + getSessionId() +
                ", " + getAuthNum() +
                ", " + getArsStatus() +
                ", " + getCompanyId() +
                ", " + pbxHost +
                ")";
    }
}
