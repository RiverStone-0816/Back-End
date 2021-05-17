package kr.co.eicn.ippbx.front.model;

import kr.co.eicn.ippbx.server.model.dto.eicn.ConfInfoSummaryResponse;
import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class ConferenceDay {
    private Date date;
    private int dayOfWeek;
    private List<ConfInfoSummaryResponse> conferences;
}
