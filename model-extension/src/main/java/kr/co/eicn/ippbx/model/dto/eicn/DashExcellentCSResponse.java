package kr.co.eicn.ippbx.model.dto.eicn;

import lombok.Data;

import java.util.List;

@Data
public class DashExcellentCSResponse {
    private String title;
    private List<DashExcellentCSListResponse> userList;  //대표서비스 응답률
}
