package kr.co.eicn.ippbx.model.dto.eicn;

import kr.co.eicn.ippbx.model.entity.eicn.WtalkScheduleInfoEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class WtalkScheduleInfoDetailResponse extends WtalkScheduleInfoEntity {
    private String kakaoServiceName; // 상담톡 서비스명
    private String isChattEnable; // 상담톡 활성화여부
}
