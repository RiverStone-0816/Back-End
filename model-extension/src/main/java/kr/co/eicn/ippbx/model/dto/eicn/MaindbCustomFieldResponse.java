package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

@Data
public class MaindbCustomFieldResponse {
    private String  fieldId;
    private String  fieldType;
    private String  fieldUse;
    private String  fieldInfo;
    private Integer fieldSize;
    private String  isneed;
    private String  isdisplay;
    private String  isdisplayList;
    private String  issearch;
    private Integer displaySeq;
}
