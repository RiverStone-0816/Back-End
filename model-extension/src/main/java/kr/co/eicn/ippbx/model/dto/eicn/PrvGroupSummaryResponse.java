package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class PrvGroupSummaryResponse {
    private Integer seq;    //시퀀스
    private String name;    //그룹명
    private Integer prvType;    //업로드유형
    private Integer resultType; //상담결과유형
    private String prvTypeName;     //업로드 유형명
    private String resultTypeName;  //상담결과 유형명
    private Integer totalCnt;       //데이터수

    private String groupCode;       //그룹코드
    private String groupTreeName;   //그룹이름
    private Integer groupLevel;     //그룹레벨
    private List<String> groupTreeNames;  //계층 구조에 따른 array
}
