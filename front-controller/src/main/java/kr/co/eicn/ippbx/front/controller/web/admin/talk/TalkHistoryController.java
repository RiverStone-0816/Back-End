package kr.co.eicn.ippbx.front.controller.web.admin.talk;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.controller.api.user.user.UserApiController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.talk.group.TalkReceptionGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.talk.history.TalkHistoryApiInterface;
import kr.co.eicn.ippbx.front.service.excel.TalkHistoryStatExcel;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.customdb.TalkMsgResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PersonSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryTalkServiceResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkRoomResponse;
import kr.co.eicn.ippbx.model.enums.RoomStatus;
import kr.co.eicn.ippbx.model.form.TalkTemplateFormRequest;
import kr.co.eicn.ippbx.model.search.PersonSearchRequest;
import kr.co.eicn.ippbx.model.search.TalkRoomSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/talk/history")
public class TalkHistoryController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TalkHistoryController.class);

    @Autowired
    private TalkHistoryApiInterface apiInterface;
    @Autowired
    private TalkReceptionGroupApiInterface talkReceptionGroupApiInterface;
    @Autowired
    private UserApiController userApiController;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") TalkRoomSearchRequest search) throws IOException, ResultFailException {
        if (StringUtils.isEmpty(search.getRoomStatus()))
            search.setRoomStatus(RoomStatus.PROCESS_OR_STOP.getCode());

        if (StringUtils.isEmpty(search.getSequence()))
            search.setSequence("desc");

        if (search.getSorts() == null)
            search.setSorts(TalkRoomSearchRequest.Sorts.START_TIME);

        final Pagination<TalkRoomResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        final Map<String, String> roomStatuses = FormUtils.optionsOfCode(RoomStatus.PROCESS_OR_STOP, RoomStatus.DOWN);
        model.addAttribute("roomStatuses", roomStatuses);

        final Map<String, String> talkServices = talkReceptionGroupApiInterface.talkServices().stream().collect(Collectors.toMap(SummaryTalkServiceResponse::getSenderKey, SummaryTalkServiceResponse::getKakaoServiceName));
        model.addAttribute("talkServices", talkServices);

        final PersonSearchRequest personSearchRequest = new PersonSearchRequest();
        personSearchRequest.setLimit(1000);
        final Map<String, String> users = userApiController.pagination(personSearchRequest).getRows().stream().collect(Collectors.toMap(PersonSummaryResponse::getId, PersonSummaryResponse::getIdName));
        model.addAttribute("users", users);

        final Map<String, String> orderTypes = FormUtils.options(false, TalkRoomSearchRequest.Sorts.class);
        model.addAttribute("orderTypes", orderTypes);

        return "admin/talk/history/ground";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @RequestParam String roomStatus, @RequestParam String roomId, @ModelAttribute("form") TalkTemplateFormRequest form) throws IOException, ResultFailException {
        final TalkRoomResponse entity = apiInterface.get(seq, roomStatus);
        model.addAttribute("entity", entity);

        final List<TalkMsgResponse> messageHistory = apiInterface.messageHistory(roomId);
        messageHistory.sort(Comparator.comparingInt(TalkMsgResponse::getSeq));
        model.addAttribute("messageHistory", messageHistory);

        return "admin/talk/history/modal";
    }

    @GetMapping("modal")
    public String modal(Model model, @RequestParam String roomId) throws IOException, ResultFailException {
        final TalkRoomResponse entity = apiInterface.get(roomId);
        model.addAttribute("entity", entity);

        final List<TalkMsgResponse> messageHistory = apiInterface.messageHistory(roomId);
        messageHistory.sort(Comparator.comparingInt(TalkMsgResponse::getSeq));
        model.addAttribute("messageHistory", messageHistory);

        return "admin/talk/history/modal";
    }

    @GetMapping("_excel")
    public void downloadExcel(TalkRoomSearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        search.setPage(1);
        search.setLimit(100000);

        new TalkHistoryStatExcel(apiInterface.pagination(search).getRows()).generator(response, "상담톡이력");
    }
}
