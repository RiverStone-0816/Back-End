package kr.co.eicn.ippbx.front.controller.api.outbound.pds;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsResultGroupApiInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.form.PDSResultGroupFormRequest;
import kr.co.eicn.ippbx.model.form.PDSResultGroupUpdateFormRequest;
import kr.co.eicn.ippbx.model.search.PDSResultGroupSearchRequest;
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
@RequestMapping(value = "api/pds-result-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class PdsResultGroupApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(PdsResultGroupApiController.class);

    @Autowired
    private PdsResultGroupApiInterface apiInterface;

    /**
     * 상담그룹설정 목록조회
     */
    @GetMapping("")
    public Pagination<PDSResultGroupSummaryResponse> pagination(PDSResultGroupSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @GetMapping("{name}")
    public PDSResultGroupDetailResponse get(@PathVariable String name) throws IOException, ResultFailException {
        return apiInterface.get(name);
    }

    @PostMapping("")
    public String post(@Valid @RequestBody PDSResultGroupFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.post(form);
    }

    @PutMapping("{name}")
    public void update(@Valid @RequestBody PDSResultGroupUpdateFormRequest form, BindingResult bindingResult, @PathVariable String name) throws IOException, ResultFailException {
        apiInterface.update(name, form);
    }

    @DeleteMapping("{name}")
    public void delete(@PathVariable String name) throws IOException, ResultFailException {
        apiInterface.delete(name);
    }

    /**
     * 실행할교환기 목록 조회 //PDSGroupApiController참조
     */
    @GetMapping("add-server")
    public List<SummaryCompanyServerResponse> addServerLists() throws IOException, ResultFailException {
        return apiInterface.addServerLists();
    }

    /**
     * 컨텍스트 목록 조회//QueueApiController 참조
     */
    @GetMapping("context")
    public List<SummaryContextInfoResponse> context() throws IOException, ResultFailException {
        return apiInterface.context();
    }

    /**
     * 추가 가능한 사용자 목록조회 //QueueApiController 참조
     */
    @GetMapping("add-on-persons")
    public List<SummaryPersonResponse> addOnPersons(@RequestParam(required = false) String name) throws IOException, ResultFailException {
        return apiInterface.addOnPersons(name);
    }
}
