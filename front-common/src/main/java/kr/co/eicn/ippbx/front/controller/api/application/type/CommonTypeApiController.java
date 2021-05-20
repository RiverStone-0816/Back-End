package kr.co.eicn.ippbx.front.controller.api.application.type;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.application.type.CommonTypeApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonBasicField;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.form.CommonTypeFormRequest;
import kr.co.eicn.ippbx.model.form.CommonTypeUpdateFormRequest;
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
@RequestMapping(value = "api/common-type", produces = MediaType.APPLICATION_JSON_VALUE)
public class CommonTypeApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CommonTypeApiController.class);

    @Autowired
    private CommonTypeApiInterface apiInterface;

    /**
     * 유형관리 목록조회
     */
    @GetMapping(value = "", params = "kind")
    public List<CommonTypeEntity> list(@RequestParam String kind) throws IOException, ResultFailException {
        return apiInterface.list(kind);
    }

    /**
     * 유형 상세조회
     */
    @GetMapping("{seq}")
    public CommonTypeEntity get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    /**
     * 유형 추가
     */
    @PostMapping("")
    public void post(@Valid @RequestBody CommonTypeFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    /**
     * 유형 수정
     */
    @PutMapping("{seq}")
    public void put(@Valid @RequestBody CommonTypeUpdateFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    /**
     * 유형 삭제
     */
    @PatchMapping("{seq}")
    public void updateStatus(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.updateStatus(seq);
    }

    /**
     * 필드 순서 변경
     * <b>History:</b>
     * tinywind: PUT -> GET: payload로 정보를 전달하지 않는다면, PUT 쓰지 말자...
     */
    @GetMapping("{seq}/move")
    public void move(@RequestParam List<Integer> sequences, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.move(seq, sequences);
    }

    /**
     * 유형 기본 필드 목록조회
     */
    @GetMapping(value = "/basic-field")
    public List<CommonBasicField> getBasicFields(@RequestParam String kind) throws IOException, ResultFailException {
        return apiInterface.getBasicFields(kind);
    }
}
