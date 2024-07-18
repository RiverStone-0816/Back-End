package kr.co.eicn.ippbx.server.controller.api.v1.admin.stt.learn;

import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.CommonTranscribeGroup;
import kr.co.eicn.ippbx.model.dto.eicn.LearnGroupResponse;
import kr.co.eicn.ippbx.model.entity.customdb.LearnGroupEntity;
import kr.co.eicn.ippbx.model.form.LearnGroupFormRequest;
import kr.co.eicn.ippbx.model.search.LearnGroupSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.service.LearnGroupService;
import kr.co.eicn.ippbx.server.service.TranscribeGroupService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/stt/learn/group", produces = MediaType.APPLICATION_JSON_VALUE)
public class LearnGroupApiController extends ApiBaseController {
    private final LearnGroupService learnGroupService;
    private final TranscribeGroupService transcribeGroupService;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<LearnGroupResponse>>> pagination(LearnGroupSearchRequest search) {
        Pagination<LearnGroupEntity> pagination;
        Map<Integer, String> groupNameMap = transcribeGroupService.getRepository().findAll().stream().collect(Collectors.toMap(CommonTranscribeGroup::getSeq, CommonTranscribeGroup::getGroupName));

        pagination = learnGroupService.getRepository().pagination(search);
        List<LearnGroupResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    LearnGroupResponse learnGroupResponse = convertDto(e, LearnGroupResponse.class);

                    learnGroupResponse.setGroupList(Arrays.stream(e.getLearnGroupCode().split("\\|")).map(f -> groupNameMap.get(Integer.parseInt(f))).collect(Collectors.toList()));

                    return learnGroupResponse;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping(value = "{seq}")
    public ResponseEntity<JsonResult<LearnGroupResponse>> get(@PathVariable Integer seq) {

        final LearnGroupResponse detail = convertDto(learnGroupService.getRepository().findOneIfNullThrow(seq), LearnGroupResponse.class);

        return ResponseEntity.ok().body(data(detail));
    }

    @PostMapping(value = "")
    public ResponseEntity<JsonResult<Integer>> post(@Valid @RequestBody LearnGroupFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        learnGroupService.getRepository().insert(form);
        return ResponseEntity.created(URI.create("api/v1/admin/stt/learn/group")).body(create());
    }

    @PutMapping(value = "{seq}")
    public ResponseEntity<JsonResult<Integer>> update(@PathVariable Integer seq, @Valid @RequestBody LearnGroupFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        learnGroupService.getRepository().update(seq, form);
        return ResponseEntity.created(URI.create("api/v1/admin/stt/learn/group")).body(create());
    }

    @GetMapping("learn-group")
    public ResponseEntity<JsonResult<List<LearnGroupResponse>>> learnGroup() {
        final List<LearnGroupResponse> list = learnGroupService.getRepository().findAll().stream()
                .map((e) -> convertDto(e, LearnGroupResponse.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(data(list));
    }

    @PutMapping(value = "status/{seq}")
    public ResponseEntity<JsonResult<Void>> statusUpdate(@PathVariable Integer seq) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        learnGroupService.getRepository().statusUpdate(seq);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping(value = "{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        learnGroupService.getRepository().delete(seq);
        return ResponseEntity.ok(create());
    }

}
