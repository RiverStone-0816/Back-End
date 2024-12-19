package kr.co.eicn.ippbx.model.search;

import kr.co.eicn.ippbx.util.page.PageForm;
import kr.co.eicn.ippbx.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmailMngSearchRequest extends PageForm {
    @PageQueryable
    private String serviceName;     // 서비스명
    @PageQueryable
    private String mailUserName;    // 수신 접속 계정
}
