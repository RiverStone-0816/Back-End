package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PdsIvr;
import lombok.Data;

import java.util.List;

@Data
public class PDSIvrDetailResponse {
	private Integer seq;
	private Integer code; //
	private String  name; // pds_ivr명
	private String  soundCode; // 음원 참조키

	private List<PdsIvr> buttonMappingList; // 버튼과 매핑되어지는 pds tree 정보
}
