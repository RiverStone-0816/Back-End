package kr.co.eicn.ippbx.model.dto.statdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StatCategoryResponse<T> {
    private T timeInformation;
    private String svcName;
    private Integer maxLevel;

    private List<StatCategoryIvrResponse> recordList = new ArrayList<>();
}
