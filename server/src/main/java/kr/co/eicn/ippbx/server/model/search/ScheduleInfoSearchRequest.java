package kr.co.eicn.ippbx.server.model.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.eicn.ippbx.server.model.enums.ScheduleType;
import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import kr.co.eicn.ippbx.server.util.page.PageQueryableForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ScheduleInfoSearchRequest extends PageQueryableForm {
	@PageQueryable
	private String number;      // 070넘버
	@PageQueryable
	private Integer groupId;    // 유형
	/**
	 * yyyy-MM-dd or DayOfWeek
	 *
	 * @see kr.co.eicn.ippbx.server.model.enums.DayOfWeek
	 */
	@PageQueryable
	private String searchDate;  // 요일, 날짜
	@PageQueryable
	private String groupCode;   // 조직코드
	@JsonIgnore
	@PageQueryable
	private ScheduleType type;        // 스케쥴 구분 W:주간, H:일별
}
