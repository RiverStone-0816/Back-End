package kr.co.eicn.ippbx.model.dto.eicn.search;

import lombok.Data;

@Data
public class SearchPDSGroupResponse {
    private Integer seq;        //seq값
    private String name;        //pds명
    /**
     * @see kr.co.eicn.ippbx.model.enums.PDSGroupConnectKind;
     **/
    private String connectionKind;
    /**
     * @see kr.co.eicn.ippbx.model.enums.PDSGroupResultKind;
     **/
    private String resultKind;
}
