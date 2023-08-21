package kr.co.eicn.ippbx.front.service.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AdminCheckApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(AdminCheckApiInterface.class);

    private static final String subUrl = "/api/v1/admin/check/";

    public void check() throws Exception {
        get(subUrl, null, String.class);
    }
}
