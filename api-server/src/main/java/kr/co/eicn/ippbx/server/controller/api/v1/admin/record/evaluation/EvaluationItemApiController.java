package kr.co.eicn.ippbx.server.controller.api.v1.admin.record.evaluation;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.EvaluationItem;
import kr.co.eicn.ippbx.server.model.form.EvaluationWholeCategoryFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.EvaluationItemRepository;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;

/**
 * 녹취관리 > 평가항목관리
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/record/evaluation/item", produces = MediaType.APPLICATION_JSON_VALUE)
public class EvaluationItemApiController extends ApiBaseController {

    private final EvaluationItemRepository repository;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<EvaluationItem>>> list() {
        return ResponseEntity.ok(data(repository.findAll()));
    }

    @GetMapping("{id}")
    public ResponseEntity<JsonResult<EvaluationItem>> get(@PathVariable Long id) {
        return ResponseEntity.ok(data(repository.findOneIfNullThrow(id)));
    }

    /**
     * 평가항목 저장
     */
    @PostMapping("target/{evaluationId}")
    public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody EvaluationWholeCategoryFormRequest form, BindingResult bindingResult, @PathVariable Long evaluationId) {
        repository.insert(form, evaluationId);
        return ResponseEntity.created(URI.create("api/v1/admin/record/evaluation/item/target/" + evaluationId)).body((create()));
    }

    @GetMapping("search")
    public ResponseEntity<JsonResult<List<EvaluationItem>>> search() {
        return ResponseEntity.ok(data(repository.findAll()));
    }
}
