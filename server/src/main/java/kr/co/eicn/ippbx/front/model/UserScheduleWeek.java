package kr.co.eicn.ippbx.front.model;

import lombok.Data;

import java.util.Calendar;

@Data
public class UserScheduleWeek {
    private UserScheduleDay sunday;
    private UserScheduleDay monday;
    private UserScheduleDay tuesday;
    private UserScheduleDay wednesday;
    private UserScheduleDay thursday;
    private UserScheduleDay friday;
    private UserScheduleDay saturday;

    public void setDay(UserScheduleDay day) {
        if (day.getDayOfWeek() == Calendar.SUNDAY) sunday = day;
        if (day.getDayOfWeek() == Calendar.MONDAY) monday = day;
        if (day.getDayOfWeek() == Calendar.TUESDAY) tuesday = day;
        if (day.getDayOfWeek() == Calendar.WEDNESDAY) wednesday = day;
        if (day.getDayOfWeek() == Calendar.THURSDAY) thursday = day;
        if (day.getDayOfWeek() == Calendar.FRIDAY) friday = day;
        if (day.getDayOfWeek() == Calendar.SATURDAY) saturday = day;
    }

    public boolean isEmpty() {
        return sunday == null &&
                monday == null &&
                tuesday == null &&
                wednesday == null &&
                thursday == null &&
                friday == null &&
                saturday == null;
    }
}
