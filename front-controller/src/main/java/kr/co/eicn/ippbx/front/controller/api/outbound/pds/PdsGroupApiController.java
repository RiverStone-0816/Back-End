package kr.co.eicn.ippbx.front.controller.api.outbound.pds;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsGroupApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.form.PDSExecuteFormRequest;
import kr.co.eicn.ippbx.model.form.PDSGroupFormRequest;
import kr.co.eicn.ippbx.model.search.PDSGroupSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/pds-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class PdsGroupApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PdsGroupApiController.class);

    @Autowired
    private PdsGroupApiInterface apiInterface;

    @GetMapping("")
    public Pagination<PDSGroupSummaryResponse> pagination(PDSGroupSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @GetMapping("{seq}")
    public PDSGroupDetailResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    @PostMapping("")
    public void post(@Valid @RequestBody PDSGroupFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    @PutMapping("{seq}")
    public void put(@Valid @RequestBody PDSGroupFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }

    /**
     * 실행요청
     */
    @PostMapping("{seq}/execute")
    public void executeRequest(@Valid @RequestBody PDSExecuteFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.executeRequest(seq, form);
    }

    /**
     * 유형 목록 조회
     */
    @GetMapping(value = "add-common-type", params = "kind")
    public List<SummaryCommonTypeResponse> addCommonTypeLists(@RequestParam String kind) throws IOException, ResultFailException {
        return apiInterface.addCommonTypeLists(kind);
    }

    /**
     * 실행할교환기 목록 조회
     */
    @GetMapping("add-server")
    public List<SummaryCompanyServerResponse> addServerLists() throws IOException, ResultFailException {
        return apiInterface.addServerLists();
    }

    /**
     * 과금번호설정 목록 조회
     */
    @GetMapping("add-numbers")
    public List<SummaryNumber070Response> addNumberLists() throws IOException, ResultFailException {
        return apiInterface.addNumberLists();
    }

    /**
     * 상담원그룹 연결대상 목록 조회(실행할교환기선택후사용가능)
     */
    @GetMapping("add-pds-queue")
    public List<SummaryPDSQueueNameResponse> addPDSQueueNameLists() throws IOException, ResultFailException {
        return apiInterface.addPDSQueueNameLists();
    }

    /**
     * PDS IVR 목록 조회
     */
    @GetMapping("add-pds-ivr")
    public List<SummaryIvrTreeResponse> addPDSIvrLists() throws IOException, ResultFailException {
        return apiInterface.addPDSIvrLists();
    }

    /**
     * 상담결과유형 목록조회
     */
    @GetMapping("add-consultation-result")
    public List<SummaryCommonTypeResponse> addConsultationResult() throws IOException, ResultFailException {
        return apiInterface.addConsultationResult();
    }

    /**
     * 설문 목록 조회
     */
    @GetMapping("add-research")
    public List<SummaryResearchListResponse> addResearchLists() throws IOException, ResultFailException {
        return apiInterface.addResearchLists();
    }

    /**
     * 유형필드 목록 조회
     */
    @GetMapping("add-field")
    public List<SummaryCommonFieldResponse> addCommonFieldLists() throws IOException, ResultFailException {
        return apiInterface.addCommonFieldLists();
    }

    @PostMapping("{seq}/fields/by-excel")
    public void postFieldsByExcel(@PathVariable Integer seq, @RequestBody FileForm form) throws IOException, ResultFailException {
        if(form.getFileName().indexOf(".xls") < 0)
            throw new IllegalArgumentException("파일을 확인하세요.");
        apiInterface.postFieldsByExcel(seq, form);
    }
}
