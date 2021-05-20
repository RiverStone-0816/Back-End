package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.DaemonInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServerInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ServerInfoEntity extends ServerInfo {
	private List<DaemonInfo> daemonInfos = new ArrayList<>();
}
