package kr.co.eicn.ippbx.server.model.dto.util;

import lombok.Data;

import java.util.Objects;

@Data
public class DayOfWeekResponse {
    private Integer dayOfWeek;

    @Override
    public String toString() {
        return Objects.equals(dayOfWeek, 1) ? "일요일"
                : Objects.equals(dayOfWeek, 2) ? "월요일"
                : Objects.equals(dayOfWeek, 3) ? "화요일"
                : Objects.equals(dayOfWeek, 4) ? "수요일"
                : Objects.equals(dayOfWeek, 5) ? "목요일"
                : Objects.equals(dayOfWeek, 6) ? "금요일"
                : "토요일";
    }
}
