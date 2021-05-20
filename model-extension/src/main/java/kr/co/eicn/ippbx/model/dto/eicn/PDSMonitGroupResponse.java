package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class PDSMonitGroupResponse {
    private String lastExecuteId;   //pds실행 id
    private String runHost;         //실행 호스트
    /**
     * @see kr.co.eicn.ippbx.model.enums.ConnectKind;
     * */
    private String connectKind;     //연결대상
}
