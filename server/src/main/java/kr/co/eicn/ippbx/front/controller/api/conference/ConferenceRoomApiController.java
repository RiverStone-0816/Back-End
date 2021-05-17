package kr.co.eicn.ippbx.front.controller.api.conference;

import io.swagger.annotations.Api;
import kr.co.eicn.ippbx.front.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.conference.ConferenceRoomApiInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.ConfRoom;
import kr.co.eicn.ippbx.server.model.dto.eicn.ConfRoomDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.ConfRoomSummaryResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.SummaryNumber070Response;
import kr.co.eicn.ippbx.server.model.enums.NumberType;
import kr.co.eicn.ippbx.server.model.form.ConfRoomFormRequest;
import kr.co.eicn.ippbx.server.model.search.ConfRoomSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tinywind
 */
@Api(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@RestController
@RequestMapping(value = "api/conference-room", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConferenceRoomApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(ConferenceRoomApiController.class);

    @Autowired
    private ConferenceRoomApiInterface apiInterface;

    /**
     * 회의실관리 목록조회
     */
    @GetMapping("")
    public Pagination<ConfRoomSummaryResponse> pagination(ConfRoomSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.pagination(search);
    }

    /**
     * 회의실관리 상세조회
     */
    @GetMapping(value = "{seq}")
    public ConfRoomDetailResponse get(@PathVariable Integer seq) throws IOException, ResultFailException {
        return apiInterface.get(seq);
    }

    /**
     * 회의실관리 추가
     */
    @PostMapping
    public void post(@Valid @RequestBody ConfRoomFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.post(form);
    }

    /**
     * 회의실관리 수정
     */
    @PutMapping(value = "{seq}")
    public void put(@Valid @RequestBody ConfRoomFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.put(seq, form);
    }

    /**
     * 회의실관리 삭제
     */
    @DeleteMapping(value = "{seq}")
    public void delete(@PathVariable Integer seq) throws IOException, ResultFailException {
        apiInterface.delete(seq);
    }

    /**
     * 회의실번호 조회(사용중이지 않은 회의실 번호)
     */
    @GetMapping("unused-confroom-number")
    public List<SummaryNumber070Response> getConfRoomNumber() throws IOException, ResultFailException {
        return apiInterface.getConfRoomNumber();
    }
}
