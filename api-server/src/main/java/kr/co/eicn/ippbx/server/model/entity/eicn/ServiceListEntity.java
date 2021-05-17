package kr.co.eicn.ippbx.server.model.entity.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.Number_070;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ServiceList;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceListEntity extends ServiceList {
	private Number_070 number070;
}
