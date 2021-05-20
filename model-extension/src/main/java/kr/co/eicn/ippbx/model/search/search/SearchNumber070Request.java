package kr.co.eicn.ippbx.model.search.search;

import kr.co.eicn.ippbx.util.page.PageQueryable;
import kr.co.eicn.ippbx.util.page.PageQueryableForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SearchNumber070Request extends PageQueryableForm {
	@PageQueryable
	private Byte type;   //
	@PageQueryable
	private Byte status; //
}
