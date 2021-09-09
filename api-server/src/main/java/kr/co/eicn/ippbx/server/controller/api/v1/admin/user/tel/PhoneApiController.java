package kr.co.eicn.ippbx.server.controller.api.v1.admin.user.tel;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.SipBuddies;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo;
import kr.co.eicn.ippbx.model.dto.eicn.PhoneInfoDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PhoneInfoSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryPhoneInfoResponse;
import kr.co.eicn.ippbx.model.enums.ForwardKind;
import kr.co.eicn.ippbx.model.enums.ForwardWhen;
import kr.co.eicn.ippbx.model.enums.IdType;
import kr.co.eicn.ippbx.model.form.PhoneInfoFormRequest;
import kr.co.eicn.ippbx.model.form.PhoneInfoUpdateFormRequest;
import kr.co.eicn.ippbx.model.search.PhoneSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.*;
import kr.co.eicn.ippbx.util.EnumUtils;
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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PersonList.PERSON_LIST;
import static kr.co.eicn.ippbx.meta.jooq.eicn.tables.PhoneInfo.PHONE_INFO;
import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * 번호/그룹/사용자 > 번호/서비스관리 > 내선관리
 */
@IsAdmin
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/user/tel/phone", produces = MediaType.APPLICATION_JSON_VALUE)
public class PhoneApiController extends ApiBaseController {

    private final ExtensionRepository repository;
    private final PhoneInfoRepository phoneInfoRepository;
    private final PersonListRepository personListRepository;
    private final SipBuddiesRepository sipBuddiesRepository;
    private final Number070Repository numberRepository;

    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<PhoneInfoSummaryResponse>>> pagination(PhoneSearchRequest search) {
        final Pagination<PhoneInfo> pagination = repository.pagination(search);
        final List<PhoneInfoSummaryResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    final PhoneInfoSummaryResponse summary = convertDto(e, PhoneInfoSummaryResponse.class);
                    if (!(ForwardWhen.NONE.getCode().equals(e.getForwardWhen())) && isNotEmpty(e.getForwarding())
                                && e.getForwarding().length() >= 5) {
                        final String[] split = split(e.getForwarding(), "|");
                        summary.setFwWhen(message.getEnumCodeText(Objects.requireNonNull(EnumUtils.of(ForwardWhen.class, e.getForwardWhen()))));
                        summary.setFwKind(message.getEnumCodeText(Objects.requireNonNull(EnumUtils.of(ForwardKind.class, split[0]))));
                        summary.setFwNum(split[1]);
                    }
                    return summary;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    @GetMapping("{peer}")
    public ResponseEntity<JsonResult<PhoneInfoDetailResponse>> get(@PathVariable String peer) {
        final PhoneInfo entity = repository.findOneIfNullThrow(peer);
        final PhoneInfoDetailResponse detail = convertDto(entity, PhoneInfoDetailResponse.class);

        if (!(ForwardWhen.NONE.getCode().equals(entity.getForwardWhen())) && isNotEmpty(entity.getForwarding())
                && entity.getForwarding().length() >= 5) {
            final String[] split = split(entity.getForwarding(), "|");
            detail.setFwWhen(message.getEnumCodeText(Objects.requireNonNull(EnumUtils.of(ForwardWhen.class, entity.getForwardWhen()))));
            detail.setFwKind(message.getEnumCodeText(Objects.requireNonNull(EnumUtils.of(ForwardKind.class, split[0]))));
            detail.setFwNum(split[1]);
        }

        final kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SipBuddies sipBuddies = sipBuddiesRepository.findOne(SipBuddies.SIP_BUDDIES.NAME.eq(detail.getPeer()));
        if (sipBuddies != null)
            detail.setPasswd(sipBuddies.getMd5secret());

        detail.setNumber070(numberRepository.findOne(entity.getVoipTel()));

        return ResponseEntity.ok(data(detail));
    }

    @PostMapping("")
    public ResponseEntity<JsonResult<Void>> register(@Valid @RequestBody PhoneInfoFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        if (!g.getUser().getIdType().equals(IdType.MASTER.getCode())
                && !g.getUser().getIdType().equals(IdType.SUPER_ADMIN.getCode())
                && !g.getUser().getIdType().equals(IdType.ADMIN.getCode()))
            throw new IllegalArgumentException("추가할 수 없습니다.");

        repository.insert(form);

        return ResponseEntity.created(URI.create("/api/v1/admin/user/tel/phone")).body(create());
    }

    @PutMapping("{peer}")
    public ResponseEntity<JsonResult<?>> update(@Valid @RequestBody PhoneInfoUpdateFormRequest form, BindingResult bindingResult, @PathVariable String peer) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        if (!g.getUser().getIdType().equals(IdType.MASTER.getCode())
                && !g.getUser().getIdType().equals(IdType.SUPER_ADMIN.getCode())
                && !g.getUser().getIdType().equals(IdType.ADMIN.getCode()))
            throw new IllegalArgumentException("수정할 수 없습니다.");

        final PhoneSearchRequest search = new PhoneSearchRequest();
        search.setExtension(form.getExtension());
        final Pagination<PhoneInfo> pagination = repository.pagination(search);
        if (pagination.getTotalCount() > 0)
            throw new IllegalArgumentException("중복내선 입니다.");

        repository.update(form);
        return ResponseEntity.ok(create());
    }

    @DeleteMapping("{peer}")
    public ResponseEntity<JsonResult<Void>> delete(@PathVariable String peer) {
        if (!g.getUser().getIdType().equals(IdType.MASTER.getCode())
                && !g.getUser().getIdType().equals(IdType.SUPER_ADMIN.getCode())
                && !g.getUser().getIdType().equals(IdType.ADMIN.getCode()))
            throw new IllegalArgumentException("삭제할 수 없습니다.");

        repository.deleteWithForeign(peer);

        return ResponseEntity.ok(create());
    }

    /**
     * 내선번호 목록조회
     */
    @GetMapping("add-extensions")
    public ResponseEntity<JsonResult<List<SummaryPhoneInfoResponse>>> addExtensions(@RequestParam(required = false) String extension) {
        final List<kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PersonList> useExtensionPersons = personListRepository.findAll(
                PERSON_LIST.EXTENSION.isNotNull().and(PERSON_LIST.EXTENSION.ne(EMPTY)).and(PERSON_LIST.EXTENSION.ne("null")));
        return ResponseEntity.ok(
                data(phoneInfoRepository.findAll(PHONE_INFO.EXTENSION.isNotNull().and(PHONE_INFO.EXTENSION.ne(EMPTY))).stream()
                        .filter(e -> useExtensionPersons.stream().noneMatch(person -> {
                            if (isNotEmpty(extension))
                                if (person.getExtension().equals(extension))
                                    return false;
                            return person.getExtension().equals(e.getExtension());
                        }))
                        .sorted(Comparator.comparing(kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo::getExtension))
                        .map(e -> convertDto(e, SummaryPhoneInfoResponse.class))
                        .collect(Collectors.toList())
                )
        );
    }
}
