package kr.co.eicn.ippbx.model.form.provision;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.eicn.ippbx.util.spring.BaseForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProvisionResultCustomInfoFormRequest extends BaseForm {
    @Schema(description = "업체번호", example = "")
    private String FIRM_NO;
    @Schema(description = "회사명", example = "")
    private String FIRM_NM;
    @Schema(description = "사업자번호", example = "")
    private String BRNO;
    @Schema(description = "법인등록번호", example = "")
    private String JURIRNO;
    @Schema(description = "주소", example = "")
    private String ADDR;
    @Schema(description = "대표전화번호", example = "")
    private String RPRS_TELNO;
    @Schema(description = "업체이메일", example = "")
    private String FIRM_EMAIL_ADDR;
    @Schema(description = "업체타입", example = "")
    private String FIRM_TYPE_CD;
    @Schema(description = "업체타입명", example = "")
    private String FIRM_TYPE_NM;
    @Schema(description = "소속구분코드", example = "")
    private String PSITN_TYPE_CD;
    @Schema(description = "소속구분코드명", example = "")
    private String PSITN_TYPE_NM;
    @Schema(description = "취급분류코드", example = "")
    private String TRMT_CLS_CD;
    @Schema(description = "취급분류코드명", example = "")
    private String TRMT_CLS_NM;
    @Schema(description = "등록상태코드", example = "")
    private String RGSTN_STAT_CD;
    @Schema(description = "등록상태명", example = "")
    private String RGSTN_STAT_NM;
    @Schema(description = "업체상태코드", example = "")
    private String FIRM_STAT_CD;
    @Schema(description = "업체상태코드명", example = "")
    private String FIRM_STAT_NM;
    @Schema(description = "회원번호", example = "")
    private String MMNO;
    @Schema(description = "회원명", example = "")
    private String MBR_NM;
    @Schema(description = "회원이메일", example = "")
    private String EMAIL_ADDR;
    @Schema(description = "휴대폰번호", example = "")
    private String MOBL_TELNO;
    @Schema(description = "상담제목", example = "")
    private String CNSLT_TTL;
    @Schema(description = "상담유형대분류코드", example = "")
    private String CNSLT_TYPE_LCLS_CD;
    @Schema(description = "상담유형중분류코드", example = "")
    private String CNSLT_TYPE_MCLS_CD;
    @Schema(description = "상담접수채널코드", example = "")
    private String CNSLT_ACPT_CHNNL_CD;
    @Schema(description = "상담요청내용", example = "")
    private String CNSLT_REQUST_CONT;
    @Schema(description = "상담답변내용", example = "")
    private String CNSLT_ANS_CONT;
    @Schema(description = "접수자회원번호", example = "")
    private String RCPTS_MBR_NO;
}
