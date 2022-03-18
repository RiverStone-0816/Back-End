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
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@AllArgsConstructor
@Service
public class MultichannelService extends ApiServerInterface {
    private static final String subUrl = "https://dev.eicn.co.kr/ipcc/multichannel/remote/session_check.jsp;jsessionid=";

    protected RequestGlobal g;
    private final UserApiInterface userApiInterface;
    private final AuthApiInterface authApiInterface;
    private final CompanyApiInterface companyApiInterface;

    public MultichannelLoginResponse checkSession(String jSessionId) throws IOException {
        return get(subUrl.concat(jSessionId), null, MultichannelLoginResponse.class);
    }

    public void ivrLogin(String jSessionId) throws IOException, ResultFailException {
        if (!g.isLogin() || !g.checkLogin()) {
            MultichannelLoginResponse response = checkSession(jSessionId);
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
