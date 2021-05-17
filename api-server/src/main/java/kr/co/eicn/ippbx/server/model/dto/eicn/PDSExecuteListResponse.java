package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

@Data
public class PDSExecuteListResponse {
    private String lastExecuteId;
    private String lastExecuteName;
}
