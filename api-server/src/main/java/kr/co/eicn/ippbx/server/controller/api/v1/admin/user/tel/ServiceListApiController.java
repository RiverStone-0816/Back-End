package kr.co.eicn.ippbx.server.controller.api.v1.admin.user.tel;

import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.Number_070;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServerInfo;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServiceList;
import kr.co.eicn.ippbx.model.dto.eicn.OrganizationSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ServiceListDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ServiceListSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryServiceListResponse;
import kr.co.eicn.ippbx.model.form.ServiceListFormRequest;
import kr.co.eicn.ippbx.model.form.ServiceListFormUpdateRequest;
import kr.co.eicn.ippbx.model.search.ServiceListSearchRequest;
import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.repository.eicn.Number070Repository;
import kr.co.eicn.ippbx.server.repository.eicn.ServerInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ServiceRepository;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.spring.IsAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.Number_070.NUMBER_070;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.ServiceList.SERVICE_LIST;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * 번호/그룹/사용자 > 번호/서비스관리 > 대표서비스
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/user/tel/service", produces = MediaType.APPLICATION_JSON_VALUE)
public class ServiceListApiController extends ApiBaseController {

    private final ServiceRepository repository;
    private final Number070Repository numberRepository;
    private final ServerInfoRepository serverInfoRepository;
    private final OrganizationService organizationService;

    @IsAdmin
    @GetMapping("")
    public ResponseEntity<JsonResult<List<ServiceListSummaryResponse>>> list(ServiceListSearchRequest search) {
        final List<ServiceList> serviceLists = repository.findAll(search);
        final List<ServerInfo> serverInfos = serverInfoRepository.findAll();
        final Map<String, Number_070> number070Map = numberRepository.findByAllListCovertToMap(NUMBER_070.TYPE.eq((byte) 2));
        final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();
        final List<ServiceListSummaryResponse> rows = serviceLists.stream()
                .map((e) -> {
                    final ServiceListSummaryResponse entity = convertDto(e, ServiceListSummaryResponse.class);
                    final Number_070 number = number070Map.get(entity.getSvcNumber());
                    if (number != null)
                        serverInfos.stream().filter(server -> isNotEmpty(server.getHost()) && server.getHost().equals(number.getHost())).findFirst()
                                .ifPresent(server -> entity.setHostName(server.getName()));

                    if (isNotEmpty(entity.getGroupCode()))
                        entity.setOrganizationSummary(organizationService.getCompanyTrees(companyTrees, entity.getGroupCode())
                                .stream()
                                .map(group -> convertDto(group, OrganizationSummaryResponse.class))
                                .collect(Collectors.toList()));

                    return entity;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data((rows)));
    }

    @GetMapping("counsel")
    public ResponseEntity<JsonResult<List<ServiceListSummaryResponse>>> listCounsel(ServiceListSearchRequest search) {
        final List<ServiceList> serviceLists = repository.findAll(search);
        final List<ServerInfo> serverInfos = serverInfoRepository.findAll();
        final Map<String, Number_070> number070Map = numberRepository.findByAllListCovertToMap(NUMBER_070.TYPE.eq((byte) 2));
        final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();
        final List<ServiceListSummaryResponse> rows = serviceLists.stream()
                .map((e) -> {
                    final ServiceListSummaryResponse entity = convertDto(e, ServiceListSummaryResponse.class);
                    final Number_070 number = number070Map.get(entity.getSvcNumber());
                    if (number != null)
                        serverInfos.stream().filter(server -> isNotEmpty(server.getHost()) && server.getHost().equals(number.getHost())).findFirst()
                                .ifPresent(server -> entity.setHostName(server.getName()));

                    if (isNotEmpty(entity.getGroupCode()))
                        entity.setOrganizationSummary(organizationService.getCompanyTrees(companyTrees, entity.getGroupCode())
                                .stream()
                                .map(group -> convertDto(group, OrganizationSummaryResponse.class))
                                .collect(Collectors.toList()));

                    return entity;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data((rows)));
    }

    @GetMapping("{seq}")
    public ResponseEntity<JsonResult<ServiceListDetailResponse>> get(@PathVariable Integer seq) {
        final ServiceListDetailResponse detail = convertDto(repository.findOneIfNullThrow(seq), ServiceListDetailResponse.class);
        final List<ServerInfo> serverInfos = serverInfoRepository.findAll();
        final Map<String, Number_070> number070Map = numberRepository.findByAllListCovertToMap(NUMBER_070.TYPE.eq((byte) 2));
        final Number_070 number = number070Map.get(detail.getSvcNumber());

        if (number != null)
            serverInfos.stream().filter(server -> isNotEmpty(server.getHost()) && server.getHost().equals(number.getHost())).findFirst()
                    .ifPresent(server -> detail.setHostName(server.getName()));

        if (isNotEmpty(detail.getGroupCode()))
            detail.setOrganizationSummary(organizationService.getCompanyTrees(detail.getGroupCode())
                    .stream()
                    .map(group -> convertDto(group, OrganizationSummaryResponse.class))
                    .collect(Collectors.toList()));

        return ResponseEntity.ok().body(data(detail));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Integer>> register(@Valid @RequestBody ServiceListFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        return ResponseEntity.created(URI.create("api/v1/admin/user/tel/service"))
                .body(data(repository.insertOnGeneratedKey(form).getValue(SERVICE_LIST.SEQ)));
    }

    @PutMapping("{seq}")
    public ResponseEntity<JsonResult<Void>> update(@Valid @RequestBody ServiceListFormUpdateRequest form, BindingResult bindingResult,
                                                   @PathVariable Integer seq) {
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

    /**
     * 대표서비스 조회
     */
    @GetMapping("add-services")
    public ResponseEntity<JsonResult<List<SummaryServiceListResponse>>> addServices() {
        return ResponseEntity.ok(data(repository.findAll().stream().map(e -> convertDto(e, SummaryServiceListResponse.class)).collect(Collectors.toList())));
    }
}
