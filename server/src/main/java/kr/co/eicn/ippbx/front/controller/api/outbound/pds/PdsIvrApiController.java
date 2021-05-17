package kr.co.eicn.ippbx.front.controller.api.outbound.pds;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsIvrApiInterface;
import kr.co.eicn.ippbx.server.model.dto.eicn.*;
import kr.co.eicn.ippbx.server.model.form.PDSIvrFormRequest;
import kr.co.eicn.ippbx.server.model.form.PDSIvrFormUpdateRequest;
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
@RequestMapping(value = "api/pds-ivr", produces = MediaType.APPLICATION_JSON_VALUE)
public class PdsIvrApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(PdsIvrApiController.class);

    @Autowired
    private PdsIvrApiInterface apiInterface;

    @GetMapping("")
    public List<PDSIvrResponse> list() throws IOException, ResultFailException {
        return apiInterface.list();
    }

    @GetMapping("{seq}")
    public PDSIvrDetailResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    /**
     * 루트IVR추가
     */
    @PostMapping("")
    public void post(@Valid @RequestBody PDSIvrFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    /**
     * 버튼맴핑
     */
    @PutMapping("{code}")
    public void put(@Valid @RequestBody PDSIvrFormUpdateRequest form, BindingResult bindingResult, @PathVariable Integer code) throws IOException, ResultFailException {
        apiInterface.put(code, form);
    }

    /**
     * 삭제
     */
    @DeleteMapping("{code}")
    public void delete(@PathVariable Integer code) throws IOException, ResultFailException {
        apiInterface.delete(code);
    }

    @GetMapping("root-node")
    public List<SummaryIvrTreeResponse> rootNodes() throws IOException, ResultFailException {
        return apiInterface.rootNodes();
    }

    /**
     * pds_queue_name 목록 조회
     */
    @GetMapping("add-pds-queue")
    public List<SummaryPDSQueueNameResponse> addPdsQueueNames() throws IOException, ResultFailException {
        return apiInterface.addPdsQueueNames();
    }
}
