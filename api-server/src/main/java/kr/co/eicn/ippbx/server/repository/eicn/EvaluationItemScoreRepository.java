package kr.co.eicn.ippbx.server.repository.eicn;

import kr.co.eicn.ippbx.meta.jooq.eicn.tables.EvaluationItemScore;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.EvaluationItemScore.EVALUATION_ITEM_SCORE;

@Getter
@Repository
public class EvaluationItemScoreRepository extends EicnBaseRepository<EvaluationItemScore, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationItemScore, Long> {
	protected final Logger logger = LoggerFactory.getLogger(EvaluationItemScoreRepository.class);

	public EvaluationItemScoreRepository() {
		super(EVALUATION_ITEM_SCORE, EVALUATION_ITEM_SCORE.ID, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationItemScore.class);
	}

	public List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationItemScore> findAllByTargetUserIdAndCdrIdAndEvaluationId(String targetUserId, Integer cdrId, Long evaluationId) {
		return findAll(EVALUATION_ITEM_SCORE.TARGET_USERID.eq(targetUserId).and(EVALUATION_ITEM_SCORE.CDR_ID.eq(cdrId)).and(EVALUATION_ITEM_SCORE.EVALUATION_ID.eq(evaluationId)));
	}
}
