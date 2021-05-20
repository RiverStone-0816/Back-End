package kr.co.eicn.ippbx.server.controller.api.v1.admin.acd.grade;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchQueueResponse;
import kr.co.eicn.ippbx.model.entity.eicn.GradeListEntity;
import kr.co.eicn.ippbx.model.form.GradeListFormRequest;
import kr.co.eicn.ippbx.model.search.GradeListSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.GradeListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.QueueNameRepository;
import kr.co.eicn.ippbx.server.service.CommonFieldPoster;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.Tables.GRADE_LIST;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * ACD > 고객등급 Routing > BlackList, VIP
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/acd/grade/gradelist", produces = MediaType.APPLICATION_JSON_VALUE)
public class GradeListApiController extends ApiBaseController {

    private final GradeListRepository repository;
    private final QueueNameRepository queueNameRepository;
    private final CommonFieldPoster commonFieldPoster;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<GradeListEntity>>> pagination(GradeListSearchRequest search) {
        return ResponseEntity.ok(data(repository.pagination(search)));
    }

    @GetMapping("{seq}")
    public ResponseEntity<JsonResult<GradeListEntity>> get(@PathVariable Integer seq) {
        return ResponseEntity.ok(data(repository.findOneIfNullThrow(seq)));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Integer>> post( @Valid @RequestBody GradeListFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        return ResponseEntity.created(URI.create("api/v1/admin/acd/grade/gradelist"))
                .body(data(repository.insertOnGeneratedKey(form).getValue(GRADE_LIST.SEQ)));
    }

    @PutMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody GradeListFormRequest form, BindingResult bindingResult, @PathVariable Integer seq) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.updateByKey(form, seq);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        repository.deleteOnIfNullThrow(seq);
        return ResponseEntity.ok(create());
    }

    @GetMapping("queue")
    public ResponseEntity<JsonResult<List<SearchQueueResponse>>> queueList() {
        return ResponseEntity.ok(data(queueNameRepository.findAll().stream()
                .map((e) -> convertDto(e, SearchQueueResponse.class))
                .collect(Collectors.toList())));
    }

    @PostMapping(value = "fields/by-excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JsonResult<Void>> postFieldsByExcel(@RequestParam MultipartFile file, @RequestParam String gradeType) throws IOException {
        if (StringUtils.isNotEmpty(gradeType)) {
            if (gradeType.equals("VIP")) {
                commonFieldPoster.postByExcel(CommonFieldPoster.ExcelType.VIP, g.getUser().getCompanyId(), file);
            } else if (gradeType.equals("BLACK")) {
                commonFieldPoster.postByExcel(CommonFieldPoster.ExcelType.BLACK, g.getUser().getCompanyId(), file);
            }
        }
        return ResponseEntity.ok(create());
    }

}
