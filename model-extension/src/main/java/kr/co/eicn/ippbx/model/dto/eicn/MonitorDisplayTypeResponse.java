package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class MonitorDisplayTypeResponse {
    private Integer seq;
    private String displayName; //전광판명칭
    /**
     * @see kr.co.eicn.ippbx.model.enums.DesignType
     */
    private String designType;  //사용디자인
    /**
     * @see kr.co.eicn.ippbx.model.enums.DisplayData
     */
    private String displayData; //표시데이터
    /**
     * @see kr.co.eicn.ippbx.model.enums.SlidingYn
     */
    private String slidingYn;   //슬라이딩 문구 사용여부
}
