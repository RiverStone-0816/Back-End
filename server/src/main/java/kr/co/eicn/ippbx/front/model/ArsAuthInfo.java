package kr.co.eicn.ippbx.front.model;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ArsAuth;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ArsAuthInfo extends ArsAuth {
    private String pbxHost;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ArsAuthInfo (");

        sb.append(getSeq());
        sb.append(", ").append(getInsertDate());
        sb.append(", ").append(getUserid());
        sb.append(", ").append(getNumber());
        sb.append(", ").append(getSessionId());
        sb.append(", ").append(getAuthNum());
        sb.append(", ").append(getArsStatus());
        sb.append(", ").append(getCompanyId());
        sb.append(", ").append(pbxHost);

        sb.append(")");
        return sb.toString();
    }
}
