package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class CustomInfoResponse {
    private List<CustomInfoCodeResponse> codeList;

    private String number;
    private String service;
}
