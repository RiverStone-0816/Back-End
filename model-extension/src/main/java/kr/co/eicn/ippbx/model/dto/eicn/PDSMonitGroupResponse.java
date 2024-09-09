package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.enums.PDSGroupConnectKind;
import lombok.Data;

@Data
public class PDSMonitGroupResponse {
    private String lastExecuteId;   //PDS 실행 id
    private String runHost;         //실행할교환기
    /**
     * @see PDSGroupConnectKind ;
     */
    private String connectKind;     //연결대상 구분
    private String connectDataValue; // 연결대상 값
}
