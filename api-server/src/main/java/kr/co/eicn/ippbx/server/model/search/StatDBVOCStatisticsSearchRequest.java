package kr.co.eicn.ippbx.server.model.search;

import kr.co.eicn.ippbx.server.util.page.PageForm;
import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatDBVOCStatisticsSearchRequest extends PageForm {
	@PageQueryable
	private Date startDate; // 시작일
	@PageQueryable
	private Date endDate; // 종료일
	@PageQueryable
	private Integer vocGroupSeq; // voc_group.seq
}
