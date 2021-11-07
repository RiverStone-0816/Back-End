package kr.co.eicn.ippbx.front.service.api;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.meta.jooq.configdb.tables.pojos.CommonMenuCompany;
import kr.co.eicn.ippbx.model.dto.configdb.UserMenuCompanyResponse;
import kr.co.eicn.ippbx.model.form.MenuFormRequest;
import kr.co.eicn.ippbx.model.form.UserMenuSequenceUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MenuApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(MenuApiInterface.class);
    private static final String subUrl = "/api/menu/";

    public List<UserMenuCompanyResponse> getUserMenus(String userId) throws IOException, ResultFailException {
        return getList(subUrl + userId + "/user", null, UserMenuCompanyResponse.class).getData();
    }

    public void reset(String userId) throws IOException, ResultFailException {
        put(subUrl + userId + "/reset", null);
    }

    public void userMenuSequence(String userId, UserMenuSequenceUpdateRequest form) throws IOException, ResultFailException {
        put(subUrl + userId + "/user-menu-sequence", form);
    }

    public void userMenuSequence(String userId, String parentMenuCode, UserMenuSequenceUpdateRequest form) throws IOException, ResultFailException {
        put(subUrl + userId + "/user-menu-sequence/" + parentMenuCode, form);
    }

    public void UpdateOrInsertUserMenu(String userId, String menuCode, MenuFormRequest form) throws IOException, ResultFailException {
        put(subUrl + userId + "/" + menuCode, form);
    }

    public UserMenuCompanyResponse getChildrenMenuByParentMenu(String userId, String menuCode) throws IOException, ResultFailException {
        return getData(subUrl + userId + "/" + menuCode, null, UserMenuCompanyResponse.class).getData();
    }

    public CommonMenuCompany getMenuByMenuCode(String menuCode) throws IOException, ResultFailException {
        return getData(subUrl + "/" + menuCode + "/user-menu", null, CommonMenuCompany.class).getData();
    }
}
