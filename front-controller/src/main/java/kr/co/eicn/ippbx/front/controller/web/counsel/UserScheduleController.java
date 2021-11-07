package kr.co.eicn.ippbx.front.controller.web.counsel;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.UserScheduleDay;
import kr.co.eicn.ippbx.front.model.UserScheduleWeek;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.UserScheduleApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.UserScheduleType;
import kr.co.eicn.ippbx.model.entity.eicn.UserScheduleEntity;
import kr.co.eicn.ippbx.model.form.UserScheduleFormRequest;
import kr.co.eicn.ippbx.model.search.UserScheduleSearchRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@Slf4j
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("user-schedule")
public class UserScheduleController extends BaseController {
    private static final int LAST_DAY_OF_WEEK = Calendar.SATURDAY;

    private final UserScheduleApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month) throws IOException, ResultFailException {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        year = year != null && year >= 1970 && year <= 2100 ? year : calendar.get(Calendar.YEAR);
        month = month != null && month >= 1 && month <= 12 ? month : calendar.get(Calendar.MONTH) + 1;

        model.addAttribute("year", year);
        model.addAttribute("month", month);
        return "counsel/user-schedule/ground";
    }

    @GetMapping("modal")
    public String modal(Model model, @RequestParam(required = false) Integer year, @RequestParam(required = false) Integer month) throws IOException, ResultFailException {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        year = year != null && year >= 1970 && year <= 2100 ? year : calendar.get(Calendar.YEAR);
        month = month != null && month >= 1 && month <= 12 ? month : calendar.get(Calendar.MONTH) + 1;

        model.addAttribute("year", year);
        model.addAttribute("month", month);
        return "counsel/user-schedule/modal";
    }

    @GetMapping("content")
    public String content(Model model, @RequestParam Integer year, @RequestParam Integer month) throws IOException, ResultFailException {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        year = year >= 1970 && year <= 2100 ? year : calendar.get(Calendar.YEAR);
        month = month >= 1 && month <= 12 ? month : calendar.get(Calendar.MONTH) + 1;

        final UserScheduleSearchRequest search = new UserScheduleSearchRequest();
        search.setYear(year);
        search.setMonth(month);
        final List<UserScheduleEntity> list = apiInterface.search(search);
        model.addAttribute("list", list);

        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        final int maxDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        final List<UserScheduleWeek> monthData = new ArrayList<>();
        UserScheduleWeek week = new UserScheduleWeek();
        for (int dayOfMonth = 1; dayOfMonth <= maxDayOfMonth; dayOfMonth++) {
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            final UserScheduleDay day = new UserScheduleDay();
            day.setDate(new Date(calendar.getTimeInMillis()));
            day.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));

            day.setSchedules(list.stream().filter(e -> {
                final long checkingStartTime = day.getDate().getTime();
                final long checkingEndTime = checkingStartTime + 1000 * 60 * 60 * 24 - 1;

                // 날짜가 겹치는지 확인하는 로직: !((interval A's end < interval B's start) || (interval B's end < interval A's start))
                return !((checkingEndTime < e.getStart().getTime()) || (e.getEnd().getTime() < checkingStartTime));
            }).collect(Collectors.toList()));

            day.getSchedules().sort((a, b) -> {
                if (!Objects.equals(g.dateFormat(a.getStart()), g.dateFormat(a.getEnd()))) {
                    if (Objects.equals(g.dateFormat(b.getStart()), g.dateFormat(b.getEnd())))
                        return -1;

                    long aTime = a.getEnd().getTime() - a.getStart().getTime();
                    long bTime = b.getEnd().getTime() - b.getStart().getTime();

                    return aTime > bTime ? -1 : 1;
                }
                if (!Objects.equals(g.dateFormat(b.getStart()), g.dateFormat(b.getEnd())))
                    return 1;

                if (a.isWholeDay())
                    return -1;

                if (a.getStart().getTime() > b.getStart().getTime())
                    return 0;

                return a.getStart().getTime() > b.getStart().getTime() ? -1 : 1;
            });

            week.setDay(day);

            if (day.getDayOfWeek() == LAST_DAY_OF_WEEK) {
                monthData.add(week);
                week = new UserScheduleWeek();
            }
        }
        if (!week.isEmpty())
            monthData.add(week);

        model.addAttribute("monthData", monthData);

        model.addAttribute("year", year);
        model.addAttribute("month", month);

        return "counsel/user-schedule/content";
    }

    @GetMapping("modal-item/{seq}")
    public String modalItem(Model model, @PathVariable Integer seq, @ModelAttribute("form") UserScheduleFormRequest form) throws IOException, ResultFailException {
        final UserScheduleEntity entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        return modalItem(model, form);
    }

    @GetMapping("modal-item")
    public String modalItem(Model model, @ModelAttribute("form") UserScheduleFormRequest form) {
        model.addAttribute("types", Objects.equals(g.getUser().getIdType(), "M")
                ? FormUtils.options(UserScheduleType.MINE, UserScheduleType.HOLYDAY)
                : FormUtils.options(UserScheduleType.MINE, UserScheduleType.HOLYDAY, UserScheduleType.ALL));

        return "counsel/user-schedule/modal-item";
    }
}
