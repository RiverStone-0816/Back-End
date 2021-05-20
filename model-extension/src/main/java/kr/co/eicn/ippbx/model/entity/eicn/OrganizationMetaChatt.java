package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrganizationMetaChatt extends CompanyTree {
    private List<OrganizationMetaChatt> organizationMetaChatt;
    private List<PersonDetailResponse> personList;
}
