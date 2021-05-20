package kr.co.eicn.ippbx.server.controller.api.v1.admin.conf.room;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.Number_070;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ConfRoom;
import kr.co.eicn.ippbx.model.dto.eicn.ConfRoomDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ConfRoomSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.OrganizationSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryNumber070Response;
import kr.co.eicn.ippbx.model.entity.eicn.CompanyServerEntity;
import kr.co.eicn.ippbx.model.enums.NumberType;
import kr.co.eicn.ippbx.model.form.ConfRoomFormRequest;
import kr.co.eicn.ippbx.model.search.ConfRoomSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.ConfRoomRepository;
import kr.co.eicn.ippbx.server.repository.eicn.Number070Repository;
import kr.co.eicn.ippbx.server.service.CacheService;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.util.FunctionUtils;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import static java.util.Comparator.comparing;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 전화회의 > 회의실관리
 */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/conf/room", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConfRoomApiController extends ApiBaseController {
    protected final Logger logger = LoggerFactory.getLogger(ConfRoomApiController.class);

    private final ConfRoomRepository repository;
    private final Number070Repository numberRepository;
    private final CacheService cacheService;
    private final OrganizationService organizationService;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<ConfRoomSummaryResponse>>> pagination(ConfRoomSearchRequest search) {
        final List<CompanyServerEntity> companyServerEntities = cacheService.getCompanyServerList(g.getUser().getCompanyId());
        Map<String, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070> numberStringObjectMap =
                numberRepository.findAll().stream().filter(FunctionUtils.distinctByKey(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070::getNumber)).collect(Collectors.toMap(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070::getNumber, e -> e));

        List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();
        final Pagination<ConfRoom> pagination = repository.pagination(search);
        final List<ConfRoomSummaryResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    ConfRoomSummaryResponse response = convertDto(e, ConfRoomSummaryResponse.class);

                    if (Objects.nonNull(numberStringObjectMap.get(e.getRoomNumber()))) {
                        kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070 number_070 = numberStringObjectMap.get(e.getRoomNumber());
                        companyServerEntities.stream()
                                .filter(company -> company.getServer().getHost().equals(number_070.getHost()))
                                .findAny()
                                .ifPresent(r -> response.setHostName(r.getServer().getName()));
                    }
                    response.setCompanyTrees(organizationService.getCompanyTrees(companyTrees, e.getGroupCode()).stream().map(company -> convertDto(company, OrganizationSummaryResponse.class)).collect(Collectors.toList()));
                    return response;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping(value = "{seq}")
    public ResponseEntity<JsonResult<ConfRoomDetailResponse>> get(@PathVariable Integer seq) {
        ConfRoomDetailResponse response = convertDto(repository.findOneIfNullThrow(seq), ConfRoomDetailResponse.class);
        response.setCompanyTrees(organizationService.getCompanyTrees(response.getGroupCode()).stream().map(company -> convertDto(company, OrganizationSummaryResponse.class)).collect(Collectors.toList()));
        return ResponseEntity.ok().body(data(response));
    }

    @PostMapping
    public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody ConfRoomFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.insert(form);
        return ResponseEntity.created(URI.create("api/v1/admin/conf/room")).body(create());
    }

    @PutMapping(value = "{seq}")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody ConfRoomFormRequest form, BindingResult bindingResult,
                                                @PathVariable Integer seq) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.updateByKey(form, seq);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping(value = "{seq}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer seq) {
        repository.deleteOnIfNullThrow(seq);
        return ResponseEntity.ok(create());
    }

    @GetMapping("unused-confroom-number")
    public ResponseEntity<JsonResult<List<SummaryNumber070Response>>> getConfRoomNumber() {
        List<ConfRoom> confRooms = repository.findAll(); //회의실 목록

        List<SummaryNumber070Response> number070s = numberRepository.findAll(Number_070.NUMBER_070.TYPE.eq(NumberType.CONFERENCE.getCode()), Number_070.NUMBER_070.NUMBER.desc())
                .stream()
                .filter(e -> confRooms.stream().noneMatch(s -> s.getRoomNumber().equals(e.getNumber())))
                .map(e -> convertDto(e, SummaryNumber070Response.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(number070s));
    }
}
