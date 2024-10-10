package kr.co.eicn.ippbx.front.controller.web.admin.wtalk;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.ChatbotApiInterface;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.wtalk.group.WtalkReceptionGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.wtalk.history.WtalkHistoryApiInterface;
import kr.co.eicn.ippbx.front.service.excel.TalkHistoryStatExcel;
import kr.co.eicn.ippbx.model.dto.customdb.WtalkMsgResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryWtalkServiceResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WtalkRoomResponse;
import kr.co.eicn.ippbx.model.enums.RoomStatus;
import kr.co.eicn.ippbx.model.form.TalkTemplateFormRequest;
import kr.co.eicn.ippbx.model.search.TalkRoomSearchRequest;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.MapToLinkedHashMap;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
@RequiredArgsConstructor
@Controller
@RequestMapping("admin/wtalk/history")
public class WtalkHistoryController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WtalkHistoryController.class);

    private final WtalkHistoryApiInterface apiInterface;
    private final WtalkReceptionGroupApiInterface talkReceptionGroupApiInterface;
    private final ChatbotApiInterface chatbotApiInterface;
    private final SearchApiInterface searchApiInterface;

    @Value("${eicn.wtalk.socket.id}")
    private String talkSocketId;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") TalkRoomSearchRequest search) throws IOException, ResultFailException {
        if (StringUtils.isEmpty(search.getRoomStatus()))
            search.setRoomStatus(RoomStatus.PROCESS_OR_STOP.getCode());

        if (StringUtils.isEmpty(search.getSequence()))
            search.setSequence("desc");

        if (search.getSorts() == null)
            search.setSorts(TalkRoomSearchRequest.Sorts.START_TIME.getCode());

        final Pagination<WtalkRoomResponse> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        final Map<String, String> roomStatuses = FormUtils.optionsOfCode(RoomStatus.PROCESS_OR_STOP, RoomStatus.DOWN);
        model.addAttribute("roomStatuses", roomStatuses);

        final Map<String, String> talkServices = talkReceptionGroupApiInterface.talkServices().stream().collect(Collectors.toMap(SummaryWtalkServiceResponse::getSenderKey, SummaryWtalkServiceResponse::getKakaoServiceName));
        model.addAttribute("talkServices", new MapToLinkedHashMap().toLinkedHashMapByValue(talkServices));

        model.addAttribute("users", searchApiInterface.persons());

        final Map<String, String> orderTypes = FormUtils.optionsOfCode(TalkRoomSearchRequest.Sorts.class);
        model.addAttribute("orderTypes", orderTypes);

        return "admin/wtalk/history/ground";
    }

    @GetMapping("{seq}/modal")
    public String modal(Model model, @PathVariable Integer seq, @RequestParam String roomStatus, @RequestParam String roomId, @ModelAttribute("form") TalkTemplateFormRequest form) throws IOException, ResultFailException {
        final WtalkRoomResponse entity = apiInterface.get(seq, roomStatus);
        model.addAttribute("entity", entity);

        final List<WtalkMsgResponse> messageHistory =  apiInterface.messageHistory(roomId).stream().map(e -> {
            if (e.getSendReceive().equals("SB") && e.getType().equals("block")) e.setBlockInfo(chatbotApiInterface.getBlock(Integer.parseInt(e.getContent().split(":")[2])));
            if (e.getSendReceive().equals("S") && e.getType().equals("block")) e.setBlockInfo(chatbotApiInterface.getBlock(Integer.parseInt(e.getContent())));
            if (e.getSendReceive().equals("RARC") || e.getSendReceive().equals("RVRC")) {
                String[] records = e.getContent().replaceAll("\"","").split(",");
                String record = "";
                for (String r : records) {
                    if (r.contains("record_file:")) record = r.replaceAll("record_file:","").replaceAll("}","");
                };
                e.setContent(record);
            }
            return e;
        }).collect(Collectors.toList());;
        messageHistory.sort(Comparator.comparingInt(WtalkMsgResponse::getSeq));
        model.addAttribute("messageHistory", messageHistory);

        final Map<String, String> socketMap = g.getSocketList();
        model.addAttribute("imageUrl", socketMap.get(talkSocketId));

        return "admin/wtalk/history/modal";
    }

    @GetMapping("modal")
    public String modal(Model model, @RequestParam String roomId) throws IOException, ResultFailException {
        final WtalkRoomResponse entity = apiInterface.get(roomId);
        model.addAttribute("entity", entity);

        final List<WtalkMsgResponse> messageHistory = apiInterface.messageHistory(roomId).stream().map(e -> {
            if (e.getSendReceive().equals("SB") && e.getType().equals("block") ) e.setBlockInfo(chatbotApiInterface.getBlock(Integer.parseInt(e.getContent().split(":")[2])));
            if (e.getSendReceive().equals("S") && e.getType().equals("block")) e.setBlockInfo(chatbotApiInterface.getBlock(Integer.parseInt(e.getContent())));
            if (e.getSendReceive().equals("RARC") || e.getSendReceive().equals("RVRC")) {
                String[] records = e.getContent().replaceAll("\"","").split(",");
                String record = "";
                for (String r : records) {
                    if (r.contains("record_file:")) record = r.replaceAll("record_file:","").replaceAll("}","");
                };
                e.setContent(record);
            }
            return e;
        }).collect(Collectors.toList());
        messageHistory.sort(Comparator.comparingInt(WtalkMsgResponse::getSeq));
        model.addAttribute("messageHistory", messageHistory);

        final Map<String, String> socketMap = g.getSocketList();
        model.addAttribute("imageUrl", socketMap.get(talkSocketId));

        return "admin/wtalk/history/modal";
    }

    @GetMapping("_excel")
    public void downloadExcel(TalkRoomSearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        search.setPage(1);
        search.setLimit(100000);

        new TalkHistoryStatExcel(apiInterface.pagination(search).getRows()).generator(response, "상담톡이력");
    }
}
