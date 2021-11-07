package kr.co.eicn.ippbx.front.controller.api;

import kr.co.eicn.ippbx.front.controller.BaseController;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.MenuApiInterface;
import kr.co.eicn.ippbx.model.dto.configdb.UserMenuCompanyResponse;
import kr.co.eicn.ippbx.model.form.MenuFormRequest;
import kr.co.eicn.ippbx.model.form.UserMenuSequenceUpdateRequest;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @author tinywind
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "api/menu", produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuApiController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(MenuApiController.class);

    private final MenuApiInterface apiInterface;

    @GetMapping("{userId}/user")
    public List<UserMenuCompanyResponse> getUserMenus(@PathVariable String userId) throws IOException, ResultFailException {
        return apiInterface.getUserMenus(userId);
    }

    @PutMapping(value = "{userId}/reset")
    public void reset(@PathVariable String userId) throws IOException, ResultFailException {
        apiInterface.reset(userId);
    }

    @PutMapping("{userId}/user-menu-sequence")
    public void userMenuSequence(@Valid @RequestBody UserMenuSequenceUpdateRequest form, BindingResult bindingResult, @PathVariable String userId) throws IOException, ResultFailException {
        apiInterface.userMenuSequence(userId, form);
    }

    @PutMapping("{userId}/user-menu-sequence/{parentMenuCode}")
    public void userMenuSequence(@Valid @RequestBody UserMenuSequenceUpdateRequest form, BindingResult bindingResult, @PathVariable String parentMenuCode, @PathVariable String userId) throws IOException, ResultFailException {
        apiInterface.userMenuSequence(userId, parentMenuCode, form);
    }

    @PutMapping("{userId}/{menuCode}")
    public void updateOrInsertUserMenu(@PathVariable String userId, @PathVariable String menuCode, @Valid @RequestBody MenuFormRequest form, BindingResult bindingResult) throws IOException, ResultFailException {
        apiInterface.UpdateOrInsertUserMenu(userId, menuCode, form);
    }

    @GetMapping("{userId}/{menuCode}")
    public UserMenuCompanyResponse getChildrenMenuByParentMenu(@PathVariable String userId, @PathVariable String menuCode) throws IOException, ResultFailException {
        return apiInterface.getChildrenMenuByParentMenu(userId, menuCode);
    }
}
