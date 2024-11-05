package kr.co.eicn.ippbx.model.dto.statdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class StatCategoryResponse<T> {
    private T       timeInformation;    //날짜/시간
    private String  svcName;            //서비스명
    private Integer maxLevel;           //IVR 최대 레벨

    private List<StatCategoryIvrResponse> recordList = new ArrayList<>();
}
