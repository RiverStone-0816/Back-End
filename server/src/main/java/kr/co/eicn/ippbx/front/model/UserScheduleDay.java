package kr.co.eicn.ippbx.front.model;

import com.ibm.icu.util.ChineseCalendar;
import kr.co.eicn.ippbx.server.model.entity.eicn.UserScheduleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Data
public class UserScheduleDay {
    private final static long ONE_DAY = 1000 * 60 * 60 * 24L;

    private Date date;
    private int dayOfWeek;
    private List<UserScheduleEntity> schedules;

    public LunarDate getLunarDate() {
        if (date == null)
            throw new IllegalStateException("date is null");

        final ChineseCalendar calendar = new ChineseCalendar();
        calendar.setTimeInMillis(date.getTime());

        return new LunarDate(
                calendar.get(ChineseCalendar.EXTENDED_YEAR) - 2637,
                calendar.get(ChineseCalendar.MONTH) + 1,
                calendar.get(ChineseCalendar.DAY_OF_MONTH)
        );
    }

    public Timestamp getDayStartTime(UserScheduleEntity e) {
        if (e.getStart().getTime() < date.getTime())
            return new Timestamp(date.getTime());

        return e.getStart();
    }

    public Timestamp getDayEndTime(UserScheduleEntity e) {
        if (e.getEnd().getTime() >= date.getTime() + ONE_DAY)
            return new Timestamp(date.getTime() + ONE_DAY - 1);

        return e.getEnd();
    }

    @AllArgsConstructor
    @Data
    public static class LunarDate {
        private Integer year;
        private Integer month;
        private Integer day;
    }
}
