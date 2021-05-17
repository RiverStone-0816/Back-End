package kr.co.eicn.ippbx.server.model.search;

import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatCategorySearchRequest extends AbstractStatSearchRequest {
    @PageQueryable
    private List<String> serviceNumbers = new ArrayList<>();
    @PageQueryable
    private List<String> ivrMulti = new ArrayList<>();
}
