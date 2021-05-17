package kr.co.eicn.ippbx.server.controller.api.v1.admin.user.user;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.server.exception.ValidationException;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.CompanyTree;
import kr.co.eicn.ippbx.server.model.dto.eicn.*;
import kr.co.eicn.ippbx.server.model.entity.eicn.CompanyLicenceEntity;
import kr.co.eicn.ippbx.server.model.entity.eicn.UserEntity;
import kr.co.eicn.ippbx.server.model.enums.DataSearchAuthorityType;
import kr.co.eicn.ippbx.server.model.enums.IdType;
import kr.co.eicn.ippbx.server.model.form.PersonFormRequest;
import kr.co.eicn.ippbx.server.model.form.PersonFormUpdateRequest;
import kr.co.eicn.ippbx.server.model.form.PersonPasswordUpdateRequest;
import kr.co.eicn.ippbx.server.model.search.PersonSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.PersonListRepository;
import kr.co.eicn.ippbx.server.repository.eicn.PhoneInfoRepository;
import kr.co.eicn.ippbx.server.repository.eicn.UserRepository;
import kr.co.eicn.ippbx.server.service.CompanyService;
import kr.co.eicn.ippbx.server.service.OrganizationService;
import kr.co.eicn.ippbx.server.service.UserService;
import kr.co.eicn.ippbx.server.util.JsonResult;
import kr.co.eicn.ippbx.server.util.PatternUtils;
import kr.co.eicn.ippbx.server.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.PersonList.PERSON_LIST;
import static kr.co.eicn.ippbx.server.jooq.eicn.tables.PhoneInfo.PHONE_INFO;
import static kr.co.eicn.ippbx.server.util.JsonResult.Result.failure;
import static kr.co.eicn.ippbx.server.util.JsonResult.Result.success;
import static kr.co.eicn.ippbx.server.util.JsonResult.create;
import static kr.co.eicn.ippbx.server.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * 번호/그룹/사용자 > 사용자관리 > 사용자설정
 */
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/user/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserApiController extends ApiBaseController {

    private final UserRepository repository;
    private final PersonListRepository personListRepository;
    private final OrganizationService organizationService;
    private final PhoneInfoRepository phoneInfoRepository;
    private final UserService userService;
    private final CompanyService companyService;

    /**
     * 사용자 목록조회
     */
    @GetMapping("")
    public ResponseEntity<JsonResult<List<PersonSummaryResponse>>> list(PersonSearchRequest search) {

        final List<UserEntity> list = repository.findAll(search);
        final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();
        final List<PersonSummaryResponse> rows = list.stream()
                .map((e) -> {
                    final PersonSummaryResponse summary = convertDto(e, PersonSummaryResponse.class);

                    if (e.getCompanyTree() != null)
                        summary.setCompanyTrees(organizationService.getCompanyTrees(companyTrees, e.getGroupCode())
                                .stream()
                                .map(group -> convertDto(group, OrganizationSummaryResponse.class))
                                .collect(toList()));
                    return summary;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(rows));
    }

    /**
     * 사용자 목록조회
     */
    @GetMapping("search")
    public ResponseEntity<JsonResult<Pagination<PersonSummaryResponse>>> pagination(@Valid PersonSearchRequest search, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ValidationException(bindingResult);

        final Pagination<UserEntity> pagination = repository.pagination(search);
        final List<CompanyTree> companyTrees = organizationService.getAllCompanyTrees();
        final List<PersonSummaryResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final PersonSummaryResponse summary = convertDto(e, PersonSummaryResponse.class);
                    summary.setDataSearchAuthority((e.getEtc().contains("S:")) ? e.getEtc().substring(e.getEtc().lastIndexOf("S:")+2) : DataSearchAuthorityType.NONE.getCode());

                    if (e.getCompanyTree() != null)
                        summary.setCompanyTrees(organizationService.getCompanyTrees(companyTrees, e.getGroupCode())
                                .stream()
                                .map(group -> convertDto(group, OrganizationSummaryResponse.class))
                                .collect(toList()));
                    return summary;
                })
                .sorted(Comparator.comparing(PersonSummaryResponse::getIdName))
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, search.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    /**
     * 사용자 상세조회
     */
    @GetMapping("{id}")
    public ResponseEntity<JsonResult<PersonDetailResponse>> get(@PathVariable String id) {

        if(!g.getUser().getIdType().equals(IdType.MASTER.getCode()) && !g.getUser().getIdType().equals(IdType.SUPER_ADMIN.getCode()))
            if(!id.equals(g.getUser().getId()))
                throw new IllegalArgumentException("본인 정보만 열람 가능합니다.");

        final UserEntity entity = repository.findOneId(id);

        final PersonDetailResponse detail = convertDto(entity, PersonDetailResponse.class);
        detail.setDataSearchAuthority((entity.getEtc().contains("S:")) ? entity.getEtc().substring(entity.getEtc().lastIndexOf("S:")+2) : DataSearchAuthorityType.NONE.getCode());

        if (entity.getCompanyTree() != null)
            detail.setCompanyTrees(organizationService.getCompanyTrees(detail.getGroupCode())
                    .stream()
                    .map(group -> convertDto(group, OrganizationSummaryResponse.class))
                    .collect(toList()));

        return ResponseEntity.ok().body(data(detail));
    }

    /**
     * 사용자 등록
     */
    @PostMapping("")
    public ResponseEntity<JsonResult<Void>> post(@Valid @RequestBody PersonFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);
        if (StringUtils.isEmpty(form.getPassword()))
            throw new IllegalArgumentException(message.getText("messages.validator.blank", "패스워드"));

        final CompanyLicenceEntity licence = companyService.getCompanyLicenceInfo();

        System.out.println("form.getIsEmail() : "+form.getIsEmail());

        Integer pds = form.getIsPds().equals("Y") ? licence.getPdsLicense().getCurrentLicence()+1 : licence.getPdsLicense().getCurrentLicence();
        Integer stat = form.getIsStat().equals("Y") ? licence.getStatLicence().getCurrentLicence()+1 : licence.getStatLicence().getCurrentLicence();
        Integer talk = form.getIsTalk().equals("Y") ? licence.getTalkLicense().getCurrentLicence()+1 : licence.getTalkLicense().getCurrentLicence();
        Integer email = form.getIsEmail().equals("Y") ? licence.getEmailLicense().getCurrentLicence()+1 : licence.getEmailLicense().getCurrentLicence();

        if((licence.getPdsLicense().getLicence() < pds && form.getIsPds().equals("Y"))
                || (licence.getStatLicence().getLicence() < stat && form.getIsStat().equals("Y"))
                || (licence.getTalkLicense().getLicence() < talk && form.getIsTalk().equals("Y"))
                || (licence.getEmailLicense().getLicence() < email && form.getIsEmail().equals("Y")))
            throw new IllegalArgumentException("라이센스를 확인하세요.");

        repository.insert(form);
        return ResponseEntity.created(URI.create("api/v1/admin/user/user")).body(create());
    }

    /**
     * 사용자 수정
     */
    @PutMapping("{id}")
    public ResponseEntity<JsonResult<Void>> update(@Valid @RequestBody PersonFormUpdateRequest form, BindingResult bindingResult,
                                                   @PathVariable String id) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        if (!g.getUser().getIdType().equals(IdType.MASTER.getCode())
                && !g.getUser().getIdType().equals(IdType.SUPER_ADMIN.getCode())
                && !g.getUser().getIdType().equals(IdType.ADMIN.getCode())
                && !g.getUser().getId().equals(id))
            throw new IllegalArgumentException("본인 아이디만 변경 가능 합니다.");

        final CompanyLicenceEntity licence = companyService.getCompanyLicenceInfo();
        final UserEntity entity = repository.findOneId(id);
        final PersonDetailResponse detail = convertDto(entity, PersonDetailResponse.class);

        if (!g.getUser().getIdType().equals(IdType.MASTER.getCode())
                && !g.getUser().getIdType().equals(IdType.SUPER_ADMIN.getCode())
                && !g.getUser().getIdType().equals(IdType.ADMIN.getCode())
                && !entity.getIdType().equals(form.getIdType()))
            throw new IllegalArgumentException("본인 아이디만 변경 가능 합니다.");

        Integer pds = form.getIsPds().equals("Y") ? licence.getPdsLicense().getCurrentLicence()+1 : licence.getPdsLicense().getCurrentLicence();
        Integer stat = form.getIsStat().equals("Y") ? licence.getStatLicence().getCurrentLicence()+1 : licence.getStatLicence().getCurrentLicence();
        Integer talk = form.getIsTalk().equals("Y") ? licence.getTalkLicense().getCurrentLicence()+1 : licence.getTalkLicense().getCurrentLicence();
        Integer email = form.getIsEmail().equals("Y") ? licence.getEmailLicense().getCurrentLicence()+1 : licence.getEmailLicense().getCurrentLicence();
        Integer chatt = form.getIsChatt().equals("Y") ? licence.getChattLicense().getCurrentLicence()+1 : licence.getChattLicense().getCurrentLicence();

        if(detail.getIsPds().equals("Y")) pds = pds-1;
        if(detail.getIsStat().equals("Y")) stat = stat-1;
        if(detail.getIsTalk().equals("Y")) talk = talk-1;
        if(detail.getIsEmail().equals("Y")) email = email-1;
        if(detail.getIsChatt().equals("Y")) chatt = chatt-1;

        if((licence.getPdsLicense().getLicence() < pds && form.getIsPds().equals("Y"))
                || (licence.getStatLicence().getLicence() < stat && form.getIsStat().equals("Y"))
                || (licence.getTalkLicense().getLicence() < talk && form.getIsTalk().equals("Y"))
                || (licence.getEmailLicense().getLicence() < email && form.getIsEmail().equals("Y"))
                || (licence.getChattLicense().getLicence() < chatt && form.getIsChatt().equals("Y")))
            throw new IllegalArgumentException("라이센스를 확인하세요.");

        repository.updateByKey(form, id);
        return ResponseEntity.ok(create());
    }

    /**
     * 사용자 삭제
     */
    @DeleteMapping("{id}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable String id) {

        if (!g.getUser().getIdType().equals(IdType.MASTER.getCode())
                && !g.getUser().getIdType().equals(IdType.SUPER_ADMIN.getCode())
                && !g.getUser().getIdType().equals(IdType.ADMIN.getCode()))
            throw new IllegalArgumentException("삭제할 수 없습니다.");

        repository.deleteOnIfNullThrow(id);
        return ResponseEntity.ok(create());
    }

    /**
     * 사용자 등록
     */
    @PostMapping("/data-upload")
    public ResponseEntity<JsonResult<Integer>> dataUpload(@Valid @RequestBody List<PersonFormRequest> forms, BindingResult bindingResult) {
        for (PersonFormRequest form : forms) {
            if (!form.validate(bindingResult))
                throw new ValidationException(bindingResult);
        }

        if (!g.getUser().getIdType().equals(IdType.MASTER.getCode())
                && !g.getUser().getIdType().equals(IdType.SUPER_ADMIN.getCode())
                && !g.getUser().getIdType().equals(IdType.ADMIN.getCode()))
            throw new IllegalArgumentException("등록할 수 없습니다.");

        return ResponseEntity.created(URI.create("api/v1/admin/user/user/data-upload")).body(data(repository.doUpdate(forms)));
    }

    /**
     * 비밀번호 변경
     */
    @PatchMapping("{id}/password")
    public ResponseEntity<JsonResult<Void>> updatePassword(@Valid @RequestBody PersonPasswordUpdateRequest form, BindingResult bindingResult,
                                                           @PathVariable String id) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        if (!g.getUser().getIdType().equals(IdType.MASTER.getCode())
                && !g.getUser().getIdType().equals(IdType.SUPER_ADMIN.getCode())
                && !g.getUser().getIdType().equals(IdType.ADMIN.getCode())
                && !g.getUser().getId().equals(id))
            throw new IllegalArgumentException("본인 아이디만 변경 가능 합니다.");

        repository.updatePassword(id, form.getPassword());
        return ResponseEntity.ok(create());
    }

    /**
     * 아이디 중복체크
     */
    @GetMapping("{id}/duplicate")
    public ResponseEntity<JsonResult<Void>> duplicate(@PathVariable String id) {
        return ResponseEntity.ok(create(personListRepository.checkDuplicate(id) || !PatternUtils.isPatternId(id) ? failure : success));
    }

    /**
     * 내선번호 목록조회
     */
    @GetMapping("extensions")
    public ResponseEntity<JsonResult<List<SummaryPhoneInfoResponse>>> extensions(@RequestParam(required = false) String extension) {
        final List<kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PersonList> useExtensionPersons = personListRepository.findAll(
                PERSON_LIST.EXTENSION.isNotNull().and(PERSON_LIST.EXTENSION.ne(EMPTY)).and(PERSON_LIST.EXTENSION.ne("null")));
        return ResponseEntity.ok(
                data(phoneInfoRepository.findAll(PHONE_INFO.EXTENSION.isNotNull().and(PHONE_INFO.EXTENSION.ne(EMPTY))).stream()
                        .filter(e -> useExtensionPersons.stream().noneMatch(person -> {
                            if (isNotEmpty(extension))
                                if (person.getExtension().equals(extension))
                                    return false;
                            return person.getExtension().equals(e.getExtension());
                        }))
                        .sorted(Comparator.comparing(kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PhoneInfo::getExtension))
                        .map(e -> convertDto(e, SummaryPhoneInfoResponse.class))
                        .collect(Collectors.toList())
                )
        );
    }

    @GetMapping("add-persons")
    public ResponseEntity<JsonResult<List<SummaryPersonResponse>>> list() {
        return ResponseEntity.ok(data(userService.findAllUserEntity().stream()
                .map(e -> {
                    final SummaryPersonResponse response = convertDto(e, SummaryPersonResponse.class);

                    if (isNotEmpty(e.getGroupCode()))
                        response.setCompanyTrees(e.getCompanyTrees().stream().map(organization -> convertDto(organization, OrganizationSummaryResponse.class)).collect(toList()));

                    return response;
                })
                .collect(Collectors.toList())));
    }
}
