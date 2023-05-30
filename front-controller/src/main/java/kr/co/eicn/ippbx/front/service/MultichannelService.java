package kr.co.eicn.ippbx.front.service;

import kr.co.eicn.ippbx.front.config.RequestGlobal;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.service.api.AuthApiInterface;
import kr.co.eicn.ippbx.front.service.api.CompanyApiInterface;
import kr.co.eicn.ippbx.front.service.api.user.user.UserApiInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CompanyInfo;
import kr.co.eicn.ippbx.model.dto.eicn.MultichannelLoginResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PersonDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PersonListSummary;
import kr.co.eicn.ippbx.util.ResultFailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class MultichannelService extends ApiServerInterface {

    private final RequestGlobal g;
    private final UserApiInterface userApiInterface;
    private final AuthApiInterface authApiInterface;
    private final CompanyApiInterface companyApiInterface;

    public void mcLogin(String companyId, String userId) throws IOException, ResultFailException {
        if (!g.isLogin() || !g.checkLogin() || !companyId.equals(g.getUser().getCompanyId()) || !userId.equals(g.getUser().getId())) {
            g.invalidateSession();

            MultichannelLoginResponse response = new MultichannelLoginResponse(companyId, userId);
            logger.info(response.toString());

            PersonListSummary data = new PersonListSummary();

            data.setId(response.getId());
            data.setCompanyId(response.getCompanyId());

            authApiInterface.login(data);

            PersonDetailResponse user = userApiInterface.get(response.getId());
            g.setCurrentUser(user);

            CompanyInfo companyInfo = companyApiInterface.getInfo(response.getCompanyId());
            g.setUsingServices(companyInfo.getService());
            g.setLoginConfirm(true);
        }
    }
}
