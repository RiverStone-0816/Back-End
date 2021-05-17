package kr.co.eicn.ippbx.server.model.dto.util;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@AllArgsConstructor
@Data
public class DateResponse {
    private Date date;

    @Override
    public String toString() {
        return date.toString();
    }
}
