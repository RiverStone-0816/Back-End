package kr.co.eicn.ippbx.front.service.api.user.user;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.PickUpGroupDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PickUpGroupSummaryResponse;
import kr.co.eicn.ippbx.model.form.PickUpGroupFormRequest;
import kr.co.eicn.ippbx.model.form.PickUpGroupFormUpdateRequest;
import kr.co.eicn.ippbx.model.search.PickUpGroupSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PickupGroupApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(PickupGroupApiInterface.class);

    private static final String subUrl = "/api/v1/admin/user/user/pickup-group/";

    public Pagination<PickUpGroupSummaryResponse> pagination(PickUpGroupSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, PickUpGroupSummaryResponse.class).getData();
    }

    public PickUpGroupDetailResponse get(Integer groupcode) throws IOException, ResultFailException {
        return getData(subUrl + groupcode, null, PickUpGroupDetailResponse.class).getData();
    }

    public Integer post(PickUpGroupFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    public void update(Integer groupcode, PickUpGroupFormUpdateRequest form) throws IOException, ResultFailException {
        put(subUrl + groupcode, form);
    }

    public void delete(Integer groupcode) throws IOException, ResultFailException {
        delete(subUrl + groupcode);
    }
}
