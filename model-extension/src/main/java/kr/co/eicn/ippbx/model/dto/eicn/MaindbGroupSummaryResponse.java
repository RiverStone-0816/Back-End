package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.enums.Bool;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class MaindbGroupSummaryResponse {
    private Integer seq;   //시퀀스
    private String name;   //그룹명
    private String info;   //추가정보
    private Timestamp makeDate;   //그룹생성일
    private Integer maindbType;   //고객정보유형
    private Integer resultType;   //상담결과유형
    private String maindbName;    //고객정보 유형명
    private String resultName;    //상담결과 유형명
    private Timestamp lastUploadDate;     //마지막업로드날짜
    private Bool isDupUse;      //체크여부(Y: 체크함, N: 체크안함)
    /**
     * @see kr.co.eicn.ippbx.model.enums.DupKeyKind
     */
    private String dupKeyKind;    //체크항목
    private String dupNeedField; //필수항목
    private Bool dupIsUpdate;   //업로드시 처리방법(Y: 업데이트, N: 처리안함)
    private Integer totalCnt = 0;     //데이터수
    private Integer uploadTryCnt = 0; //업로드횟수
    private Bool lastUploadStatus;      //마지막업로드상태(Y: 업로드완료, N:업로드안함)
    private String groupCode;      //지사
    private String groupTreeName;    //지점
    private Integer groupLevel;     //부서
    private List<String> groupTreeNames;   //계층 구조에 따른 array
}
