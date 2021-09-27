package kr.co.eicn.ippbx.front.service.api;

import kr.co.eicn.ippbx.front.model.ArsAuthInfo;
import kr.co.eicn.ippbx.front.model.form.LoginForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.SipBuddies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AuthApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(AuthApiInterface.class);

    public void login(LoginForm form) throws IOException, ResultFailException {
        final String accessToken = getData(HttpMethod.POST, "/auth/authenticate", form, String.class, false).getData();
        setAccessToken(accessToken);
    }

    public void logout() throws IOException, ResultFailException {
        getResult("/auth/logout", null);
    }

    public ArsAuthInfo getArsAuth(String userId) throws IOException, ResultFailException {
        return getData("/api/ars-auth/" + userId, null, ArsAuthInfo.class).getData();
    }

    public SipBuddies getSoftPhoneAuth(String peer) throws IOException, ResultFailException {
        return getData("/api/softphone-auth/" + peer, null, SipBuddies.class).getData();
    }

    public void update(String peer, String secret)  throws IOException, ResultFailException {
        put("/api/softphone-auth/" + peer + "/" + secret , null);
    }

    public String getAccessToken() {
        return super.getAccessToken();
    }
}
