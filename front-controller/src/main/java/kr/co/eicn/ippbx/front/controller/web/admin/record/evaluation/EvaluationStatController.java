package kr.co.eicn.ippbx.front.controller.web.admin.record.evaluation;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.record.evaluation.EvaluationFormApiInterface;
import kr.co.eicn.ippbx.front.service.api.record.evaluation.EvaluationResultApiInterface;
import kr.co.eicn.ippbx.front.service.excel.EvaluationStatExcel;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.EvaluationResultProcessStatus;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationForm;
import kr.co.eicn.ippbx.model.entity.eicn.EvaluationResultStatResponse;
import kr.co.eicn.ippbx.model.search.EvaluationFormSearchRequest;
import kr.co.eicn.ippbx.model.search.EvaluationResultSearchRequest;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.ResultFailException;
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
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/record/evaluation/stat")
public class EvaluationStatController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(EvaluationStatController.class);

    private final EvaluationResultApiInterface apiInterface;
    private final EvaluationFormApiInterface evaluationFormApiInterface;
    private final SearchApiInterface searchApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") EvaluationResultSearchRequest search) throws IOException, ResultFailException {
        search.setStatus(EvaluationResultProcessStatus.COMPLETE);
        final List<EvaluationResultStatResponse> list = apiInterface.statistics(search);
        model.addAttribute("list", list);

        model.addAttribute("forms", evaluationFormApiInterface.search(new EvaluationFormSearchRequest()).stream().collect(Collectors.toMap(EvaluationForm::getId, EvaluationForm::getName)));

        model.addAttribute("persons", searchApiInterface.persons());

        final Map<String, String> statuses = FormUtils.options(EvaluationResultProcessStatus.class);
        model.addAttribute("statuses", statuses);

        return "admin/record/evaluation/stat/ground";
    }

    @GetMapping("_excel")
    public void downloadExcel(EvaluationResultSearchRequest search, HttpServletResponse response) throws IOException, ResultFailException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        search.setPage(1);
        search.setLimit(10000);
        final List<EvaluationResultStatResponse> list = apiInterface.statistics(search);

        new EvaluationStatExcel(list).generator(response, "고객정보");
    }
}
