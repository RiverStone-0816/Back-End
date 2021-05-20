package kr.co.eicn.ippbx.model.search;

import kr.co.eicn.ippbx.util.page.PageForm;
import kr.co.eicn.ippbx.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class GradeListSearchRequest extends PageForm {
    @PageQueryable
    private String gradeNumber;
    @PageQueryable
    private Set<String> gradeNumbers = new HashSet<>();
    @PageQueryable
    private String grade;
}
