package kr.co.eicn.ippbx.server.model.entity.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.server.model.OrganizationPerson;
import lombok.Data;

@Data
public class OrganizationMemberEntity extends CompanyTree {
	private OrganizationPerson person;
}
