package kr.co.eicn.ippbx.server.model.dto.eicn;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Date;

@Data
public class SearchCycleUtilsResponse<T> {
    private T timeResponse;
}
