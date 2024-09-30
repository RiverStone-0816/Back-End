package kr.co.eicn.ippbx.model.search;

import lombok.Data;

import java.util.List;

@Data
public class ResultRecordSearchRequest {
    public List<String> uniqueIds;
}