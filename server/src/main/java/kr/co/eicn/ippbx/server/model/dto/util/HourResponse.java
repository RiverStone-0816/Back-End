package kr.co.eicn.ippbx.server.model.dto.util;

import lombok.Data;

@Data
public class HourResponse {
    private Byte hour;

    @Override
    public String toString() {
        return hour + "시";
    }
}
