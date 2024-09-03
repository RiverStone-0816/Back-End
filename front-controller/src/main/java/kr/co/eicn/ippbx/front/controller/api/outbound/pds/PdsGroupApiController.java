package kr.co.eicn.ippbx.front.controller.api.outbound.pds;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.front.service.api.outbound.pds.PdsGroupApiInterface;
import kr.co.eicn.ippbx.model.form.PDSExecuteFormRequest;
import kr.co.eicn.ippbx.model.form.PDSGroupFormRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author tinywind
 */
@Slf4j
@AllArgsConstructor
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/pds-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class PdsGroupApiController extends BaseController {

    private final PdsGroupApiInterface apiInterface;

    /**
     * PDS 그룹 추가
     */
    @PostMapping("")
    public void post(@Valid @RequestBody PDSGroupFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    /**
     * PDS 그룹 수정
     */
    @PutMapping("{seq}")
    public void put(@Valid @RequestBody PDSGroupFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    /**
     * PDS 그룹 삭제
     */
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
     * 데이터 업로드
     */
    @PostMapping("{seq}/fields/by-excel")
    public void postFieldsByExcel(@PathVariable Integer seq, @RequestBody FileForm form) throws IOException, ResultFailException {
        if (form.getFileName().indexOf(".xls") < 0)
            throw new IllegalArgumentException("파일을 확인하세요.");

        apiInterface.postFieldsByExcel(seq, form);
    }
}
