package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class MonitControlResponse {
    private String groupCode;
    private String groupName;
    private List<PersonListSummary> person;
}
