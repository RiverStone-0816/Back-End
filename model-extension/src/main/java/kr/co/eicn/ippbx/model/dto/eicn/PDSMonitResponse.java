package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.dto.pds.ExecutePDSCustomInfoCountResponse;
import lombok.Data;

import java.util.List;

@Data
public class PDSMonitResponse {
    private PDSMonitGroupResponse pdsGroup;         //PDS그룹정보
    private ExecutePDSGroupResponse executeGroup;   //실행중인 PDS정보
    private ExecutePDSCustomInfoCountResponse count;//통계정보
    private List<PersonListSummary> persons;        //담당자목록
}
