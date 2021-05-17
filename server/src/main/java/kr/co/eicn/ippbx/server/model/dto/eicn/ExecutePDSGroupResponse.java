package kr.co.eicn.ippbx.server.model.dto.eicn;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class ExecutePDSGroupResponse {
    private String pdsHost;
    private Integer pdsGroupId;
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.PDSGroupExecuteStatus
     **/
    private String pdsStatus;       //PDS상태
    private String executeName;     //실행명
    private String executeId;
    private String pdsName;         //PDS명
    private String pdsAdminId;      //실행자 id
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private Timestamp startDate;    //시작일시
    private String pdsTypeName;     //PDS유형 명
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.PDSGroupRidKind;
     **/
    private String ridKind;         //RID유형
    private String ridData;         //RID값
    /**
     * @see kr.co.eicn.ippbx.server.model.enums.PDSGroupSpeedKind;
     **/
    private String speedKind;       //속도유형
    private Number speedData;       //속도값 speedKind = MEMBER ? speedData / 10
    private String dialTimeout;     //타임아웃
    private Integer totalCnt;       //고객수
    private Integer numbersCnt;     //전화번호수
}
