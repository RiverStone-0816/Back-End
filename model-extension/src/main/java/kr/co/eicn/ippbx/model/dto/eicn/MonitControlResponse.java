package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class MonitControlResponse {
    private String groupCode;
    private String groupName;
    private String groupTreeName;
    private List<PersonListSummary> person;
}
