package kr.co.eicn.ippbx.model.entity.eicn;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationItemScore;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationResult;
import kr.co.eicn.ippbx.model.entity.customdb.EicnCdrEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class EvaluationResultEntity extends EvaluationResult {
    private EvaluationFormEntity form; // 평가지정보
    private EicnCdrEntity cdr;  // 녹취정보
    private String targetUserName;

    private List<EvaluationItemScore> scores; // 점수 정보...

    @JsonIgnore
    public Integer getTotalScore() {
        if (scores == null)
            return 0;

        return scores.stream().mapToInt(EvaluationItemScore::getScore).sum();
    }
}
