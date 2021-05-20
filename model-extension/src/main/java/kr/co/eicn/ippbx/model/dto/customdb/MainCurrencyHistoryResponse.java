package kr.co.eicn.ippbx.model.dto.customdb;

import lombok.Data;

import java.util.List;

@Data
public class MainCurrencyHistoryResponse {
    private List<MainInOutInfoResponse> inOutInfoList;
    private Integer callCnt;
}
