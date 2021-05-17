package kr.co.eicn.ippbx.front.service.api.email;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.EmailMngDetailResponse;
import kr.co.eicn.ippbx.server.model.form.EmailMngFormRequest;
import kr.co.eicn.ippbx.server.model.search.EmailMngSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(EmailApiInterface.class);

    private static final String subUrl = "/api/v1/admin/email/mng/";

    public Pagination<EmailMngDetailResponse> pagination(EmailMngSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, EmailMngDetailResponse.class).getData();
    }

    public EmailMngDetailResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, EmailMngDetailResponse.class).getData();
    }

    public void post(EmailMngFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void put(Integer seq, EmailMngFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }
}
