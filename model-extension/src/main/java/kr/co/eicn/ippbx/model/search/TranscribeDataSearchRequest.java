package kr.co.eicn.ippbx.model.search;

import kr.co.eicn.ippbx.util.page.PageForm;
import kr.co.eicn.ippbx.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TranscribeDataSearchRequest extends PageForm {
    @PageQueryable
    private Integer transcribeGroup; //그룹Seq
    @PageQueryable
    private String userId; //담당상담원 ID
}
