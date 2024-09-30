package kr.co.eicn.ippbx.front.controller.api.record.history;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.api.CallBotApiInterface;
import kr.co.eicn.ippbx.model.CallBotRequest;
import kr.co.eicn.ippbx.model.CallBotResponse;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.record.history.RecordingHistoryApiInterface;
import kr.co.eicn.ippbx.model.RecordFile;
import kr.co.eicn.ippbx.model.dto.customdb.CommonEicnCdrResponse;
import kr.co.eicn.ippbx.model.form.RecordDownFormRequest;
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
@RequestMapping(value = "api/record-history", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecordingHistoryApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(RecordingHistoryApiController.class);

    private final RecordingHistoryApiInterface apiInterface;
    private final CallBotApiInterface          callBotApiInterface;

    @GetMapping("{seq}")
    public CommonEicnCdrResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    /**
     * 녹취파일 목록 조회
     */
    @GetMapping("{seq}/record-files")
    public List<RecordFile> getFiles(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.getFiles(seq);
    }

    /**
     * 녹취 일괄다운로드 등록
     */
    @PostMapping(value = "record-batch-download-register")
    public String recordInBatchesRegister(@RequestBody @Valid RecordDownFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.recordInBatchesRegister(form);
    }

    @DeleteMapping("{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }

    @GetMapping("call-bot/{callUuid}/{createdDt}/{tenantId}/{serviceCode}/{phoneNumber}")
    public CallBotResponse callBot(@PathVariable String callUuid, @PathVariable String createdDt, @PathVariable String tenantId, @PathVariable String serviceCode, @PathVariable String phoneNumber) throws IOException {
        final CallBotRequest request = new CallBotRequest(callUuid, createdDt, tenantId, phoneNumber);
        if (g.getLguCallBotApiUrl() == null)
            throw new IllegalArgumentException("콜봇 URL 이 등록되지 않았습니다.\nmaster 에서 등록해주세요.");

        return callBotApiInterface.getCallbot(request, g.getLguCallBotApiUrl().get(tenantId + "_" + serviceCode));
    }
}
