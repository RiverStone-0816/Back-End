package kr.co.eicn.ippbx.server.controller.api.v1.admin.service.etc;

import kr.co.eicn.ippbx.server.controller.api.ApiBaseController;
import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.PhoneInfo;
import kr.co.eicn.ippbx.model.dto.eicn.CidInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.NumberListResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PhoneInfoResponse;
import kr.co.eicn.ippbx.model.enums.DialStatus;
import kr.co.eicn.ippbx.model.enums.FirstStatus;
import kr.co.eicn.ippbx.model.enums.LogoutStatus;
import kr.co.eicn.ippbx.model.form.CidInfoChangeRequest;
import kr.co.eicn.ippbx.model.form.CidInfoUpdateFormRequest;
import kr.co.eicn.ippbx.model.search.CidInfoSearchRequest;
import kr.co.eicn.ippbx.server.repository.eicn.CidRepository;
import kr.co.eicn.ippbx.server.repository.eicn.Number070Repository;
import kr.co.eicn.ippbx.server.repository.eicn.PhoneInfoRepository;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static kr.co.eicn.ippbx.util.JsonResult.create;
import static kr.co.eicn.ippbx.util.JsonResult.data;

/**
 * 서비스운영관리 > 기타관리 > 내선기타정보설정
 **/
@Log4j2
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/admin/service/etc/extension", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidInfoApiController extends ApiBaseController {
    private final PhoneInfoRepository phoneInfoRepository;
    private final CidRepository cidRepository;
    private final Number070Repository numberRepository;

    /**
     * 내선목록
     **/
    @GetMapping("")
    public ResponseEntity<JsonResult<Pagination<PhoneInfoResponse>>> pagination(CidInfoSearchRequest search) {
        final Pagination<PhoneInfo> pagination = phoneInfoRepository.pagination(search);
        final List<PhoneInfoResponse> rows = pagination.getRows().stream()
                .map((e) -> {
                    PhoneInfoResponse entity = convertDto(e, PhoneInfoResponse.class);
                    entity.setFirstStatus(FirstStatus.of(e.getFirstStatus()));
                    entity.setDialStatus(DialStatus.of(e.getDialStatus()));
                    entity.setLogoutStatus(LogoutStatus.of(e.getLogoutStatus()));

                    return entity;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(data(new Pagination<>(rows, pagination.getPage(), pagination.getTotalCount(), search.getLimit())));
    }

    /**
     * 내선정보수정
     **/
    @PutMapping("")
    public ResponseEntity<JsonResult<Void>> put(@Valid @RequestBody CidInfoUpdateFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        phoneInfoRepository.updatePage(form);

        return ResponseEntity.ok(create());
    }

    /**
     * cid 번호수정
     **/
    @PutMapping("cid")
    public ResponseEntity<JsonResult<Void>> putCid(@Valid @RequestBody CidInfoChangeRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        phoneInfoRepository.updateCid(form);
        return ResponseEntity.ok(create());
    }

    /**
     * cid 목록
     **/
    @GetMapping("cids")
    public ResponseEntity<JsonResult<List<CidInfoResponse>>> cidList() {
        return ResponseEntity.ok(data(
                cidRepository.findAll().stream()
                        .map(cid -> convertDto(cid, CidInfoResponse.class))
                        .collect(Collectors.toList())
        ));
    }

    /**
     * 과금번호 목록
     **/
    @GetMapping("numbers")
    public ResponseEntity<JsonResult<List<NumberListResponse>>> numberList() {
        return ResponseEntity.ok(data(
                numberRepository.findAll().stream()
                        .map(cid -> convertDto(cid, NumberListResponse.class))
                        .collect(Collectors.toList())
        ));
    }
}
