package kr.co.eicn.ippbx.front.controller.web.admin.wtalk.statistics;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.WebchatConfigApiInterface;
import kr.co.eicn.ippbx.front.service.api.wtalk.group.WtalkReceptionGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.wtalk.statistics.WtalkHourlyStatApiInterface;
import kr.co.eicn.ippbx.front.service.excel.TalkHourlyStatExcel;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryWtalkServiceResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebchatServiceSummaryInfoResponse;
import kr.co.eicn.ippbx.model.dto.statdb.WtalkStatisticsHourlyResponse;
import kr.co.eicn.ippbx.model.search.TalkStatisticsHourlySearchRequest;
import kr.co.eicn.ippbx.util.MapToLinkedHashMap;
import kr.co.eicn.ippbx.util.ResultFailException;
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
@RequestMapping("admin/wtalk/statistics/hourly")
public class WtalkHourlyStatController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(WtalkHourlyStatController.class);

    @Autowired
    private WtalkHourlyStatApiInterface apiInterface;
    @Autowired
    private WtalkReceptionGroupApiInterface talkReceptionGroupApiInterface;
    @Autowired
    private WebchatConfigApiInterface webchatConfigApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") TalkStatisticsHourlySearchRequest search) throws IOException, ResultFailException {
        final List<WtalkStatisticsHourlyResponse> list = apiInterface.list(search);
        model.addAttribute("list", list);

        final Map<String, String> chatServiceMap = webchatConfigApiInterface.list().stream().collect(Collectors.toMap(WebchatServiceSummaryInfoResponse::getSenderKey, WebchatServiceSummaryInfoResponse::getChannelName));
        final Map<String, String> talkServices = talkReceptionGroupApiInterface.talkServices().stream().collect(Collectors.toMap(SummaryWtalkServiceResponse::getSenderKey, SummaryWtalkServiceResponse::getKakaoServiceName));
        talkServices.putAll(chatServiceMap);
        model.addAttribute("talkServices", new MapToLinkedHashMap().toLinkedHashMapByValue(talkServices));

        return "admin/wtalk/statistics/hourly/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(TalkStatisticsHourlySearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        new TalkHourlyStatExcel(search, apiInterface.list(search)).generator(response, "상담톡시간별통계");
    }
}
