package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class IvrDetailResponse extends IvrTree {
    private List<IvrTree> buttonMappingList = Collections.emptyList(); // 버튼과 매핑되어지는 tree 정보
}
