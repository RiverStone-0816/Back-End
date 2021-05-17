package kr.co.eicn.ippbx.server.model.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.eicn.ippbx.server.config.Constants;
import kr.co.eicn.ippbx.server.jooq.eicn.enums.EvaluationFormUseType;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class EvaluationFormSearchRequest {
	private String name;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
	private Date startDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = Constants.DEFAULT_TIMEZONE)
	private Date endDate;
	private Boolean visible;
	private List<EvaluationFormUseType> useTypes = new ArrayList<>();
}
