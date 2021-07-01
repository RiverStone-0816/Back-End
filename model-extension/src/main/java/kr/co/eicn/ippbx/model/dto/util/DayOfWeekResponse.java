package kr.co.eicn.ippbx.model.dto.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
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
