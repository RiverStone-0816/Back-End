package kr.co.eicn.ippbx.front.controller.api.record.file;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.record.file.RecordFileEncApiInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.RecordEnc;
import kr.co.eicn.ippbx.server.model.dto.eicn.RecordEncKeyResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.RecordEncKeySummaryResponse;
import kr.co.eicn.ippbx.server.model.form.RecordEncFormRequest;
import kr.co.eicn.ippbx.server.model.form.RecordEncKeyFormRequest;
import kr.co.eicn.ippbx.server.model.search.RecordEncSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * @author tinywind
 */
@LoginRequired
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/record-file-enc", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecordFileEncApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(RecordFileEncApiController.class);

    @Autowired
    private RecordFileEncApiInterface apiInterface;

    /**
     * 녹취암호관리 목록조회
     */
    @GetMapping("key")
    public Pagination<RecordEncKeySummaryResponse> pagination(RecordEncSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    /**
     * 녹취파일 암호화방식 조회
     * -- 고객사의 암호화방식 정보 조회
     */
    @GetMapping("")
    public RecordEnc getCurrentEncryptionType() throws IOException, ResultFailException {
        return apiInterface.getCurrentEncryptionType();
    }

    /**
     * 암호키 조회
     */
    @GetMapping("key/{id}")
    public RecordEncKeyResponse get(@PathVariable Integer id) throws IOException, ResultFailException {
        return apiInterface.get(id);
    }

    /**
     * 녹취파일 암호화 추가
     */
    @PutMapping("")
    public void updateCurrentEncryptionType(@Valid @RequestBody RecordEncFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.updateCurrentEncryptionType(form);
    }

    /**
     * 암호키 추가
     */
    @PostMapping("key")
    public Integer keyRegister(@Valid @RequestBody RecordEncKeyFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        return apiInterface.registerKey(form);
    }

    /**
     * 암호키 수정
     */
    @PutMapping("key/{id}")
    public void update(@Valid @RequestBody RecordEncKeyFormRequest form, BindingResult bindingResult, @PathVariable Integer id) throws IOException, ResultFailException {
        apiInterface.updateKey(id, form);
    }

    /**
     * 암호키 삭제
     */
    @DeleteMapping("key/{id}")
    public void delete(@PathVariable Integer id) throws IOException, ResultFailException {
        apiInterface.deleteByKey(id);
    }
}
