package kr.co.eicn.ippbx.model.entity.eicn;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.model.OrganizationPerson;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@ToString
@Data
public class Organization extends CompanyTree {
	private List<OrganizationPerson> persons;
}
