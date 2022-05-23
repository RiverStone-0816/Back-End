package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.WtalkMemberList;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WtalkMemberListEntity extends WtalkMemberList {
	private PersonList person;
}
