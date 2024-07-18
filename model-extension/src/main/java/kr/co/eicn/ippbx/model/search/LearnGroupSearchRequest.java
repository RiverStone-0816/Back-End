package kr.co.eicn.ippbx.model.search;

import kr.co.eicn.ippbx.util.page.PageForm;
import kr.co.eicn.ippbx.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LearnGroupSearchRequest extends PageForm {
    @PageQueryable
    private Integer learnGroup; //그룹Seq
    @PageQueryable
    private String groupName; // 그룹명
    @PageQueryable
    private String learnStatus; //진행상태
}
