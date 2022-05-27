package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class WtalkTemplateSummaryResponse {
    private Integer seq;
    private String type;
    private String typeMent;
    private String typeData;
    private String typeGroup;
    private String typeDataName;
    private Integer companyTreeLevel = 0;
    private String writeUserid;
    private String writeUserName;
    private String mentName;
    private String ment;
    private String originalFileName;
    private String filePath;
}
