package kr.co.eicn.ippbx.model.form;

import kr.co.eicn.ippbx.util.page.PageQueryable;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import kr.co.eicn.ippbx.util.valid.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TalkCurrentListSearchRequest extends BaseForm {
	@PageQueryable
	private String roomId;
	@PageQueryable
	private String authType;     //  USER 또는 MONIT
	@PageQueryable
	private String orderBy;        // orderby
	@NotNull("mode")
	@PageQueryable
	private String mode;    //상담톡 리스트 모드(MY:상담중, END:종료, TOT:비접수, OTH:타상담)
}
