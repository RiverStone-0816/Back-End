package kr.co.eicn.ippbx.model.entity.eicn;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class EvaluationResultStatResponse {
	private String targetUserid; // 평가대상 아이디
	private String targetUserName; // 상담원명
	private Long  evaluationId;
	private String evaluationName; // 평가지명
	private Timestamp maxEvaluationDate; // 최종평가일
	private Integer cnt;
	private Integer total;
	private Double avg;
}
