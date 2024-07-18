package kr.co.eicn.ippbx.server.controller.api.v1.admin.stt.keyword;

import kr.co.eicn.ippbx.meta.jooq.customdb.tables.pojos.ProhibitedKeyword;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.service.ProhibitedKeywordService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/keyword", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProhibitedKeywordController extends ApiBaseController {
    private final ProhibitedKeywordService service;

    @GetMapping
    public ResponseEntity<JsonResult<List<ProhibitedKeyword>>> list() {
        return ResponseEntity.ok(data(service.getRepository().findAll()));
    }

    @PostMapping("prohibit/{keyword}")
    public ResponseEntity<JsonResult<Void>> prohibitInsert(@PathVariable String keyword) {
        service.getRepository().prohibitKeywordInsert(keyword);
        return ResponseEntity.ok(create());
    }

    @PostMapping("keyword/{keyword}")
    public ResponseEntity<JsonResult<Void>> keywordInsert(@PathVariable String keyword) {
        service.getRepository().keywordInsert(keyword);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping("{keyword}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable String keyword) {
        service.getRepository().keywordDelete(keyword);
        return ResponseEntity.ok(create());
    }
}
