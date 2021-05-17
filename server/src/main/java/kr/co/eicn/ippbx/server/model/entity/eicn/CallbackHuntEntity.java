package kr.co.eicn.ippbx.server.model.entity.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.QueueName;
import lombok.Data;

import java.util.List;

@Data
public class CallbackHuntEntity extends QueueName {
    private List<PersonList> idNames;
}
