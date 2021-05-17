package kr.co.eicn.ippbx.server.model.dto.statdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.IvrTree;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StatCategoryIvrPathResponse extends IvrTree {
    private StatInboundResponse record;
}
