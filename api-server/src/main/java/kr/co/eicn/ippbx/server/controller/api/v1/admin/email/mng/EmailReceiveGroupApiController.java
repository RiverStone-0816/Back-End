package kr.co.eicn.ippbx.server.controller.api.v1.admin.email.mng;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.*;
import kr.co.eicn.ippbx.server.model.dto.eicn.*;
import kr.co.eicn.ippbx.server.model.entity.eicn.EmailMemberListEntity;
import kr.co.eicn.ippbx.server.model.form.EmailReceiveGroupFormRequest;
import kr.co.eicn.ippbx.server.repository.eicn.EmailMemberListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.EmailMngRepository;
import kr.co.eicn.ippbx.server.repository.eicn.EmailMemberGroupRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.server.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.EMAIL_MEMBER_GROUP;
import static kr.co.eicn.ippbx.server.jooq.eicn.Tables.EMAIL_MEMBER_LIST;
import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;

/**
 * 이메일상담관리 > 이메일설정관리 > 이메일수신그룹관리
 */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/email/mng/receive-group", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmailReceiveGroupApiController extends ApiBaseController {

    private final EmailMemberGroupRepository repository;
    private final EmailMngRepository emailMngRepository;
    private final EmailMemberListRepository emailMemberListRepository;
    private final PersonListRepository personListRepository;
    private final OrganizationService organizationService;

    @GetMapping("")
    public ResponseEntity<JsonResult<List<EmailReceiveGroupSummaryResponse>>> list() {
        final Map<Integer, String> emailServiceInfo = emailMngRepository.findAll().stream().collect(Collectors.toMap(EmailServiceInfo::getSeq, EmailServiceInfo::getServiceName));
        final List<EmailMemberListEntity> emailMemberLists = emailMemberListRepository.getEmailMemberListEntities();

        final List<EmailReceiveGroupSummaryResponse> list = repository.findAll().stream()
                .map((e) -> {
                    final EmailReceiveGroupSummaryResponse emailGroupSummaryResponse = convertDto(e, EmailReceiveGroupSummaryResponse.class);
                    emailGroupSummaryResponse.setServiceName(emailServiceInfo.get(e.getEmailId()));

                    final List<EmailMemberListSummaryResponse> emailMemberList = emailMemberLists.stream()
                            .filter(member -> member.getGroupId().equals(e.getGroupId()))
                            .map(member -> convertDto(member.getPerson(), EmailMemberListSummaryResponse.class))
                            .collect(Collectors.toList());

                    emailGroupSummaryResponse.setEmailMemberLists(emailMemberList);
                    emailGroupSummaryResponse.setMemberCnt(emailMemberList.size());

                    return emailGroupSummaryResponse;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(list));
    }

    @GetMapping(value = "{groupId}")
    public ResponseEntity<JsonResult<EmailReceiveGroupDetailResponse>> get(@PathVariable Integer groupId) {
        final EmailMemberGroup emailMemberGroup = repository.findOneIfNullThrow(groupId);
        final Map<Integer, String> emailServiceInfo = emailMngRepository.findAll().stream().collect(Collectors.toMap(EmailServiceInfo::getSeq, EmailServiceInfo::getServiceName));
        final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();
        final List<EmailMemberListEntity> emailMemberLists = emailMemberListRepository.getEmailMemberListEntities();

        final EmailReceiveGroupDetailResponse emailGroupDetailResponse = convertDto(emailMemberGroup, EmailReceiveGroupDetailResponse.class);

        emailGroupDetailResponse.setServiceId(emailMemberGroup.getEmailId());
        emailGroupDetailResponse.setServiceName(emailServiceInfo.get((emailMemberGroup.getEmailId())));
        emailGroupDetailResponse.setEmailMemberLists(
                emailMemberLists.stream()
                        .filter(e -> e.getGroupId().equals(emailMemberGroup.getGroupId()))
                        .map(e -> {
                            final EmailMemberListSummaryResponse memberListSummaryResponse = convertDto(e.getPerson(), EmailMemberListSummaryResponse.class);
                            companyTrees.stream()
                                        .filter(companyTree -> companyTree.getGroupCode().equals(e.getPerson().getGroupCode()))
                                        .findFirst()
                                        .ifPresent(companyTree -> memberListSummaryResponse.setOrganization(convertDto(companyTree, OrganizationSummaryResponse.class)));
                            return memberListSummaryResponse;
                        })
                        .collect(Collectors.toList())
        );

        return ResponseEntity.ok(data(emailGroupDetailResponse));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Integer>> post(@Valid @RequestBody EmailReceiveGroupFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        return ResponseEntity.created(URI.create("api/v1/admin/email/mng/receive-group"))
                .body(data(repository.insertOnGeneratedKey(form).getValue(EMAIL_MEMBER_GROUP.GROUP_ID)));
    }

    @PutMapping(value = "{groupId}")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody EmailReceiveGroupFormRequest form, BindingResult bindingResult,
                                                             @PathVariable Integer groupId) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        repository.emailGroupUpdate(form, groupId);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping(value = "{groupId}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable Integer groupId) {
        repository.emailGroupDelete(groupId);
        return ResponseEntity.ok(create());
    }

    /**
     *   관련 이메일 서비스 목록조회
     */
    @GetMapping("services")
    public ResponseEntity<JsonResult<List<EmailMngResponse>>> emailService() {
        return ResponseEntity.ok(data(emailMngRepository.findAll().stream().map(e -> convertDto(e, EmailMngResponse.class)).collect(Collectors.toList())));
    }

    /**
     * 추가 가능한 사용자 목록조회
     */
    @GetMapping("add-member")
    public ResponseEntity<JsonResult<List<EmailMemberListSummaryResponse>>> addMember(@RequestParam(required = false) Integer groupId) {
        final List<EmailMemberList> emailMemberLists = emailMemberListRepository.findAll(EMAIL_MEMBER_LIST.GROUP_ID.eq(groupId));
        final List<PersonList> personLists = personListRepository.findAll().stream().sorted(Comparator.comparing(PersonList::getIdName)).collect(Collectors.toList());
        final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();

        return ResponseEntity.ok(data(
                personLists.stream().filter(e -> {
                    if (emailMemberLists != null)
                        return emailMemberLists.stream().noneMatch(emailMemberList -> emailMemberList.getUserid().equals(e.getId()));
                    return true;
                })
                        .map((e) -> {
                            final EmailMemberListSummaryResponse emailMemberListSummaryResponse = convertDto(e, EmailMemberListSummaryResponse.class);
                            companyTrees.stream()
                                    .filter(companyTree -> companyTree.getGroupCode().equals(e.getGroupCode()))
                                    .findFirst()
                                    .ifPresent(companyTree -> emailMemberListSummaryResponse.setOrganization(convertDto(companyTree, OrganizationSummaryResponse.class)));
                            return emailMemberListSummaryResponse;
                        })
                        .collect(Collectors.toList())
        ));
    }
}