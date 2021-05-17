package kr.co.eicn.ippbx.server.model.entity.customdb;

import kr.co.eicn.ippbx.server.model.entity.eicn.EicnCdrEvaluationResultEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EicnCdrEvaluationEntity extends EicnCdrEntity {
	private EicnCdrEvaluationResultEntity evaluationResult; // 상담원 평가 결과
}
