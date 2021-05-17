package kr.co.eicn.ippbx.server.model.dto.statdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StatCategoryIvrResponse {
    private String ivrName;

    private List<StatCategoryIvrPathResponse> recordNameList = new ArrayList<>();
}
