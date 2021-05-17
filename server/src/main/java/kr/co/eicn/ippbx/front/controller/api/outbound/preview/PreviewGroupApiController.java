package kr.co.eicn.ippbx.front.controller.api.outbound.preview;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.front.model.form.PrvGroupUploadForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.preview.PreviewGroupApiInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.CommonTypeResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.PrvGroupNumber070Response;
import kr.co.eicn.ippbx.server.model.dto.eicn.PrvGroupSummaryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.search.SearchPersonListResponse;
import kr.co.eicn.ippbx.server.model.form.PrvGroupFormRequest;
import kr.co.eicn.ippbx.server.model.search.PrvGroupSearchRequest;
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
@RequestMapping(value = "api/preview-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class PreviewGroupApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(PreviewGroupApiController.class);

    @Autowired
    private PreviewGroupApiInterface apiInterface;

    @GetMapping("")
    public Pagination<PrvGroupSummaryResponse> pagination(PrvGroupSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @PostMapping(value = "")
    public Integer post(@Valid @RequestBody PrvGroupFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PutMapping(value = "{seq}")
    public void put(@Valid @RequestBody PrvGroupFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    @DeleteMapping(value = "{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }

    /**
     * 업로드유형 목록조회
     */
    @GetMapping("preview-type")
    public List<CommonTypeResponse> prvType() throws IOException, ResultFailException {
        return apiInterface.prvType();
    }

    /**
     * 상담결과유형 목록조회
     */
    @GetMapping("result-type")
    public List<CommonTypeResponse> resultType() throws IOException, ResultFailException {
        return apiInterface.resultType();
    }

    /**
     * 프리뷰그룹 번호선택 목록
     */
    @GetMapping("preview-number")
    public List<PrvGroupNumber070Response> prvNumber() throws IOException, ResultFailException {
        return apiInterface.prvNumber();
    }

    /**
     * 상담원 목록
     */
    @GetMapping("person")
    public List<SearchPersonListResponse> person() throws IOException, ResultFailException {
        return apiInterface.persons();
    }

    @PostMapping("{seq}/fields/by-excel")
    public void postFieldsByExcel(@PathVariable Integer seq, @RequestBody FileForm form) throws IOException, ResultFailException {
        if(form.getFileName().indexOf(".xls") < 0)
            throw new IllegalArgumentException("파일을 확인하세요.");
        apiInterface.postFieldsByExcel(seq, form);
    }
}
