package kr.co.eicn.ippbx.front.service.api.user.user;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.form.PersonMePasswordUpdateRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UserMeApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(UserMeApiInterface.class);

    private static final String subUrl = "/api/v1/me/";

    public void updatePassword(PersonMePasswordUpdateRequest form) throws IOException, ResultFailException {
        patch(subUrl + "password", form);
    }
}
