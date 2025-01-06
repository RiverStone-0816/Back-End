package kr.co.eicn.ippbx.model.search.provision;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.eicn.ippbx.util.page.PageForm;
import kr.co.eicn.ippbx.util.page.PageQueryable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProvisionSttSearchRequest extends PageForm {
    @Schema(description = "상담 고유키", example = "")
    @PageQueryable
    private Integer resultSeq;
}
