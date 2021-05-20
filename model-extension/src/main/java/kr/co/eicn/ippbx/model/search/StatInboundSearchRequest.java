package kr.co.eicn.ippbx.model.search;

import kr.co.eicn.ippbx.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class StatInboundSearchRequest extends AbstractStatSearchRequest {
    @PageQueryable
    private List<String> serviceNumbers = new ArrayList<>();
    @PageQueryable
    private List<String> queueNumbers = new ArrayList<>();
}
