package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.RouteApplication;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RouteApplicationEntity extends RouteApplication {
    private String appUserName;
    private String rstUserName;
    private Integer cdrSeq;
}
