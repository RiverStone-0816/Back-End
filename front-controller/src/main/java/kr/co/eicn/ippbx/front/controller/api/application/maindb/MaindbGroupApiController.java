package kr.co.eicn.ippbx.front.controller.api.application.maindb;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.maindb.MaindbGroupApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.CommonTypeResponse;
import kr.co.eicn.ippbx.model.dto.eicn.MaindbGroupSummaryResponse;
import kr.co.eicn.ippbx.model.form.MaindbGroupFormRequest;
import kr.co.eicn.ippbx.model.form.MaindbGroupUpdateRequest;
import kr.co.eicn.ippbx.model.search.MaindbGroupSearchRequest;
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
@RequestMapping(value = "api/maindb-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class MaindbGroupApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MaindbGroupApiController.class);

    @Autowired
    private MaindbGroupApiInterface apiInterface;

    @GetMapping("")
    public Pagination<MaindbGroupSummaryResponse> pagination(MaindbGroupSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @PostMapping("")
    public void post(@Valid @RequestBody MaindbGroupFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    @PutMapping("{seq}")
    public void put(@Valid @RequestBody MaindbGroupUpdateRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }

    /**
     * 고객정보유형 목록조회
     */
    @GetMapping("maindb-type")
    public List<CommonTypeResponse> maindbType() throws IOException, ResultFailException {
        return apiInterface.maindbType();
    }

    /**
     * 상담결과유형 목록조회
     */
    @GetMapping("result-type")
    public List<CommonTypeResponse> resultType() throws IOException, ResultFailException {
        return apiInterface.resultType();
    }

    @PostMapping("{seq}/fields/by-excel")
    public void postFieldsByExcel(@PathVariable Integer seq, @RequestBody FileForm form) throws IOException, ResultFailException {
        if(form.getFileName().indexOf(".xls") < 0)
            throw new IllegalArgumentException("파일을 확인하세요.");
        apiInterface.postFieldsByExcel(seq, form);
    }
}
