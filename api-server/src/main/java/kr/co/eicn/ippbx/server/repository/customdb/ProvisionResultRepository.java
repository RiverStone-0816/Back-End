package kr.co.eicn.ippbx.server.repository.customdb;


import static kr.co.eicn.ippbx.meta.jooq.customdb.tables.ResultCustomInfo.RESULT_CUSTOM_INFO;

import kr.co.eicn.ippbx.meta.jooq.customdb.CommonRoutines;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.ResultCustomInfo;
import kr.co.eicn.ippbx.model.entity.customdb.ResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.form.provision.ProvisionResultCustomInfoFormRequest;
import lombok.Getter;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Getter
@Repository
public class ProvisionResultRepository extends CustomDBBaseRepository<ResultCustomInfo, ResultCustomInfoEntity, Integer> {

  protected final Logger logger = LoggerFactory.getLogger(ProvisionResultRepository.class);
  private static final String ENCRYPTION_KEY_PREFIX = "eicn_";

  public ProvisionResultRepository() {
    super(RESULT_CUSTOM_INFO, RESULT_CUSTOM_INFO.SEQ, ResultCustomInfoEntity.class);
  }

  public void saveConsultation(ProvisionResultCustomInfoFormRequest request) {
    dsl.insertInto(RESULT_CUSTOM_INFO)
        .set(RESULT_CUSTOM_INFO.SEQ, calculateNextSeq()) // SEQ
        .set(RESULT_CUSTOM_INFO.RS_STRING_1, request.getCNSLT_TTL()) // 상담제목
        .set(RESULT_CUSTOM_INFO.RS_STRING_2, request.getCNSLT_REQUST_CONT()) // 상담요청내용
        .set(RESULT_CUSTOM_INFO.RS_STRING_3, request.getCNSLT_ANS_CONT()) // 상담답변내용
        .set(RESULT_CUSTOM_INFO.RS_STRING_4, CommonRoutines.fnEncStringText(request.getMBR_NM(), ENCRYPTION_KEY_PREFIX+getCompanyId())) // 회원명
        .set(RESULT_CUSTOM_INFO.RS_STRING_5, CommonRoutines.fnEncStringText(request.getMOBL_TELNO(), ENCRYPTION_KEY_PREFIX+getCompanyId())) // 휴대폰번호
        .set(RESULT_CUSTOM_INFO.RS_CODE_1, request.getCNSLT_ACPT_CHNNL_CD()) // 상담접수채널코드
        .set(RESULT_CUSTOM_INFO.RS_CODE_2, request.getCNSLT_TYPE_LCLS_CD()) // 상담유형대분류코드
        .set(RESULT_CUSTOM_INFO.RS_CODE_3, request.getCNSLT_TYPE_MCLS_CD()) // 상담유형중분류코드
        .execute();
  }

  private int calculateNextSeq() {
    Integer currentMaxSeq = dsl.select(DSL.max(RESULT_CUSTOM_INFO.SEQ))
        .from(RESULT_CUSTOM_INFO)
        .fetchOne(0, Integer.class);
    return (currentMaxSeq == null) ? 1 : currentMaxSeq + 1;
  }

}
