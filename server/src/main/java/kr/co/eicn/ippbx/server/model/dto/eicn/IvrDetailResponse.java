package kr.co.eicn.ippbx.server.model.dto.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.IvrTree;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
public class IvrDetailResponse extends IvrTree {
	private List<IvrTree> buttonMappingList = Collections.emptyList(); // 버튼과 매핑되어지는 tree 정보
}
