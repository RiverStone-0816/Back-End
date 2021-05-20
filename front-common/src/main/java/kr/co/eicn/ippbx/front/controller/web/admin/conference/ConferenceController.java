package kr.co.eicn.ippbx.front.controller.web.admin.conference;

import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.ConferenceDay;
import kr.co.eicn.ippbx.front.model.ConferenceWeek;
import kr.co.eicn.ippbx.front.model.form.ConfInfoCopyForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.conference.ConferenceApiInterface;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.enums.ConfInfoIsRecord;
import kr.co.eicn.ippbx.model.form.ConfInfoFormRequest;
import kr.co.eicn.ippbx.model.form.ConfInfoMinutesSaveFormRequest;
import kr.co.eicn.ippbx.model.search.ConfInfoSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/conference/conference/conference")
public class ConferenceController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ConferenceController.class);
    private static final int LAST_DAY_OF_WEEK = Calendar.SATURDAY;

    @Autowired
    private ConferenceApiInterface apiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") ConfInfoSearchRequest search) throws IOException, ResultFailException {
        final List<ConfRoomSummaryResponse> roomList = apiInterface.getConfRoomList();
        final Map<String, String> rooms = roomList.stream().collect(Collectors.toMap(ConfRoomDetailResponse::getRoomNumber, ConfRoomDetailResponse::getRoomName));
        model.addAttribute("rooms", rooms);

        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        final int year = search.getReserveYear() != null && search.getReserveYear() >= 1970 && search.getReserveYear() <= 2100 ? search.getReserveYear() : calendar.get(Calendar.YEAR);
        search.setReserveYear(year);
        final int month = search.getReserveMonth() != null && search.getReserveMonth() >= 1 && search.getReserveMonth() <= 12 ? search.getReserveMonth() : calendar.get(Calendar.MONTH) + 1;
        search.setReserveMonth(month);

        if (roomList.size() > 0) {
            final String firstRoomNumber = roomList.get(0).getRoomNumber();
            if (StringUtils.isEmpty(search.getRoomNumber()))
                search.setRoomNumber(firstRoomNumber);
        }

        final List<ConfInfoSummaryResponse> list = apiInterface.list(search);
        model.addAttribute("list", list);

        calendar.setTimeInMillis(0);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 0);

        final int maxDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        final List<ConferenceWeek> monthData = new ArrayList<>();
        ConferenceWeek week = new ConferenceWeek();
        for (int dayOfMonth = 1; dayOfMonth <= maxDayOfMonth; dayOfMonth++) {
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            final ConferenceDay day = new ConferenceDay();
            day.setDate(new Date(calendar.getTimeInMillis()));
            day.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
            day.setConferences(list.stream().filter(e -> Objects.equals(e.getReserveDate(), day.getDate())).collect(Collectors.toList()));

            week.setDay(day);

            if (day.getDayOfWeek() == LAST_DAY_OF_WEEK) {
                monthData.add(week);
                week = new ConferenceWeek();
            }
        }
        if (!week.isEmpty())
            monthData.add(week);

        model.addAttribute("monthData", monthData);

        return "admin/conference/conference/conference/ground";
    }

    @GetMapping("new/modal")
    public String modal(Model model, @ModelAttribute("form") ConfInfoFormRequest form, @RequestParam Date date) throws IOException, ResultFailException {
        final List<ConfRoomSummaryResponse> roomList = apiInterface.getConfRoomList();
        final Map<String, String> rooms = roomList.stream().collect(Collectors.toMap(ConfRoomDetailResponse::getRoomNumber, ConfRoomDetailResponse::getRoomName));
        model.addAttribute("rooms", rooms);

        form.setReserveDate(date);

        final Map<Integer, String> soundList = apiInterface.addSoundList().stream().collect(Collectors.toMap(SummarySoundListResponse::getSeq, SummarySoundListResponse::getSoundName));
        model.addAttribute("soundList", soundList);

        final Map<String, String> isRecordTypes = FormUtils.optionsOfCode(ConfInfoIsRecord.class);
        model.addAttribute("isRecordTypes", isRecordTypes);

        final Map<String, String> persons = apiInterface.addOnConfPersons(null).stream().collect(Collectors.toMap(SummaryPersonResponse::getPeer, SummaryPersonResponse::getIdName));
        model.addAttribute("persons", persons);

        return "admin/conference/conference/conference/modal";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @ModelAttribute("form") ConfInfoFormRequest form) throws IOException, ResultFailException {
        final ConfInfoDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);
        ReflectionUtils.copy(form, entity);

        final ConfRoomSummaryResponse room = apiInterface.getConfRoomList().stream().filter(e -> e.getRoomNumber().equals(entity.getRoomNumber())).findAny().orElse(null);
        model.addAttribute("room", room);

        final Map<Integer, String> soundList = apiInterface.addSoundList().stream().collect(Collectors.toMap(SummarySoundListResponse::getSeq, SummarySoundListResponse::getSoundName));
        model.addAttribute("soundList", soundList);

        final Map<String, String> isRecordTypes = FormUtils.optionsOfCode(ConfInfoIsRecord.class);
        model.addAttribute("isRecordTypes", isRecordTypes);

        final Map<String, String> persons = apiInterface.addOnConfPersons(null).stream().collect(Collectors.toMap(SummaryPersonResponse::getPeer, SummaryPersonResponse::getIdName));
        model.addAttribute("persons", persons);

        for (SummaryPersonResponse person : entity.getInMemberList())
            persons.remove(person.getPeer());

        return "admin/conference/conference/conference/modal";
    }

    @GetMapping("{seq}/modal-info")
    public String modalInfo(Model model, @PathVariable Integer seq, @ModelAttribute("form") ConfInfoMinutesSaveFormRequest form) throws IOException, ResultFailException {
        final ConfInfoDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);

        final ConfRoomSummaryResponse room = apiInterface.getConfRoomList().stream().filter(e -> e.getRoomNumber().equals(entity.getRoomNumber())).findAny().orElse(null);
        model.addAttribute("room", room);

        form.setConfId(entity.getSeq());

        try {
            final ConfInfoMinutesResponse minutes = apiInterface.minutes(seq);
            form.setConfMinute(minutes.getMinutesText());
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }

        return "admin/conference/conference/conference/modal-info";
    }

    @GetMapping("{seq}/modal-copy")
    public String modalCopy(Model model, @PathVariable Integer seq, @ModelAttribute("form") ConfInfoCopyForm form) throws IOException, ResultFailException {
        final ConfInfoDetailResponse entity = apiInterface.get(seq);
        model.addAttribute("entity", entity);

        form.setTargetSeq(seq);
        final List<ConfRoomSummaryResponse> roomList = apiInterface.getConfRoomList();
        final Map<String, String> rooms = roomList.stream().collect(Collectors.toMap(ConfRoomDetailResponse::getRoomNumber, ConfRoomDetailResponse::getRoomName));
        model.addAttribute("rooms", rooms);

        return "admin/conference/conference/conference/modal-copy";
    }

}
