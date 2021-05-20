package kr.co.eicn.ippbx.server.controller.api;

import kr.co.eicn.ippbx.exception.ValidationException;
import kr.co.eicn.ippbx.meta.jooq.configdb.tables.pojos.CommonMenuCompany;
import kr.co.eicn.ippbx.model.dto.configdb.MenuCompanyResponse;
import kr.co.eicn.ippbx.model.dto.configdb.UserMenuCompanyResponse;
import kr.co.eicn.ippbx.model.form.MenuFormRequest;
import kr.co.eicn.ippbx.model.form.UserMenuSequenceUpdateRequest;
import kr.co.eicn.ippbx.server.service.MenuCompanyService;
import kr.co.eicn.ippbx.util.JsonResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * 메뉴 API
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/menu", produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuCompanyApiController extends ApiBaseController {

    private final MenuCompanyService service;

    /**
     * 고객사별 메뉴 목록 조회
     */
    @GetMapping("company")
    public ResponseEntity<JsonResult<List<MenuCompanyResponse>>> getCompanyMenus() {
        return ResponseEntity.ok(data(service.getCompanyMenus().stream().map((e) -> convertDto(e, MenuCompanyResponse.class)).collect(Collectors.toList())));
    }

    /**
     * 사용자 권한에 따른 메뉴 목록 조회
     */
    @GetMapping("{userid}/user")
    public ResponseEntity<JsonResult<List<UserMenuCompanyResponse>>> getUserMenus(@PathVariable String userid) {
        return ResponseEntity.ok(data(service.getUserMenus(userid)));
    }

    /**
     * 메뉴 순서 목록
     */
    @GetMapping("{userid}/user-parent-menu")
    public ResponseEntity<JsonResult<List<UserMenuCompanyResponse>>> getParentMenu(@PathVariable String userid) {
        return ResponseEntity.ok(data(service.getParentMenu(userid)));
    }

    /**
     * 서브 메뉴 순서 목록
     */
    @GetMapping("{userid}/{parentMenuCode}")
    public ResponseEntity<JsonResult<UserMenuCompanyResponse>> getChildrenMenuByParentMenu(@PathVariable String userid, @PathVariable String parentMenuCode) {
        return ResponseEntity.ok(data(service.getChildrenMenuByParentMenu(userid, parentMenuCode)));
    }

    /**
     * 서브메뉴 조회
     */
    @GetMapping("{menuCode}/user-menu")
    public ResponseEntity<JsonResult<CommonMenuCompany>> getMenuByMenuCode(@PathVariable String menuCode) {
        return ResponseEntity.ok(data(service.getMenuByMenuCode(menuCode)));
    }

    @PutMapping(value = "{userid}/{menuCode}")
    public ResponseEntity<JsonResult<Integer>> updateOrInsertUserMenu(@PathVariable String userid, @PathVariable String menuCode,
                                                                   @Valid @RequestBody MenuFormRequest form, BindingResult bindingResult) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        service.getRepository().updateByKeyOrInsertByMenuCompany(form, menuCode, userid);
        return ResponseEntity.ok(create());
    }

    //메뉴 순서 변경
    @PutMapping(value = "{userid}/user-menu-sequence")
    public ResponseEntity<JsonResult<Void>> menuSequence(@Valid @RequestBody UserMenuSequenceUpdateRequest form, BindingResult bindingResult,
                                                             @PathVariable String userid) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        service.getRepository().updateMenuSequence(form, userid);
        return ResponseEntity.ok(create());
    }

    //서브 메뉴 순서 변경
    @PutMapping(value = "{userid}/user-menu-sequence/{parentMenuCode}")
    public ResponseEntity<JsonResult<Void>> userMenuSequence(@Valid @RequestBody UserMenuSequenceUpdateRequest form, BindingResult bindingResult,
                                                             @PathVariable String parentMenuCode, @PathVariable String userid) {
        if (!form.validate(bindingResult))
            throw new ValidationException(bindingResult);

        service.getRepository().updateSubMenuSequence(form, parentMenuCode, userid);
        return ResponseEntity.ok(create());
    }

    //초기화
    @PutMapping(value = "{userid}/reset")
    public ResponseEntity<JsonResult<Void>> reset(@PathVariable String userid) {
        service.getRepository().menuReset(userid);
        return ResponseEntity.ok(create());
    }
}
