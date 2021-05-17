package kr.co.eicn.ippbx.front.model.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PrvGroupUploadForm extends FileForm {
    private String name;        //프리뷰 실행명
    private Integer prvType;    //프리뷰 유형
    private String fieldInfo;   //유형업로드필드
    private String fieldType;   //유형업로드필드 종류
    private Integer resultType; //상담결과 유형
    private Integer prvGroup;   //프리뷰 수행그룹

    private Byte dialTimeout;   //다이얼시간
    private String ridKind;     //발신번호 설정
    private String ridData;     //(내선별, 프리뷰그룹별)
    private String billingKind; //과금번호 설정
    private String billingData; //(내선별, 프리뷰그룹별)
}
