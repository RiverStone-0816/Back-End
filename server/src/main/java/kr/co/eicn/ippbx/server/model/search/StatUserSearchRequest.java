package kr.co.eicn.ippbx.server.model.search;

import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import kr.co.eicn.ippbx.server.util.page.PageQueryableForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatUserSearchRequest extends AbstractStatSearchRequest {
    @PageQueryable
    private List<String> serviceNumbers = new ArrayList<>();
    @PageQueryable
    private String groupCode;
    @PageQueryable
    private List<String> personIds = new ArrayList<>();
}
