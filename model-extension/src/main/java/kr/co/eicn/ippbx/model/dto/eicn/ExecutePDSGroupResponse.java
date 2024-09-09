package kr.co.eicn.ippbx.model.dto.eicn;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ExecutePDSGroupResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp startDate;    //시작일시

    private String pdsTypeName;     //PDS유형 명

    private String  pdsHost;
    private Integer pdsGroupId;

    /**
     * @see kr.co.eicn.ippbx.model.enums.PDSGroupExecuteStatus
     **/
    private String pdsStatus;       //PDS상태
    private String executeId;
    private String executeName;     //실행명
    private String pdsName;         //PDS그룹명
    private String pdsAdminId;      //실행자 id
    private String pdsAdminName;    //실행자명

    /**
     * @see kr.co.eicn.ippbx.model.enums.PDSGroupRidKind;
     **/
    private String ridKind;         // RID(발신번호)설정 구분값
    private String ridData;         // RID(발신번호) 정보

    /**
     * @see kr.co.eicn.ippbx.model.enums.PDSGroupSpeedKind;
     **/
    private String speedKind;       // 속도 기준
    private Number speedData;       // 속도 데이터

    private String dialTimeout;     // 다이얼 시간

    private Integer totalCnt;       //고객수
    private Integer numbersCnt;     //전화번호수
}
