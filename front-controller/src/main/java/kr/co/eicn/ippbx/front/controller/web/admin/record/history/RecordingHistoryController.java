package kr.co.eicn.ippbx.front.controller.web.admin.record.history;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.search.RecordCallSearchForm;
import kr.co.eicn.ippbx.front.service.OrganizationService;
import kr.co.eicn.ippbx.front.service.api.SearchApiInterface;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbResultApiInterface;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchPersonListResponse;
import kr.co.eicn.ippbx.util.MapToLinkedHashMap;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.acd.grade.GradelistApiInterface;
import kr.co.eicn.ippbx.front.service.api.record.evaluation.EvaluationFormApiInterface;
import kr.co.eicn.ippbx.front.service.api.record.history.RecordingHistoryApiInterface;
import kr.co.eicn.ippbx.front.service.api.sounds.IvrApiInterface;
import kr.co.eicn.ippbx.front.service.api.sounds.schedule.OutboundDayScheduleApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.tel.ServiceApiInterface;
import kr.co.eicn.ippbx.front.service.excel.RecordingHistoryStatExcel;
import kr.co.eicn.ippbx.util.FormUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.EvaluationFormUseType;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.EvaluationForm;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree;
import kr.co.eicn.ippbx.model.RecordFile;
import kr.co.eicn.ippbx.model.dto.customdb.CommonEicnCdrResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryPhoneInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchQueueResponse;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyEntity;
import kr.co.eicn.ippbx.model.enums.AdditionalState;
import kr.co.eicn.ippbx.model.enums.CallStatus;
import kr.co.eicn.ippbx.model.enums.IdType;
import kr.co.eicn.ippbx.model.form.EvaluationResultFormRequest;
import kr.co.eicn.ippbx.model.form.RecordDownFormRequest;
import kr.co.eicn.ippbx.model.search.EvaluationFormSearchRequest;
import kr.co.eicn.ippbx.model.search.RecordCallSearch;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@AllArgsConstructor
@LoginRequired
@Controller
@RequestMapping("admin/record/history/history")
public class RecordingHistoryController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RecordingHistoryController.class);

    private final RecordingHistoryApiInterface apiInterface;
    private final ServiceApiInterface serviceApiInterface;
    private final OrganizationService organizationService;
    private final OutboundDayScheduleApiInterface outboundDayScheduleApiInterface;
    private final GradelistApiInterface gradelistApiInterface;
    private final IvrApiInterface ivrApiInterface;
    private final EvaluationFormApiInterface evaluationFormApiInterface;
    private final CompanyApiInterface companyApiInterface;
    private final SearchApiInterface searchApiInterface;
    private final MaindbResultApiInterface maindbResultApiInterface;

    @GetMapping("")
    public String page(Model model, @ModelAttribute("search") RecordCallSearchForm search) throws IOException, ResultFailException {
        final Pagination<CommonEicnCdrResponse> pagination = apiInterface.pagination(search);
        if (search.getBatchDownloadMode())
            search.setLimit(1000);
        model.addAttribute("pagination", pagination);

        model.addAttribute("searchOrganizationNames", organizationService.getHierarchicalOrganizationNames(search.getGroupCode()));
        model.addAttribute("persons", searchApiInterface.persons());

        model.addAttribute("extensions", outboundDayScheduleApiInterface.addExtensions().stream()
                .filter(e -> e.getExtension() != null && e.getInUseIdName() != null).sorted(Comparator.comparing(SummaryPhoneInfoResponse::getExtension)).collect(Collectors.toList()));
        model.addAttribute("sortTypes", FormUtils.options(false, RecordCallSearch.Sort.class));

        model.addAttribute("callTypes", FormUtils.optionsOfCode(RecordCallSearch.SearchCallType.class));
        model.addAttribute("callStatuses", FormUtils.optionsOfCode(CallStatus.normal_clear, CallStatus.no_answer, CallStatus.user_busy, CallStatus.fail, CallStatus.local_forward));
        model.addAttribute("etcStatuses", FormUtils.optionsOfCode(
                AdditionalState.HANGUP_BEFORE_CONNECT, AdditionalState.CANCEL_CONNECT,
                AdditionalState.PICKUPEE, AdditionalState.PICKUPER,
                AdditionalState.TRANSFERER, AdditionalState.TRANSFEREE,
                AdditionalState.REDIRECTOUT_TRANSFERER, AdditionalState.REDIRECTOUT_TRANSFEREE,
                AdditionalState.SCD_TRANSFERER, AdditionalState.SCD_TRANSFEREE,
                AdditionalState.LOCAL_TRANSFERER, AdditionalState.LOCAL_TRANSFEREE,
                AdditionalState.FORWARD_TRANSFERER, AdditionalState.FORWARD_TRANSFEREE));

        final List<IvrTree> ivrTrees = ivrApiInterface.addIvrTreeList();
        model.addAttribute("ivrCodes", new MapToLinkedHashMap().toLinkedHashMapByValue(ivrTrees.stream().filter(e -> e.getTreeName().split("_").length == 1).collect(Collectors.toMap(IvrTree::getCode, IvrTree::getName))));
        model.addAttribute("ivrKeys", ivrTrees.stream().filter(e -> StringUtils.isNotEmpty(e.getButton())).collect(Collectors.groupingBy(IvrTree::getCode)));
        model.addAttribute("services", serviceApiInterface.addServices());

        model.addAttribute("callTimeTypes", FormUtils.options(false, RecordCallSearch.CallTime.class));
        model.addAttribute("queues", new MapToLinkedHashMap().toLinkedHashMapByValue(gradelistApiInterface.queues().stream().collect(Collectors.toMap(SearchQueueResponse::getNumber, SearchQueueResponse::getHanName))));

        final CompanyEntity company = companyApiInterface.getInfo();
        model.addAttribute("company", company);

        return "admin/record/history/history/ground";
    }

    // FIXME: 기획삭제
    @GetMapping("{uniqueId}/{phoneNumber}/modal-stt")
    public String modalStt(Model model, @PathVariable String uniqueId, @PathVariable String phoneNumber) throws IOException, ResultFailException {
        model.addAttribute("uniqueId", uniqueId);
        model.addAttribute("phoneNumber", phoneNumber);

        final List<RecordFile> files = maindbResultApiInterface.getFiles(uniqueId);
        model.addAttribute("files", files);
        return "admin/record/history/history/modal-record-stt";
    }

    @GetMapping("modal-batch-download")
    public String modalBatchDownload(@ModelAttribute("form") RecordDownFormRequest form) {
        if (form.getSequences() == null || form.getSequences().isEmpty())
            throw new IllegalArgumentException("녹취정보가 존재하지 않습니다.");

        return "admin/record/history/history/modal-batch-download";
    }

    @GetMapping("{seq}/modal-records")
    public String popupRecords(Model model, @PathVariable Integer seq) throws IOException, ResultFailException {
        final List<RecordFile> files = apiInterface.getFiles(seq);
        final CommonEicnCdrResponse list = apiInterface.get(seq);
        model.addAttribute("files", files);
        model.addAttribute("seq", seq);

        model.addAttribute("list", list);

        return "admin/record/history/history/modal-records";
    }

    @GetMapping("modal-batch-evaluation")
    public String modalBatchEvaluation(Model model, @RequestParam List<Integer> seq, @ModelAttribute("form") EvaluationResultFormRequest form) throws IOException, ResultFailException {
        if (seq == null || seq.isEmpty())
            throw new IllegalArgumentException("녹취의 seq 정보는 필수입니다.");

        final RecordCallSearchForm search = new RecordCallSearchForm();
        search.setSeqList(seq);
        search.setBatchEvaluationMode(true);
        final List<CommonEicnCdrResponse> list = apiInterface.list(search);
        model.addAttribute("list", list);

        return "admin/record/history/history/modal-batch-evaluation";
    }

    @GetMapping("{seq}/modal-evaluation")
    public String modalEvaluation(Model model, @PathVariable Integer seq, @ModelAttribute("form") EvaluationResultFormRequest form) {
        model.addAttribute("seq", seq);
        return "admin/record/history/history/modal-evaluation";
    }

    @GetMapping("{seq}/cdr-evaluation-form")
    public String evaluationForm(Model model, @PathVariable Integer seq, @ModelAttribute("form") EvaluationResultFormRequest form) throws IOException, ResultFailException {
        final CommonEicnCdrResponse cdr = apiInterface.get(seq);
        model.addAttribute("cdr", cdr);

        form.setCdrId(seq);
        if (cdr.getPersonList() != null)
            form.setTargetUserid(cdr.getPersonList().getId());

        final List<RecordFile> files = apiInterface.getFiles(seq);
        model.addAttribute("files", files);
        model.addAttribute("seq", seq);

        model.addAttribute("now", new Timestamp(System.currentTimeMillis()));

        final EvaluationFormSearchRequest search = new EvaluationFormSearchRequest();
        search.setStartDate(new Date(System.currentTimeMillis()));
        search.setEndDate(new Date(System.currentTimeMillis()));
        search.setVisible(true);
        search.setUseTypes(Arrays.asList(EvaluationFormUseType.PERIOD, EvaluationFormUseType.IN_PROGRESS));

        final List<EvaluationForm> forms = evaluationFormApiInterface.search(search);
        model.addAttribute("forms", forms);

        List<SearchPersonListResponse> persons = searchApiInterface.persons();
        model.addAttribute("users", persons.stream().filter(e -> e.getIdType().contains(IdType.USER.getCode())).collect(Collectors.toList()));

        return "admin/record/history/history/cdr-evaluation-form";
    }

    @GetMapping("_excel")
    public void downloadExcel(RecordCallSearchForm search, HttpServletResponse response) throws IOException, ResultFailException {
        search.setPage(1);
        search.setLimit(100000);

        new RecordingHistoryStatExcel(apiInterface.pagination(search).getRows()).generator(response, "녹취_통화이력");
    }

    @GetMapping("modal-call-bot/{seq}")
    public String callBot(Model model, @PathVariable Integer seq) throws IOException, ResultFailException {
        CommonEicnCdrResponse response = apiInterface.get(seq);
        model.addAttribute("cdr", response);
        return "admin/record/history/history/modal-call-bot";
    }
}
