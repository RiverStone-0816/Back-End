package kr.co.eicn.ippbx.model.dto.statdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StatCategoryIvrPathResponse extends IvrTree {
    private StatInboundResponse record;
}
