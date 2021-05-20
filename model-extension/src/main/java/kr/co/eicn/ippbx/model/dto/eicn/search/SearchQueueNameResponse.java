package kr.co.eicn.ippbx.model.dto.eicn.search;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.co.eicn.ippbx.model.Constants;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchQueueNameResponse extends QueueName {

	@JsonFormat(pattern = Constants.DEFAULT_DATETIME_PATTERN)
	@Override
	public Timestamp getBlendingWaitLasttime() {
		return super.getBlendingWaitLasttime();
	}
}
