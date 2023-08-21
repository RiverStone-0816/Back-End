package kr.co.eicn.ippbx.server.controller.api.v1.admin.user.user;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PickupGroup;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ServerInfo;
import kr.co.eicn.ippbx.model.dto.eicn.OrganizationSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PickUpGroupDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PickUpGroupSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryPickUpPersonResponse;
import kr.co.eicn.ippbx.model.form.PickUpGroupFormRequest;
import kr.co.eicn.ippbx.model.form.PickUpGroupFormUpdateRequest;
import kr.co.eicn.ippbx.model.search.PickUpGroupSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PhoneInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PickUpGroupRepository;
import kr.co.eicn.ippbx.server.repository.eicn.ServerInfoRepository;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.util.FunctionUtils;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
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

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PersonList.PERSON_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PhoneInfo.PHONE_INFO;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PickupGroup.PICKUP_GROUP;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * 번호/그룹/사용자 > 사용자관리 > 당겨받기그룹설정
 */
@IsAdmin
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/user/user/pickup-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class PickUpGroupApiController extends ApiBaseController {

    private final PickUpGroupRepository repository;
    private final PhoneInfoRepository phoneInfoRepository;
    private final OrganizationService organizationService;
    private final ServerInfoRepository serverInfoRepository;
    private final PersonListRepository personListRepository;

    @IsAdmin
    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<PickUpGroupSummaryResponse>>> pagination(PickUpGroupSearchRequest search) {
        final Pagination<PickupGroup> pagination = repository.pagination(search);
        final List<ServerInfo> serverInfos = serverInfoRepository.findAll();
        final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();

        final List<PickUpGroupSummaryResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final PickUpGroupSummaryResponse summary = convertDto(e, PickUpGroupSummaryResponse.class);
                    summary.setPersonCount(phoneInfoRepository.fetchCount(PHONE_INFO.PICKUP.eq(String.valueOf(summary.getGroupcode()))));

                    if (isNotEmpty(e.getHost()))
                        serverInfos.stream().filter(server -> isNotEmpty(server.getHost()) && server.getHost().equals(e.getHost())).findFirst()
                                .ifPresent(server -> summary.setHostName(server.getName()));

                    if (isNotEmpty(e.getGroupCode()))
                        summary.setCompanyTrees(organizationService.getCompanyTrees(companyTrees , summary.getGroupCode())
                                .stream()
                                .map(group -> convertDto(group, OrganizationSummaryResponse.class))
                                .collect(Collectors.toList()));

                    return summary;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping("{groupcode}")
    public ResponseEntity<JsonResult<PickUpGroupDetailResponse>> get(@PathVariable Integer groupcode) {
        final PickUpGroupDetailResponse detail = convertDto(repository.findOneIfNullThrow(PICKUP_GROUP.GROUPCODE.eq(groupcode)), PickUpGroupDetailResponse.class);
        final List<ServerInfo> serverInfos = serverInfoRepository.findAll();
        final Map<String, kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList> personListMap = personListRepository.findAll(PERSON_LIST.EXTENSION.isNotNull().and(PERSON_LIST.EXTENSION.notEqual(EMPTY)))
                .stream()
		        .filter(FunctionUtils.distinctByKey(PersonList::getExtension))
                .collect(Collectors.toMap(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList::getExtension, e -> e));

        final List<PickupGroup> pickupGroups = repository.findAll();

        final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo> phoneInfos = phoneInfoRepository.findAll(PHONE_INFO.HOST.eq(detail.getHost()), PHONE_INFO.EXTENSION.desc());

        final List<SummaryPickUpPersonResponse> pickUpPersons = phoneInfos
                .stream()
                .map((e) -> {
                    final SummaryPickUpPersonResponse person = convertDto(e, SummaryPickUpPersonResponse.class);

                    if (isNotEmpty(e.getHost()))
                        serverInfos.stream().filter(server -> isNotEmpty(server.getHost()) && server.getHost().equals(e.getHost())).findFirst()
                                .ifPresent(server -> person.setHostName(server.getName()));

                    final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList personList = personListMap.get(e.getExtension());
                    if (personList != null)
                        person.setIdName(personList.getIdName());
                    else
                        person.setIdName("사용자없음");

                    person.setPickupName("당겨받기그룹없음");
                    if (isNotEmpty(e.getPickup()))
                        pickupGroups.stream()
                                .filter(pickup -> String.valueOf(pickup.getGroupcode()).equals(e.getPickup()))
                                .findFirst()
                                .ifPresent(pickup -> person.setPickupName(pickup.getGroupname()));

                    return person;
                })
                .collect(Collectors.toList());

        // 추가 가능한 사용자
        detail.setAddOnPersons(
                pickUpPersons.stream().filter(e -> !e.getPickup().equals(String.valueOf(detail.getGroupcode()))).collect(Collectors.toList())
        );
        // 추가된 사용자
        detail.setAddPersons(
                pickUpPersons.stream().filter(e -> e.getPickup().equals(String.valueOf(detail.getGroupcode()))).collect(Collectors.toList())
        );

        return ResponseEntity.ok().body(data(detail));
    }

    /**
     * @return 생성된 당겨받기그룹
     */
    @PostMapping("")
    public ResponseEntity<JsonResult<Integer>> register(@Valid @RequestBody PickUpGroupFormRequest form, BindingResult bindingResult) {
    	if (bindingResult.hasErrors())
    	    throw new ValidationException(bindingResult);

        return ResponseEntity.created(URI.create("api/v1/admin/user/user/pickup-group"))
                .body(data(repository.insertOnGeneratedKey(form).getValue(PICKUP_GROUP.GROUPCODE)));
    }

    @PutMapping("{groupcode}")
    public ResponseEntity<JsonResult<Void>> update(@Valid @RequestBody PickUpGroupFormUpdateRequest form, BindingResult bindingResult, @PathVariable Integer groupcode) {
        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult);

        repository.updateByKey(form, groupcode);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping("{groupcode}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer groupcode) {
        repository.delete(groupcode);
        return ResponseEntity.ok(create());
    }
}
