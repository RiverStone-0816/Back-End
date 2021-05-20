package kr.co.eicn.ippbx.front.model;

import lombok.Data;

import java.util.Calendar;

@Data
public class ConferenceWeek {
    private ConferenceDay sunday;
    private ConferenceDay monday;
    private ConferenceDay tuesday;
    private ConferenceDay wednesday;
    private ConferenceDay thursday;
    private ConferenceDay friday;
    private ConferenceDay saturday;

    public void setDay(ConferenceDay day) {
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
