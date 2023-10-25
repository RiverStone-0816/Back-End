package kr.co.eicn.ippbx.model.search;

import kr.co.eicn.ippbx.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Data
public class StatOutboundSearchRequest extends AbstractStatSearchRequest {
    @PageQueryable
    private List<String> serviceNumbers = new ArrayList<>();
}
