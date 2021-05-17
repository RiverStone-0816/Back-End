package kr.co.eicn.ippbx.server.model.search;

import kr.co.eicn.ippbx.server.util.page.PageForm;
import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PhoneSearchRequest extends PageForm {
    @PageQueryable
    private String extension; //내선
    @PageQueryable
    private String voipTel;   //070번호
    @PageQueryable
    private String localPrefix; //지역번호
    @PageQueryable
    private String cid;         //CID번호
//	private String  billingNumber;
}
