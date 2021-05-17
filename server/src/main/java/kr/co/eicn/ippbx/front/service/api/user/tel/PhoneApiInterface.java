package kr.co.eicn.ippbx.front.service.api.user.tel;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.LicenseInfo;
import kr.co.eicn.ippbx.server.model.dto.eicn.PhoneInfoDetailResponse;
import kr.co.eicn.ippbx.server.model.dto.eicn.PhoneInfoSummaryResponse;
import kr.co.eicn.ippbx.server.model.form.PhoneInfoFormRequest;
import kr.co.eicn.ippbx.server.model.form.PhoneInfoUpdateFormRequest;
import kr.co.eicn.ippbx.server.model.search.PhoneSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PhoneApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(PhoneApiInterface.class);

    private static final String subUrl = "/api/v1/admin/user/tel/phone/";

    public Pagination<PhoneInfoSummaryResponse> pagination(PhoneSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, PhoneInfoSummaryResponse.class).getData();
    }

    public PhoneInfoDetailResponse get(String peer) throws IOException, ResultFailException {
        return getData(subUrl + peer, null, PhoneInfoDetailResponse.class).getData();
    }

    public void post(PhoneInfoFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void update(String peer, PhoneInfoUpdateFormRequest request) throws IOException, ResultFailException {
        super.put(subUrl + peer, request);
    }

    public void delete(String peer) throws IOException, ResultFailException {
        super.delete(subUrl + peer);
    }

}
