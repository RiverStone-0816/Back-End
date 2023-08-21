package kr.co.eicn.ippbx.server.controller.api.v1.admin.service.log;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.LoginHistory;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList;
import kr.co.eicn.ippbx.model.dto.eicn.LoginHistoryResponse;
import kr.co.eicn.ippbx.model.enums.DialStatus;
import kr.co.eicn.ippbx.model.enums.LogoutStatus;
import kr.co.eicn.ippbx.model.search.LoginHistorySearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.LoginHistoryRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.util.spring.IsAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 *   서비스운영관리 > 로그인이력 > 로그인이력관리
 **/
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/service/log/history", produces = MediaType.APPLICATION_JSON_VALUE)
public class LoginHistoryApiController extends ApiBaseController {
    private final LoginHistoryRepository repository;
    private final PersonListRepository personListRepository;
    private final OrganizationService organizationService;

    @IsAdmin
    @GetMapping
    public ResponseEntity<JsonResult<Pagination<LoginHistoryResponse>>> pagination(LoginHistorySearchRequest search) {
        final Pagination<LoginHistory> pagination = repository.pagination(search);
        final Map<String, String> personMap = personListRepository.findAll().stream().collect(Collectors.toMap(PersonList::getId, PersonList::getGroupCode));
        final List<CompanyTree> companyTreeList = organizationService.getAllCompanyTrees();

        final List<LoginHistoryResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    LoginHistoryResponse entity = convertDto(e, LoginHistoryResponse.class);
                    if (Objects.nonNull(personMap.get(e.getUserid())))
                        entity.setCompanyTrees(organizationService.getCompanyTrees(companyTreeList, personMap.get(e.getUserid())).stream().map(CompanyTree::getGroupName).collect(Collectors.toList()));
                    else
                        entity.setCompanyTrees(Collections.emptyList());
                    entity.setLogoutStatus(LogoutStatus.of(e.getLogoutStatus()));
                    entity.setDialStatus(DialStatus.of(e.getDialStatus()));

                    return entity;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }
}
