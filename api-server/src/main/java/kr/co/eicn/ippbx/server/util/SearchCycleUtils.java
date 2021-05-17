package kr.co.eicn.ippbx.server.util;

import kr.co.eicn.ippbx.server.model.dto.util.*;
import kr.co.eicn.ippbx.server.model.entity.statdb.StatByTimeUnit;
import kr.co.eicn.ippbx.server.model.enums.SearchCycle;

import javax.xml.crypto.Data;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SearchCycleUtils {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static List<?> getDateByWeekOfCurrentMonth() {

        Calendar currentDate = Calendar.getInstance();
        String endDate = dateFormat.format(currentDate.getTime());
        currentDate.add(Calendar.MONTH, -1);
        String startDate = dateFormat.format(currentDate.getTime());

        return getDateByType(Date.valueOf(startDate), Date.valueOf(endDate), SearchCycle.WEEK);
    }

    public static List<?> getDateByType(Date searchStartDate, Date searchEndDate, SearchCycle timeUnit) {
        List<?> searchDateList = new ArrayList<>();
        Calendar startDate = new GregorianCalendar();
        Calendar endDate = new GregorianCalendar();

        startDate.setTime(searchStartDate);
        endDate.setTime(searchEndDate);

        if (SearchCycle.DAY_OF_WEEK.equals(timeUnit)) {
            List<DayOfWeekResponse> dayOfWeekList = new ArrayList<>();

            for (int dayOfWeek = 1; dayOfWeek <= 7; dayOfWeek++) {
                dayOfWeekList.add(new DayOfWeekResponse(dayOfWeek));
            }

            return dayOfWeekList;
        } else if (SearchCycle.DATE.equals(timeUnit)) {
            List<DateResponse> dateList = new ArrayList<>();

            int dayDiff = (int) ((endDate.getTimeInMillis() - startDate.getTimeInMillis()) / 1000 / (60 * 60 * 24));
            for (int i = 0; i <= dayDiff; i++) {
                dateList.add(new DateResponse(Date.valueOf(dateFormat.format(startDate.getTime()))));

                startDate.add(Calendar.DATE, 1);
            }

            return dateList;
        } else if (SearchCycle.HOUR.equals(timeUnit)) {
            List<HourResponse> hourList = new ArrayList<>();
            for (byte statHour = 0; statHour <= 23; statHour++) {
                hourList.add(new HourResponse(statHour));
            }

            return hourList;
        } else if (SearchCycle.WEEK.equals(timeUnit)) {
            List<WeekResponse> weekList = new ArrayList<>();
            int weekDiff = (int) ((endDate.getTimeInMillis() - startDate.getTimeInMillis()) / 1000 / (60 * 60 * 24 * 7));
            if (((endDate.getTimeInMillis() - startDate.getTimeInMillis()) / 1000 / (60 * 60 * 24)) % 7 > 0)
                weekDiff += 1;

            Calendar startOfWeek = new GregorianCalendar();
            startOfWeek.setTime(searchStartDate);
            startOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

            Calendar endOfWeek = new GregorianCalendar();
            endOfWeek.setTime(searchStartDate);
            endOfWeek.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);

            Calendar weekBaseDate = new GregorianCalendar();
            weekBaseDate.setTime(searchStartDate);
            weekBaseDate.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);

            Calendar firstDayOfMonth = new GregorianCalendar();
            firstDayOfMonth.setTime(searchStartDate);
            firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1);

            for (int i = 0; i <= weekDiff; i++) {
                WeekResponse weekResponse = new WeekResponse();
                Date startDateOfWeek = Date.valueOf(dateFormat.format(startOfWeek.getTime()));
                Date endDateOfWeek = Date.valueOf(dateFormat.format(endOfWeek.getTime()));

                weekResponse.setYear(weekBaseDate.get(Calendar.YEAR));
                weekResponse.setMonth(weekBaseDate.get(Calendar.MONTH) + 1);
                if (firstDayOfMonth.get(Calendar.DAY_OF_WEEK) >= Calendar.THURSDAY)
                    weekResponse.setWeekOfMonth(weekBaseDate.get(Calendar.WEEK_OF_MONTH)-1);
                else
                    weekResponse.setWeekOfMonth(weekBaseDate.get(Calendar.WEEK_OF_MONTH));

                if (i == 0)
                    weekResponse.setStartDateOfWeek(searchStartDate);
                else
                    weekResponse.setStartDateOfWeek(startDateOfWeek);

                weekResponse.setEndDateOfWeek(endDateOfWeek);
                if (endOfWeek.compareTo(endDate) >= 0) {
                    weekResponse.setEndDateOfWeek(searchEndDate);
                    weekList.add(weekResponse);
                    break;
                }

                startOfWeek.add(Calendar.DATE, 7);
                endOfWeek.add(Calendar.DATE, 7);
                weekBaseDate.add(Calendar.DATE, 7);
                firstDayOfMonth.set(Calendar.MONTH, weekBaseDate.get(Calendar.MONTH));

                weekList.add(weekResponse);
            }

            return weekList;
        } else if (SearchCycle.MONTH.equals(timeUnit)) {
            List<MonthResponse> monthList = new ArrayList<>();
            Calendar monthStart = new GregorianCalendar();
            Calendar monthEnd = new GregorianCalendar();

            int yearDiff = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);
            int monthDiff = yearDiff * 12 + (endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH));

            monthStart.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.getMinimum(Calendar.DAY_OF_MONTH));
            monthEnd.set(startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.getActualMaximum(Calendar.DAY_OF_MONTH));

            for (int i = 0; i <= monthDiff + 1; i++) {
                MonthResponse monthResponse;
                Date startDateOfMonth = Date.valueOf(dateFormat.format(monthStart.getTime()));
                Date endDateOfMonth = Date.valueOf(dateFormat.format(monthEnd.getTime()));

                if (i == 0) {
                    monthResponse = new MonthResponse(searchStartDate, endDateOfMonth, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH) + 1);

                    if (monthDiff == 0) {
                        monthResponse.setEndDateOfMonth(searchEndDate);
                        monthList.add(monthResponse);
                        break;
                    }
                } else {
                    monthResponse = new MonthResponse(startDateOfMonth, endDateOfMonth, monthStart.get(Calendar.YEAR), monthStart.get(Calendar.MONTH) + 1);
                    if (monthEnd.compareTo(endDate) >= 0) {
                        monthResponse.setEndDateOfMonth(searchEndDate);
                        monthList.add(monthResponse);
                        break;
                    }
                }
                monthList.add(monthResponse);

                monthStart.add(Calendar.MONTH, 1);
                monthEnd.add(Calendar.MONTH, 1);
                monthEnd.set(Calendar.DATE, monthEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
            }
            return monthList;
        }

        return searchDateList;
    }

    public static <T extends StatByTimeUnit> List<T> streamFiltering(List<T> list, SearchCycle timeUnit, Object timeInformation) {
        Stream<T> stream = list.stream();
        if (timeUnit.equals(SearchCycle.DATE)) {
            DateResponse date = (DateResponse) timeInformation;
            stream = stream.filter(data -> data.getStatDate().equals(date.getDate()));
        } else if (timeUnit.equals(SearchCycle.HOUR)) {
            HourResponse hour = (HourResponse) timeInformation;
            stream = stream.filter(data -> data.getStatHour().equals(hour.getHour()));
        } else if (timeUnit.equals(SearchCycle.WEEK)) {
            WeekResponse week = (WeekResponse) timeInformation;
            stream = stream.filter(data -> data.getYear().equals(week.getYear()) && data.getMonth().equals(week.getMonth()) && data.getWeek().equals(week.getWeekOfMonth()));
        } else if (timeUnit.equals(SearchCycle.MONTH)) {
            MonthResponse month = (MonthResponse) timeInformation;
            stream = stream.filter(data -> data.getYear().equals(month.getYear()) && data.getMonth().equals(month.getMonth()));
        } else if (timeUnit.equals(SearchCycle.DAY_OF_WEEK)) {
            DayOfWeekResponse dayOfWeek = (DayOfWeekResponse) timeInformation;
            stream = stream.filter(data -> data.getDayOfWeek().equals(dayOfWeek.getDayOfWeek()));
        }

        return stream.collect(Collectors.toList());
    }

    /*public static <T> T convertTimeInformation(SearchCycle timeUnit, Object timeInformation) {
        if (timeUnit.equals(SearchCycle.DATE)) {
            DateResponse date = (DateResponse) timeInformation;
            return date;
        } else if (timeUnit.equals(SearchCycle.HOUR)) {
            HourResponse hour = (HourResponse) timeInformation;
            stream = stream.filter(inbound -> inbound.getStatHour().equals(hour.getHour()));
        } else if (timeUnit.equals(SearchCycle.WEEK)) {
            WeekResponse week = (WeekResponse) timeInformation;
            stream = stream.filter(inbound -> inbound.getYear().equals(week.getYear()) && inbound.getMonth().equals(week.getMonth()));
        } else if (timeUnit.equals(SearchCycle.MONTH)) {
            MonthResponse month = (MonthResponse) timeInformation;
            stream = stream.filter(inbound -> inbound.getYear().equals(month.getYear()) && inbound.getMonth().equals(month.getMonth()));
        } else if (timeUnit.equals(SearchCycle.DAY_OF_WEEK)) {
            DayOfWeekResponse dayOfWeek = (DayOfWeekResponse) timeInformation;
            stream = stream.filter(inbound -> inbound.getDayOfWeek().equals(dayOfWeek.getDayOfWeek()));
        }

        return stream;
    }*/
}