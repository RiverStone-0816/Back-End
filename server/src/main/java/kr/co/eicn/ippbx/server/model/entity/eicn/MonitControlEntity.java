package kr.co.eicn.ippbx.server.model.entity.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTree;
import lombok.Data;

import java.util.List;

@Data
public class MonitControlEntity extends CompanyTree {
    private List<PersonEntity> person;
}
