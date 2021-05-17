package kr.co.eicn.ippbx.server.model.dto.configdb;

import kr.co.eicn.ippbx.server.jooq.configdb.tables.pojos.CommonMenuCompany;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserMenuCompanyResponse extends CommonMenuCompany {
    private List<UserMenuCompanyResponse> children = new ArrayList<>();
    private String groupName;
}
