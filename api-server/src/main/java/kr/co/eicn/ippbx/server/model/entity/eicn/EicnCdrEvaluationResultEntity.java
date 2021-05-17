package kr.co.eicn.ippbx.server.model.entity.eicn;

import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.EvaluationItemScore;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.EvaluationResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class EicnCdrEvaluationResultEntity extends EvaluationResult {
	private EvaluationFormEntity form; // 평가지정보
	private String targetUserName; // 평가대상자명

	private List<EvaluationItemScore> scores; // 점수 정보...
}
