package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ConGroupSummaryResponse {
    private Integer seq;
    private String name;      //연동DB 그룹명
    private Integer conType;  //연동DB 유형
    private String info;      //추가정보
    private Integer totalCnt;   //데이터수
    private String groupCode;   //그룹코드
    private String groupTreeName;   //그룹이름
    private Integer groupLevel;     //그룹레벨
    private List<String> groupTreeNames;  //계층 구조에 따른 array
    private Map<String, String> fieldInfoType; //유형업로드 종류
}
