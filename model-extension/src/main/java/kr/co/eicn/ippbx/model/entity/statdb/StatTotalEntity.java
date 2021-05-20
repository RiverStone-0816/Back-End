package kr.co.eicn.ippbx.model.entity.statdb;

import lombok.Data;

@Data
public class StatTotalEntity {
    private Integer serviceTotalCount;
    private Integer directTotalCount;
    private Integer onlyRead;
    private Integer connReq;
    private Integer serviceSuccessCount;
    private Integer directSuccessCount;
    private Integer serviceCancelCount;
    private Integer directCancelCount;
    private Integer callback;
}
