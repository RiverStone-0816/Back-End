package kr.co.eicn.ippbx.model.form.provision;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonProperty;

@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class ProvisionResultCustomInfoFormRequest extends BaseForm {

    @JsonProperty("SRVC_KEYVAL")
    private String SRVC_KEYVAL;

    @JsonProperty("SCR_KEYVAL")
    private String SCR_KEYVAL;

    @JsonProperty("firmNo")
    @Schema(description = "업체번호", example = "2000001329")
    private String FIRM_NO;

    @JsonProperty("firmNm")
    @Schema(description = "회사명", example = "영암군연합사업단")
    private String FIRM_NM;

    @JsonProperty("brno")
    @Schema(description = "사업자번호", example = "1208550247")
    private String BRNO;

    @JsonProperty("jurirno")
    @Schema(description = "법인등록번호", example = "1101114809468")
    private String JURIRNO;

    @JsonProperty("addr")
    @Schema(description = "주소", example = "전라남도 영암군 영암읍 중앙로 43")
    private String ADDR;

    @JsonProperty("rprsTelno")
    @Schema(description = "대표전화번호", example = "01000000000")
    private String RPRS_TELNO;

    @JsonProperty("firmEmailAddr")
    @Schema(description = "업체이메일", example = "nhag5610-1@nonghyup.com")
    private String FIRM_EMAIL_ADDR;

    @JsonProperty("firmTypeCd")
    @Schema(description = "업체타입", example = "001 판매자, 002 구매자, 003 내부관리자, 004 농관원, 005 시장개설자, 006 출하자")
    private String FIRM_TYPE_CD;

    @JsonProperty("firmTypeNm")
    @Schema(description = "업체타입명", example = "판매자")
    private String FIRM_TYPE_NM;

    @JsonProperty("psitnTypeCd")
    @Schema(description = "소속구분코드", example = "104")
    private String PSITN_TYPE_CD;

    @JsonProperty("psitnTypeNm")
    @Schema(description = "소속구분코드명", example = "직접판매자")
    private String PSITN_TYPE_NM;

    @JsonProperty("trmtClsCd")
    @Schema(description = "취급분류코드", example = "001")
    private String TRMT_CLS_CD;

    @JsonProperty("trmtClsNm")
    @Schema(description = "취급분류코드명", example = "청과")
    private String TRMT_CLS_NM;

    @JsonProperty("rgstnStatCd")
    @Schema(description = "등록상태코드", example = "001")
    private String RGSTN_STAT_CD;

    @JsonProperty("rgstnStatNm")
    @Schema(description = "등록상태명", example = "정상")
    private String RGSTN_STAT_NM;

    @JsonProperty("firmStatCd")
    @Schema(description = "업체상태코드", example = "002")
    private String FIRM_STAT_CD;

    @JsonProperty("firmStatNm")
    @Schema(description = "업체상태코드명", example = "승인")
    private String FIRM_STAT_NM;

    @JsonProperty("mmno")
    @Schema(description = "회원번호", example = "2000002226")
    private String MMNO;

    @JsonProperty("mbrNm")
    @Schema(description = "회원명", example = "영암군연합사업단")
    private String MBR_NM;

    @JsonProperty("emailAddr")
    @Schema(description = "회원이메일", example = "nhag5610-1@nonghyup.com")
    private String EMAIL_ADDR;

    @JsonProperty("moblTelno")
    @Schema(description = "휴대폰번호", example = "01000000000")
    private String MOBL_TELNO;

    @JsonProperty("CNSLT_TTL")
    @Schema(description = "상담제목", example = "서비스 요청")
    private String CNSLT_TTL;

    @JsonProperty("CNSLT_TYPE_LCLS_CD")
    @Schema(description = "상담유형대분류코드", example = "C01")
    private String CNSLT_TYPE_LCLS_CD;

    @JsonProperty("CNSLT_TYPE_MCLS_CD")
    @Schema(description = "상담유형중분류코드", example = "C02")
    private String CNSLT_TYPE_MCLS_CD;

    @JsonProperty("CNSLT_ACPT_CHNNL_CD")
    @Schema(description = "상담접수채널코드", example = "001 콜센터, 002 Q&A, 003 출장")
    private String CNSLT_ACPT_CHNNL_CD;

    @JsonProperty("CNSLT_REQUST_CONT")
    @Schema(description = "상담요청내용", example = "서비스 관련 문의")
    private String CNSLT_REQUST_CONT;

    @JsonProperty("CNSLT_ANS_CONT")
    @Schema(description = "상담답변내용", example = "서비스 처리 완료")
    private String CNSLT_ANS_CONT;

    @JsonProperty("RCPTS_MBR_NO")
    @Schema(description = "접수자회원번호", example = "R56789")
    private String RCPTS_MBR_NO;
}
