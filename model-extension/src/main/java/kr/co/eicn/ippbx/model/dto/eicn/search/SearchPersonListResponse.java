package kr.co.eicn.ippbx.model.dto.eicn.search;

import lombok.Data;

@Data
public class SearchPersonListResponse {
    private String id;
    private String idName;
    private String groupCode;
    private String groupTreeName;
    private String idType;
}
