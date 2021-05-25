package kr.co.eicn.ippbx.front.controller.api;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.CounselApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TodoList;
import kr.co.eicn.ippbx.model.dto.eicn.TalkCurrentListResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkCurrentMsgResponse;
import kr.co.eicn.ippbx.model.form.TalkCurrentListSearchRequest;
import kr.co.eicn.ippbx.model.form.TodoListUpdateFormRequest;
import kr.co.eicn.ippbx.model.form.TodoReservationFormRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@LoginRequired
@AllArgsConstructor
@RestController
@RequestMapping(value = "api/counsel", produces = MediaType.APPLICATION_JSON_VALUE)
public class CounselApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(CounselApiController.class);

    private final CounselApiInterface apiInterface;

    //todolist 처리결과 수정
    @PutMapping("to-do")
    public void put(@Valid @RequestBody TodoListUpdateFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.updateTodoStatus(form);
    }

    @PostMapping("consultation-reservation")
    public void reserveConsultation(@Valid @RequestBody TodoReservationFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.reserveConsultation(form);
    }

    @GetMapping("recent-consultation-reservation")
    public List<TodoList> getRecentReservations() throws IOException, ResultFailException {
        return apiInterface.getRecentReservations();
    }

    /**
     * 상담원 상담톡 리스트
     */
    @GetMapping("current-talk-list")
    public List<TalkCurrentListResponse> currentTalkList(TalkCurrentListSearchRequest search) throws IOException, ResultFailException {
        return apiInterface.currentTalkList(search);
    }

    /**
     * 상담원 상담톡 내용
     */
    @GetMapping("current-talk-msg/{roomId}")
    public TalkCurrentMsgResponse currentTalkMsg(@PathVariable String roomId) throws IOException, ResultFailException {
        return apiInterface.currentTalkMsg(roomId);
    }

    /**
     * 상담톡 내리기
     */
    @DeleteMapping("talk-remove-room/{roomId}")
    public void talkRemoveRoom(@PathVariable String roomId) throws IOException, ResultFailException {
        apiInterface.talkRemoveRoom(roomId);
    }
}
