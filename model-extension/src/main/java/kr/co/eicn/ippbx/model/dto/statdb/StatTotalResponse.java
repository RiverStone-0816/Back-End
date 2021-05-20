package kr.co.eicn.ippbx.model.dto.statdb;

import lombok.Data;

import java.util.List;

@Data
public class StatTotalResponse<T> {
    private List<StatTotalRow<T>> rows;
}
