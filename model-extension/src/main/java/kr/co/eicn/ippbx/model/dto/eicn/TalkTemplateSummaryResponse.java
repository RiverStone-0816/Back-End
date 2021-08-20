package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class TalkTemplateSummaryResponse {
    private Integer seq;
    private String  type;
    private String  typeData;
    private String  typeDataName;
    private Integer companyTreeLevel = 0;
    private String  writeUserid;
    private String  writeUserName;
    private String  mentName;
    private String  ment;
}
