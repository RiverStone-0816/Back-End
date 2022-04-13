package kr.co.eicn.ippbx.model.entity.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyServer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyServerEntity extends CompanyServer {
	private ServerInfoEntity server;
	private WebrtcServerInfoEntity webrtcServerInfo;
}
