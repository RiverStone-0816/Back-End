package kr.co.eicn.ippbx.front.controller.web.admin.talk.statistics;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.talk.group.TalkReceptionGroupApiInterface;
import kr.co.eicn.ippbx.front.service.api.talk.statistics.TalkDailyStatApiInterface;
import kr.co.eicn.ippbx.front.service.excel.TalkDailyStatExcel;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryTalkServiceResponse;
import kr.co.eicn.ippbx.server.model.dto.statdb.TalkStatisticsDailyResponse;
import kr.co.eicn.ippbx.server.model.search.TalkStatisticsSearchRequest;
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
@RequestMapping("admin/talk/statistics/daily")
public class TalkDailyStatController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TalkDailyStatController.class);

    @Autowired
    private TalkDailyStatApiInterface apiInterface;
    @Autowired
    private TalkReceptionGroupApiInterface talkReceptionGroupApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") TalkStatisticsSearchRequest search) throws IOException, ResultFailException {
        final List<TalkStatisticsDailyResponse> list = apiInterface.list(search);
        model.addAttribute("list", list);

        final Map<String, String> talkServices = talkReceptionGroupApiInterface.talkServices().stream().collect(Collectors.toMap(SummaryTalkServiceResponse::getSenderKey, SummaryTalkServiceResponse::getKakaoServiceName));
        model.addAttribute("talkServices", talkServices);

        return "admin/talk/statistics/daily/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(TalkStatisticsSearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        new TalkDailyStatExcel(search, apiInterface.list(search)).generator(response, "상담톡일별통계");
    }
}
