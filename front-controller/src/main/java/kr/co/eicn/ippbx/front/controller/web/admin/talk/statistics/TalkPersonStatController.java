package kr.co.eicn.ippbx.front.controller.web.admin.talk.statistics;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.WebchatConfigApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatServiceSummaryInfoResponse;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.talk.group.TalkReceptionGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.talk.statistics.TalkPersonStatApiInterface;
import kr.co.eicn.ippbx.front.service.excel.TalkPersonStatExcel;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryTalkServiceResponse;
import kr.co.eicn.ippbx.model.dto.statdb.TalkStatisticsPersonResponse;
import kr.co.eicn.ippbx.model.search.TalkStatisticsSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@LoginRequired
@Controller
@RequestMapping("admin/talk/statistics/person")
public class TalkPersonStatController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TalkPersonStatController.class);

    @Autowired
    private TalkPersonStatApiInterface apiInterface;
    @Autowired
    private TalkReceptionGroupApiInterface talkReceptionGroupApiInterface;
    @Autowired
    private WebchatConfigApiInterface webchatConfigApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") TalkStatisticsSearchRequest search) throws IOException, ResultFailException {
        final List<TalkStatisticsPersonResponse> list = apiInterface.list(search);
        model.addAttribute("list", list);

        final Map<String, String> chatServiceMap = webchatConfigApiInterface.list().stream().collect(Collectors.toMap(WebchatServiceSummaryInfoResponse::getSenderKey, WebchatServiceSummaryInfoResponse::getChannelName));
        final Map<String, String> talkServices = talkReceptionGroupApiInterface.talkServices().stream().collect(Collectors.toMap(SummaryTalkServiceResponse::getSenderKey, SummaryTalkServiceResponse::getKakaoServiceName));
        talkServices.putAll(chatServiceMap);
        model.addAttribute("talkServices", talkServices);

        return "admin/talk/statistics/person/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(TalkStatisticsSearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        search.setPage(1);
        search.setLimit(100000);

        new TalkPersonStatExcel(search, apiInterface.list(search)).generator(response, "상담톡상담원별통계");
    }
}
