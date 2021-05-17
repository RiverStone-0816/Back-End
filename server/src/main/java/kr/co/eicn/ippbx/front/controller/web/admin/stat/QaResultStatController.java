package kr.co.eicn.ippbx.front.controller.web.admin.stat;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.stat.QaResultStatApiInterface;
import kr.co.eicn.ippbx.front.service.excel.QaResultIndividualStatExcel;
import kr.co.eicn.ippbx.front.service.excel.QaResultLinkStatExcel;
import kr.co.eicn.ippbx.front.util.FormUtils;
import kr.co.eicn.ippbx.server.model.dto.customdb.*;
import kr.co.eicn.ippbx.server.model.dto.eicn.IndividualCodeResponse;
import kr.co.eicn.ippbx.server.model.search.StatQaResultIndividualSearchRequest;
import kr.co.eicn.ippbx.server.model.search.StatQaResultSearchRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/stat/result")
public class QaResultStatController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(QaResultStatController.class);

    private final QaResultStatApiInterface apiInterface;

    @GetMapping("")
    public String page() {
        return "redirect:/admin/stat/result/link";
    }

    @GetMapping("link")
    public String linkPage(Model model, @ModelAttribute("search") StatQaResultSearchRequest search) throws IOException, ResultFailException {
        final List<StatQaResultResponse> list = apiInterface.list(search);
        model.addAttribute("list", list);

        final Map<String, String> sendReceiveTypes = FormUtils.options(false, StatQaResultSearchRequest.SendReceiveType.class);
        model.addAttribute("sendReceiveTypes", sendReceiveTypes);

        final Set<String> dates = new TreeSet<>(Comparator.comparing(e -> e));
        model.addAttribute("dates", dates);
        for (StatQaResultResponse e : list)
            for (StatQaResultFieldResponse e2 : e.getFieldResponses())
                for (StatQaResultCodeResponse e3 : e2.getCodeResponses())
                    for (QaResultResponse e4 : e3.getQaResultStat())
                        dates.add(e4.getStatDate());

        final Map<String, Map<String, Integer>> codeToDateToCountMap = new HashMap<>();
        model.addAttribute("codeToDateToCountMap", codeToDateToCountMap);
        for (StatQaResultResponse e : list)
            for (StatQaResultFieldResponse e2 : e.getFieldResponses())
                for (StatQaResultCodeResponse e3 : e2.getCodeResponses())
                    codeToDateToCountMap.put(e3.getCodeId(), e3.getQaResultStat().stream().collect(Collectors.toMap(QaResultResponse::getStatDate, QaResultResponse::getCount)));

        return "admin/stat/result/link/ground";
    }

    @GetMapping("link/_excel")
    public void downloadLinkExcel(StatQaResultSearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        final List<StatQaResultResponse> list = apiInterface.list(search);
        new QaResultLinkStatExcel(list).generator(response, "상담결과통계[연계]");
    }

    @GetMapping("individual")
    public String individualPage(Model model, @ModelAttribute("search") StatQaResultIndividualSearchRequest search) throws IOException, ResultFailException {
        final List<StatQaResultIndividualResponse> list = apiInterface.getIndividualResult(search);
        model.addAttribute("list", list);

        final List<IndividualCodeResponse> fieldList = apiInterface.getFieldList();
        model.addAttribute("fieldList", fieldList);

        final Map<String, String> sendReceiveTypes = FormUtils.options(false, StatQaResultSearchRequest.SendReceiveType.class);
        model.addAttribute("sendReceiveTypes", sendReceiveTypes);

        final Set<String> dates = new TreeSet<>(Comparator.comparing(e -> e));
        model.addAttribute("dates", dates);
        for (StatQaResultIndividualResponse e : list)
            for (QaResultResponse e2 : e.getQaResultStat())
                dates.add(e2.getStatDate());

        final Map<StatQaResultIndividualResponse, Map<String, Integer>> codeToDateToCountMap = new HashMap<>();
        model.addAttribute("codeToDateToCountMap", codeToDateToCountMap);
        for (StatQaResultIndividualResponse e : list)
            codeToDateToCountMap.put(e, e.getQaResultStat().stream().collect(Collectors.toMap(QaResultResponse::getStatDate, QaResultResponse::getCount)));

        return "admin/stat/result/individual/ground";
    }

    @GetMapping("individual/_excel")
    public void downloadIndividualExcel(StatQaResultIndividualSearchRequest search, HttpServletResponse response) throws IOException, ResultFailException {
        final List<StatQaResultIndividualResponse> list = apiInterface.getIndividualResult(search);
        new QaResultIndividualStatExcel(list).generator(response, "상담결과통계[개인]");
    }
}
