package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EmailMemberList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmailMemberListEntity extends EmailMemberList {
	private PersonList person;
}
