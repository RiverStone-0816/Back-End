package kr.co.eicn.ippbx.model.dto.statdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StatCategoryIvrResponse {
    private String ivrName; //IVR명

    private List<StatCategoryIvrPathResponse> recordNameList = new ArrayList<>();
}
