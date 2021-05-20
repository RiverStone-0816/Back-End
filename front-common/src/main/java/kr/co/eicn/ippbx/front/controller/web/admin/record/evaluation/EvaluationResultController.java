package kr.co.eicn.ippbx.front.controller.web.admin.record.evaluation;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.record.evaluation.EvaluationFormApiInterface;
import kr.co.eicn.ippbx.front.service.api.record.evaluation.EvaluationResultApiInterface;
import kr.co.eicn.ippbx.front.service.api.record.history.RecordingHistoryApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.user.UserApiInterface;
import kr.co.eicn.ippbx.front.service.excel.EvaluationResultExcel;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.EvaluationFormUseType;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.EvaluationResultProcessStatus;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationForm;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationItemScore;
import kr.co.eicn.ippbx.model.RecordFile;
import kr.co.eicn.ippbx.model.dto.customdb.CommonEicnCdrResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PersonSummaryResponse;
import kr.co.eicn.ippbx.model.entity.eicn.EvaluationResultEntity;
import kr.co.eicn.ippbx.model.form.EvaluationResultFormRequest;
import kr.co.eicn.ippbx.model.search.EvaluationFormSearchRequest;
import kr.co.eicn.ippbx.model.search.EvaluationResultSearchRequest;
import kr.co.eicn.ippbx.model.search.PersonSearchRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/record/evaluation/result")
public class EvaluationResultController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(EvaluationResultController.class);

    private final EvaluationResultApiInterface apiInterface;
    private final EvaluationFormApiInterface evaluationFormApiInterface;
    private final UserApiInterface userApiInterface;
    private final RecordingHistoryApiInterface recordingHistoryApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") EvaluationResultSearchRequest search) throws IOException, ResultFailException {
        final Pagination<EvaluationResultEntity> pagination = apiInterface.pagination(search);
        model.addAttribute("pagination", pagination);

        model.addAttribute("forms", evaluationFormApiInterface.search(new EvaluationFormSearchRequest()).stream().collect(Collectors.toMap(EvaluationForm::getId, EvaluationForm::getName)));

        final PersonSearchRequest personSearchRequest = new PersonSearchRequest();
        personSearchRequest.setLimit(1000);
        model.addAttribute("persons", userApiInterface.pagination(personSearchRequest).getRows().stream().collect(Collectors.toMap(PersonSummaryResponse::getId, PersonSummaryResponse::getIdName)));

        final Map<String, String> statuses = FormUtils.options(EvaluationResultProcessStatus.class);
        model.addAttribute("statuses", statuses);

        return "admin/record/evaluation/result/ground";
    }

    @GetMapping("{id}/modal")
    public String modal(Model model, @PathVariable Integer id) throws IOException, ResultFailException {
        final EvaluationResultEntity entity = apiInterface.get(id);
        model.addAttribute("entity", entity);

        final Map<Long, Byte> itemToScore = entity.getScores().stream().collect(Collectors.toMap(EvaluationItemScore::getItemId, EvaluationItemScore::getScore));
        model.addAttribute("itemToScore", itemToScore);

        return "admin/record/evaluation/result/modal";
    }

    @GetMapping("{id}/modal-evaluation")
    public String modalEvaluation(Model model, @PathVariable Integer id, @ModelAttribute("form") EvaluationResultFormRequest form) throws IOException, ResultFailException {
        model.addAttribute("id", id);
        final EvaluationResultEntity entity = apiInterface.get(id);
        model.addAttribute("entity", entity);

        return "admin/record/evaluation/result/modal-evaluation";
    }

    @GetMapping("{id}/cdr-evaluation-form")
    public String evaluationForm(Model model, @PathVariable Integer id, @ModelAttribute("form") EvaluationResultFormRequest form) throws IOException, ResultFailException {
        final EvaluationResultEntity entity = apiInterface.get(id);
        model.addAttribute("entity", entity);

        final CommonEicnCdrResponse cdr = recordingHistoryApiInterface.get(entity.getCdr().getSeq());
        model.addAttribute("cdr", cdr);

        form.setCdrId(entity.getCdr().getSeq());
        if (cdr.getPersonList() != null)
            form.setTargetUserid(cdr.getPersonList().getId());

        final List<RecordFile> files = recordingHistoryApiInterface.getFiles(entity.getCdr().getSeq());
        model.addAttribute("files", files);
        model.addAttribute("id", id);

        model.addAttribute("now", new Timestamp(System.currentTimeMillis()));

        final EvaluationFormSearchRequest search = new EvaluationFormSearchRequest();
/*
        search.setStartDate(new Date(System.currentTimeMillis()));
        search.setEndDate(new Date(System.currentTimeMillis()));
*/
        search.setVisible(true);
        search.setUseTypes(Arrays.asList(EvaluationFormUseType.PERIOD, EvaluationFormUseType.IN_PROGRESS));

        final List<EvaluationForm> forms = evaluationFormApiInterface.search(search);
        model.addAttribute("forms", forms);

        final PersonSearchRequest personSearchRequest = new PersonSearchRequest();
        personSearchRequest.setLimit(1000);
        model.addAttribute("persons", userApiInterface.pagination(personSearchRequest).getRows().stream().collect(Collectors.toMap(PersonSummaryResponse::getId, PersonSummaryResponse::getIdName)));

        return "admin/record/evaluation/result/cdr-evaluation-form";
    }

    @GetMapping("_excel")
    public void downloadExcel(EvaluationResultSearchRequest search, HttpServletResponse response) throws IOException, ResultFailException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        search.setPage(1);
        search.setLimit(10000);
        final List<EvaluationResultEntity> rows = apiInterface.pagination(search).getRows();

        new EvaluationResultExcel(rows).generator(response, "고객정보");
    }
}
