package kr.co.eicn.ippbx.server.model.entity.customdb;

import kr.co.eicn.ippbx.server.jooq.customdb.tables.pojos.VocResearchResult;
import kr.co.eicn.ippbx.server.model.entity.eicn.VocGroupEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class VocResearchResultEntity extends VocResearchResult {
	private String    idName;
	private VocGroupEntity group; // VOC그룹
	/**
	 * 문항트리의 답변레벨이 3이라면, List[0], List[1], List[2]의 답변정보가 전달된다
	 */
	private List<String> results; // LIST[0] 서울, LIST[1]10
}
