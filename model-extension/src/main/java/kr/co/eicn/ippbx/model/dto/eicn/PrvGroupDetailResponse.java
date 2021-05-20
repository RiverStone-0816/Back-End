package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PrvGroupDetailResponse {
    private Integer seq;    //시퀀스
    private String name;    //그룹명
    private Integer prvType;    //업로드유형
    private Integer resultType; //상담결과유형
    private String prvTypeName;     //업로드 유형명
    private String resultTypeName;  //상담결과 유형명
    private Integer totalCnt;       //데이터수
    private String info;    //추가정보

    private Byte dialTimeout;       //다이얼시간
    private String ridKind; //발신번호 설정
    private String ridData;
    private String billingKind;     //과금번호 설정
    private String billingData;
    /**
     * @see kr.co.eicn.ippbx.model.enums.PrvMemberKind
     */
    private String memberKind;      //상담원 설정
    private List<CommonMemberResponse> memberDataList;     //상담원 지정
    private String groupCode;       //그룹코드
    private String groupTreeName;   //그룹이름
    private Integer groupLevel;     //그룹레벨
    private List<String> groupTreeNames;  //계층 구조에 따른 array
    private Map<String, String> fieldInfoType; //유형업로드 종류
}
