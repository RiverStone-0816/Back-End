package kr.co.eicn.ippbx.model.dto.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DateResponse {
    private Date date;

    @Override
    public String toString() {
        return date.toString();
    }
}
