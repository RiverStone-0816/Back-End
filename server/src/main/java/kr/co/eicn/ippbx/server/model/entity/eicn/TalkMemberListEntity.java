package kr.co.eicn.ippbx.server.model.entity.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.TalkMemberList;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkMemberListEntity extends TalkMemberList {
	private PersonList person;
}
