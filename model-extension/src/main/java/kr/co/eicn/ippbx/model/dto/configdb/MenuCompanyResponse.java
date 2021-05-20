package kr.co.eicn.ippbx.model.dto.configdb;

import kr.co.eicn.ippbx.meta.jooq.configdb.tables.pojos.CommonMenuCompany;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MenuCompanyResponse extends CommonMenuCompany {
}
