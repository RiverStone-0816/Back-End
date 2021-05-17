package kr.co.eicn.ippbx.server.model.dto.configdb;

import kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MenuCompanyResponse extends CommonMenuCompany {
}
