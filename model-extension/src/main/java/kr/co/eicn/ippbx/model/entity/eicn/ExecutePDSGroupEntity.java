package kr.co.eicn.ippbx.model.entity.eicn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ExecutePdsGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ExecutePDSGroupEntity extends ExecutePdsGroup {
    @JsonIgnore
    public Number getSpeedDataBySpeedKind() {
        return getSpeedKind().equals("MEMBER") ? (double) getSpeedData() / 10 : getSpeedData();
    }
}
