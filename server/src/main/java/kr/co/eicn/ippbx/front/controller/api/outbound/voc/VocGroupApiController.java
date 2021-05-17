package kr.co.eicn.ippbx.front.controller.api.outbound.voc;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.outbound.voc.VocGroupApiInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.VocGroup;
import kr.co.eicn.ippbx.server.model.dto.eicn.VOCGroupResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.VocArsSmsResponse;
import kr.co.eicn.ippbx.server.model.form.VOCGroupFormRequest;
import kr.co.eicn.ippbx.server.model.search.VOCGroupSearchRequest;
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
@RequestMapping(value = "api/voc-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class VocGroupApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(VocGroupApiController.class);

    @Autowired
    private VocGroupApiInterface apiInterface;

    @GetMapping("search")
    public Pagination<VOCGroupResponse> pagination(VOCGroupSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    @GetMapping("{seq}")
    public VOCGroupResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    @PostMapping("")
    public void post(@Valid @RequestBody VOCGroupFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    @PutMapping("{seq}")
    public void put(@Valid @RequestBody VOCGroupFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }

    @GetMapping("")
    public List<VocGroup> list(VOCGroupSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.list(search);
    }

    /**
     * 상담화면 ARS, SMS
     */
    @GetMapping("{type}/ars-sms-list")
    public List<VocGroup> getArsSmsList(@PathVariable String type) throws IOException, ResultFailException {
        return apiInterface.getArsSmsList(type);
    }
}
