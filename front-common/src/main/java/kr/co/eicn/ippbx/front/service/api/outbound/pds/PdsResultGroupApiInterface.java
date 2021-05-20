package kr.co.eicn.ippbx.front.service.api.outbound.pds;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.form.PDSResultGroupFormRequest;
import kr.co.eicn.ippbx.model.form.PDSResultGroupUpdateFormRequest;
import kr.co.eicn.ippbx.model.search.PDSResultGroupSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class PdsResultGroupApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(PdsResultGroupApiInterface.class);
    private static final String subUrl = "/api/v1/admin/outbound/pds/result-group/";

    public Pagination<PDSResultGroupSummaryResponse> pagination(PDSResultGroupSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, PDSResultGroupSummaryResponse.class).getData();
    }

    public PDSResultGroupDetailResponse get(String name) throws IOException, ResultFailException {
        return getData(subUrl + name, null, PDSResultGroupDetailResponse.class).getData();
    }

    public String post(PDSResultGroupFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, String.class, false).getData();
    }

    public void update(String name, PDSResultGroupUpdateFormRequest form) throws IOException, ResultFailException {
        super.put(subUrl + name, form);
    }

    public void delete(String name) throws IOException, ResultFailException {
        super.delete(subUrl + name);
    }

    public List<SummaryCompanyServerResponse> addServerLists() throws IOException, ResultFailException {
        return getList(subUrl + "add-server", null, SummaryCompanyServerResponse.class).getData();
    }

    public List<SummaryContextInfoResponse> context() throws IOException, ResultFailException {
        return getList(subUrl + "context", null, SummaryContextInfoResponse.class).getData();
    }

    public List<SummaryPersonResponse> addOnPersons(String name) throws IOException, ResultFailException {
        return getList(subUrl + "add-on-persons", Collections.singletonMap("name", name), SummaryPersonResponse.class).getData();
    }
}
