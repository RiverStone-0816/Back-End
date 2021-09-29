package kr.co.eicn.ippbx.server.controller.api.v1.admin.acd.route;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.DuplicateKeyException;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CsRoute;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.QueueName;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchQueueResponse;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.form.CsRouteFormRequest;
import kr.co.eicn.ippbx.model.search.CsRouteSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CsRouteRepository;
import kr.co.eicn.ippbx.server.repository.eicn.QueueNameRepository;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.PBXServerInterface;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.CsRoute.CS_ROUTE;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * ACD > DB Routing > 직전통화 상담원 연결 라우팅
 */

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/acd/route/cs", produces = MediaType.APPLICATION_JSON_VALUE)
public class CsRouteApiController extends ApiBaseController {

    private final CsRouteRepository repository;
    private final QueueNameRepository queueNameRepository;
    private final CacheService cacheService;
    private final PBXServerInterface pbxServerInterface;

    @GetMapping
    public ResponseEntity<JsonResult<Pagination<CsRouteSummaryResponse>>> pagination(CsRouteSearchRequest search) {
        final Pagination<CsRoute> pagination = repository.pagination(search);
        final Map<String, String> queueNameMap = queueNameRepository.findAll().stream().collect(Collectors.toMap(QueueName::getNumber, QueueName::getHanName));

        final List<CsRouteSummaryResponse> rows = pagination.getRows().stream()
                .map(e -> {
                    CsRouteSummaryResponse entity = convertDto(e, CsRouteSummaryResponse.class);
                    if(Objects.nonNull(queueNameMap.get(e.getHuntNumber()))) {
                        entity.setQueueNumber(e.getHuntNumber());
                        entity.setQueueName(queueNameMap.get(e.getHuntNumber()));
                    }

                    return entity;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping("{seq}")
    public ResponseEntity<JsonResult<CsRouteGetResponse>> get(@PathVariable Integer seq) {
        final CsRoute route = repository.findOneIfNullThrow(seq);
        final CsRouteGetResponse result = convertDto(route, CsRouteGetResponse.class);
        result.setQueueNumber(route.getHuntNumber());
        return ResponseEntity.ok(data(result));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Integer>> post(@Valid @RequestBody CsRouteFormRequest form, BindingResult bindingResult){
        if(!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        if (repository.existsHunt(form.getQueueNumber()))
            throw new DuplicateKeyException("해당하는 헌트번호설정이 이미 존재합니다.");

        return ResponseEntity.created(URI.create("api/v1/admin/acd/route/cs"))
                .body(data(repository.insertOnGeneratedKey(form).getValue(CS_ROUTE.SEQ)));
    }

    @PutMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody CsRouteFormRequest form, BindingResult bindingResult, @PathVariable Integer seq){
        if(!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        final List<CompanyServerEntity> pbxServerList = cacheService.pbxServerList(g.getUser().getCompanyId());
        pbxServerList.forEach(e -> {
            DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
            repository.updateByKey(pbxDsl, form, seq);
        });

        repository.updateByKey(form, seq);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        final List<CompanyServerEntity> pbxServerList = cacheService.pbxServerList(g.getUser().getCompanyId());
        pbxServerList.forEach(e -> {
            DSLContext pbxDsl = pbxServerInterface.using(e.getHost());
            repository.delete(pbxDsl, seq);
        });

        repository.deleteOnIfNullThrow(seq);
        return ResponseEntity.ok(create());
    }

    @GetMapping("queue")
    public ResponseEntity<JsonResult<List<SearchQueueResponse>>> queueList() {
        return ResponseEntity.ok(data(queueNameRepository.findAll().stream()
                .map((e) -> convertDto(e, SearchQueueResponse.class))
                .collect(Collectors.toList())));
    }
}
