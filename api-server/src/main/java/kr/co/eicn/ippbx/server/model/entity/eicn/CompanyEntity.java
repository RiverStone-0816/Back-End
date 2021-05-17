package kr.co.eicn.ippbx.server.model.entity.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyEntity extends CompanyInfo {

    public boolean isServiceAvailable(final String service) {
        return isNotEmpty(getService()) && Arrays.asList(getService().split("[|]")).contains(service);
    }
}

