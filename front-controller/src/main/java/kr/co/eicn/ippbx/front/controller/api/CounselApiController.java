package kr.co.eicn.ippbx.front.controller.api;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.front.interceptor.LoginRequired;
import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.front.service.api.CounselApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.enums.TodoListTodoStatus;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.TodoList;
import kr.co.eicn.ippbx.model.dto.eicn.TalkCurrentListResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TalkCurrentMsgResponse;
import kr.co.eicn.ippbx.model.form.TalkAutoEnableFormRequest;
import kr.co.eicn.ippbx.model.form.TalkCurrentListSearchRequest;
import kr.co.eicn.ippbx.model.form.TodoListUpdateFormRequest;
import kr.co.eicn.ippbx.model.form.TodoReservationFormRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@LoginRequired
@AllArgsConstructor
@RestController
@RequestMapping(value = "api/counsel", produces = MediaType.APPLICATION_JSON_VALUE)
public class CounselApiController extends BaseController {
    private final CounselApiInterface apiInterface;

    //todolist 처리결과 수정
    @PutMapping("to-do")
    public void put(@Valid @RequestBody TodoListUpdateFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.updateTodoStatus(form);
    }

    //상태 done으로 변경
    @PutMapping("to-do/done/{seq}")
    public void toDoDone(@PathVariable int seq) throws IOException, ResultFailException {
        TodoListUpdateFormRequest form = new TodoListUpdateFormRequest();
        form.setSeq(seq);
        form.setTodoStatus(TodoListTodoStatus.DONE);
        apiInterface.updateTodoStatus(form);
    }
    //상태 delete로 변경
    @DeleteMapping("to-do/delete/{seq}")
    public void toDoDelete(@PathVariable int seq) throws IOException, ResultFailException {
        TodoListUpdateFormRequest form = new TodoListUpdateFormRequest();
        form.setSeq(seq);
        form.setTodoStatus(TodoListTodoStatus.DELETE);
        apiInterface.updateTodoStatus(form);
    }

    @PostMapping("consultation-reservation")
    public void reserveConsultation(@Valid @RequestBody TodoReservationFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.reserveConsultation(form);
    }

    @SneakyThrows
    @PostMapping("file/{channelType}")
    public String uploadFile(@PathVariable String channelType, @Valid @RequestBody FileForm form, BindingResult bindingResult) {
        return apiInterface.uploadFile(form, g.getUser().getCompanyId(), channelType);
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

    /**
     * 상담톡 자동멘트 수정
     */
    @PutMapping("talk-auto-enable/{roomId}")
    public void updateTalkAutoEnable(@Valid @RequestBody TalkAutoEnableFormRequest form, BindingResult bindingResult, @PathVariable String roomId) throws IOException, ResultFailException {
        apiInterface.updateTalkAutoEnable(roomId, form);
    }
}
