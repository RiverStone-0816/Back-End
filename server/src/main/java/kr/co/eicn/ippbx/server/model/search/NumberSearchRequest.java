package kr.co.eicn.ippbx.server.model.search;

import kr.co.eicn.ippbx.server.util.page.PageQueryable;
import kr.co.eicn.ippbx.server.util.page.PageQueryableForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NumberSearchRequest extends PageQueryableForm {
    @NotNull
    @PageQueryable
    private Byte type;
}
