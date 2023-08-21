package kr.co.eicn.ippbx.front.service.api.user.tel;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.ServiceListDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ServiceListSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryServiceListResponse;
import kr.co.eicn.ippbx.model.form.ServiceListFormRequest;
import kr.co.eicn.ippbx.model.form.ServiceListFormUpdateRequest;
import kr.co.eicn.ippbx.model.search.ServiceListSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ServiceApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(ServiceApiInterface.class);

    private static final String subUrl = "/api/v1/admin/user/tel/service/";

    public List<ServiceListSummaryResponse> list(ServiceListSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, ServiceListSummaryResponse.class).getData();
    }

    public List<ServiceListSummaryResponse> listCounsel(ServiceListSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "counsel", search, ServiceListSummaryResponse.class).getData();
    }

    public ServiceListDetailResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, ServiceListDetailResponse.class).getData();
    }

    public Integer post(ServiceListFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    public void update(Integer seq, ServiceListFormUpdateRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }

    public List<SummaryServiceListResponse> addServices() throws IOException, ResultFailException {
        return getList(subUrl + "add-services", null, SummaryServiceListResponse.class).getData();
    }
}
