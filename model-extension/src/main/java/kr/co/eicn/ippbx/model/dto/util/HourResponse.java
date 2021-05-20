package kr.co.eicn.ippbx.model.dto.util;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class HourResponse {
    private Byte hour;

    @Override
    public String toString() {
        return hour + "시";
    }
}
