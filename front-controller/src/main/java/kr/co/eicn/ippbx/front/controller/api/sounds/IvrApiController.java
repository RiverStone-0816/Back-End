package kr.co.eicn.ippbx.front.controller.api.sounds;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.sounds.IvrApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.IvrTree;
import kr.co.eicn.ippbx.model.dto.eicn.IvrResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryIvrTreeResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryNumber070Response;
import kr.co.eicn.ippbx.model.form.IvrFormRequest;
import kr.co.eicn.ippbx.model.form.IvrFormUpdateRequest;
import kr.co.eicn.ippbx.model.form.IvrPositionFormRequest;
import kr.co.eicn.ippbx.model.form.WebVoiceItemsFormRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author tinywind
 */
@AllArgsConstructor
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/ivr", produces = MediaType.APPLICATION_JSON_VALUE)
public class IvrApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(IvrApiController.class);
    private final IvrApiInterface apiInterface;

    @GetMapping("")
    public List<IvrResponse> list() throws IOException, ResultFailException {
        return apiInterface.list();
    }

    /**
     * 상세조회
     */
    @GetMapping("{seq}")
    public IvrResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    /**
     * 메뉴 등록
     */
    @PostMapping("")
    public Integer postMenu(@Valid @RequestBody IvrFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.postMenu(form);
    }

    /**
     * IVR TREE 수정
     */
    @PutMapping("{seq}")
    public void put(@Valid @RequestBody IvrFormUpdateRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    @PutMapping("{seq}/position")
    public void updatePosition(@Valid @RequestBody IvrPositionFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.updatePosition(seq, form);
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }

    @PostMapping("{ivrCode}/apply")
    public void apply(@PathVariable Integer ivrCode,
                            @Valid @RequestBody WebVoiceItemsFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.apply(ivrCode, form);
    }

    @GetMapping("root-node")
    public List<SummaryIvrTreeResponse> rootNodes() throws IOException, ResultFailException {
        return apiInterface.rootNodes();
    }

    /**
     * Ivr 목록 조회
     */
    @GetMapping("add-ivr-list")
    public List<IvrTree> addIvrTreeList() throws IOException, ResultFailException {
        return apiInterface.addIvrTreeList();
    }

    /**
     * 번호 목록 조회
     */
    @GetMapping("add-number-list")
    public List<SummaryNumber070Response> addNumber070List() throws IOException, ResultFailException {
        return apiInterface.addNumber070List();
    }
}
