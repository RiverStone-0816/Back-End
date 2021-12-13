package kr.co.eicn.ippbx.server.controller.api.v1.messenger;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.model.entity.customdb.MemoMsgEntity;
import kr.co.eicn.ippbx.model.form.MemoMsgFormRequest;
import kr.co.eicn.ippbx.model.search.MemoMsgSearchRequest;
import kr.co.eicn.ippbx.server.service.FileSystemStorageService;
import kr.co.eicn.ippbx.server.service.MemoMsgService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.io.FilenameUtils.getName;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/memo", produces = MediaType.APPLICATION_JSON_VALUE)
public class MemoMsgController extends ApiBaseController {

    private final MemoMsgService memoMsgService;
    private final FileSystemStorageService fileSystemStorageService;

    /**
     * 보낸 쪽지
     */
    @GetMapping("send")
    public ResponseEntity<JsonResult<Pagination<MemoMsgEntity>>> getSendMemoList(MemoMsgSearchRequest search) {
        final Pagination<MemoMsgEntity> pagination = memoMsgService.getSendMemoList(search);
        return ResponseEntity.ok(data(new Pagination<>(memoMsgService.convertToMemoUserName(pagination.getRows()), pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    /**
     * 받은 쪽지
     */
    @GetMapping("receive")
    public ResponseEntity<JsonResult<Pagination<MemoMsgEntity>>> getReceiveMemoList(MemoMsgSearchRequest search) {
        final Pagination<MemoMsgEntity> pagination = memoMsgService.getReceiveMemoList(search);
        return ResponseEntity.ok(data(new Pagination<>(memoMsgService.convertToMemoUserName(pagination.getRows()), pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    /**
     * 안읽은 메시지 개수
     */
    @GetMapping("unread-message")
    public ResponseEntity<JsonResult<Integer>> getUnreadMessageCount() {
        return ResponseEntity.ok(data(memoMsgService.getUnreadMessageCount()));
    }

    /**
     * 상세 보기
     */
    @GetMapping("{seq}")
    public ResponseEntity<JsonResult<MemoMsgEntity>> getMemoMessage(@PathVariable Integer seq) {
        return ResponseEntity.ok(data(memoMsgService.getMemoMessage(seq)));
    }

    /**
     * 쪽지 보내기
     */
    @PostMapping("")
    public ResponseEntity<JsonResult<String>> sendMemoMessage(@Valid @RequestBody MemoMsgFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        return ResponseEntity.ok(data(memoMsgService.getRepository().insertMemoMessage(null, form)));
    }

    /**
     * 쪽지 답장
     */
    @PostMapping("{seq}")
    public ResponseEntity<JsonResult<String>> receiveMemoMessage(@PathVariable Integer seq, @RequestBody MemoMsgFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        return ResponseEntity.ok(data(memoMsgService.getRepository().insertMemoMessage(seq, form)));
    }

    /**
     * 받은 쪽지 삭제
     */
    @DeleteMapping("receive")
    public ResponseEntity<JsonResult<Void>> deleteReceiveMemoMessages(@RequestParam List<Integer> memoSequences) {
        memoMsgService.getRepository().deleteMemoMessages(memoSequences, null);
        return ResponseEntity.ok(create());
    }

    /**
     * 보낸 쪽지 삭제
     */
    @DeleteMapping("send")
    public ResponseEntity<JsonResult<Void>> deleteSendMemoMessages(@RequestParam List<String> messageIds) {
        memoMsgService.getRepository().deleteMemoMessages(null, messageIds);
        return ResponseEntity.ok(create());
    }

    /**
     * 프로필 수정
     */
    @PostMapping(value = "profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResult<String>> updateProfile(@RequestParam MultipartFile file) {
        return ResponseEntity.ok(data(memoMsgService.updateProfilePhoto(file)));
    }

    /**
     * 프로필 파일 삭제
     */
    @DeleteMapping(value = "delete-specific-file")
    public ResponseEntity<JsonResult<Void>> deleteSpecificFile() {
        memoMsgService.deleteProfilePhoto();
        return ResponseEntity.ok(create());
    }

    /**
     * 프로필 파일 다운로드
     * 썸네일 경우: 파일명 포함한 경로 전달. ex)/tmp/1611720465270_1026800762681955_logo.png
     * 그 외: person_list.profilePhoto 전달.
     */
    @GetMapping(value = "profile-resource", params = {"token"})
    public ResponseEntity<Resource> specificFileResource(@RequestParam("path") String path) {
        final String file = memoMsgService.getProfilePhotoSavePath(path);

        final Resource resource = this.fileSystemStorageService.loadAsResource(Paths.get(file.substring(0, file.indexOf(getName(file))-1)), getName(file));

        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(resource).isPresent() ? MediaTypeFactory.getMediaType(resource).get() : MediaType.APPLICATION_OCTET_STREAM)
                .headers(header -> header.add(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.builder("attachment").filename(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8).build().toString()))
                .body(resource);
    }
}
