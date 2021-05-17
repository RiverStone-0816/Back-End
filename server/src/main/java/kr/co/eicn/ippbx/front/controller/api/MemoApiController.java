package kr.co.eicn.ippbx.front.controller.api;

import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.front.service.api.MemoApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.user.UserApiInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.model.entity.customdb.MemoMsgEntity;
import kr.co.eicn.ippbx.server.model.form.MemoMsgFormRequest;
import kr.co.eicn.ippbx.server.model.search.MemoMsgSearchRequest;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author tinywind
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "api/memo", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemoApiController extends ApiBaseController {
    private static final Logger logger = LoggerFactory.getLogger(MemoApiController.class);

    private final MemoApiInterface apiInterface;
    private final UserApiInterface userApiInterface;

    /**
     * 보낸 쪽지
     */
    @SneakyThrows
    @GetMapping("send")
    public Pagination<MemoMsgEntity> getSendMemoList(MemoMsgSearchRequest search) {
        return apiInterface.getSendMemoList(search);
    }

    /**
     * 받은 쪽지
     */
    @SneakyThrows
    @GetMapping("receive")
    public Pagination<MemoMsgEntity> getReceiveMemoList(MemoMsgSearchRequest search) {
        return apiInterface.getReceiveMemoList(search);
    }

    @SneakyThrows
    @GetMapping("unread-message")
    public Integer getUnreadMessageCount() {
        return apiInterface.getUnreadMessageCount();
    }

    /**
     * 상세 보기
     */
    @SneakyThrows
    @GetMapping("{seq}")
    public MemoMsgEntity getMemoMessage(@PathVariable Integer seq) {
        return apiInterface.getMemoMessage(seq);
    }

    /**
     * 쪽지 보내기
     */
    @SneakyThrows
    @PostMapping("")
    public String sendMemoMessage(@Valid @RequestBody MemoMsgFormRequest form, BindingResult bindingResult) {
        return apiInterface.sendMemoMessage(form);
    }

    /**
     * 쪽지 답장
     */
    @SneakyThrows
    @PostMapping("{seq}")
    public String receiveMemoMessage(@PathVariable Integer seq, @Valid @RequestBody MemoMsgFormRequest form, BindingResult bindingResult) {
        return apiInterface.receiveMemoMessage(seq, form);
    }

    /**
     * 쪽지 삭제
     */
    @SneakyThrows
    @DeleteMapping("")
    public void deleteMemoMessages(@RequestParam List<Integer> memoSequences) {
        apiInterface.deleteMemoMessages(memoSequences);
    }

    /**
     * 프로필 수정
     */
    @SneakyThrows
    @PutMapping("profile")
    public void updateProfile(@RequestBody FileForm form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        apiInterface.updateProfile(form);
        updateProfilePhoto();
    }

    /**
     * 프로필 파일 삭제
     */
    @SneakyThrows
    @DeleteMapping("delete-specific-file")
    public void deleteSpecificFile() {
        apiInterface.deleteSpecificFile();
        updateProfilePhoto();
    }

    @SneakyThrows
    private void updateProfilePhoto() {
        val storedUser = g.getUser();
        val user = userApiInterface.get(storedUser.getId());

        storedUser.setProfilePhoto(user.getProfilePhoto());
        g.setCurrentUser(storedUser);
    }
}
