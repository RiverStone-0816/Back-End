package kr.co.eicn.ippbx.front.service.api.acd.route;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.CsRouteGetResponse;
import kr.co.eicn.ippbx.model.dto.eicn.CsRouteSummaryResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchQueueResponse;
import kr.co.eicn.ippbx.model.form.CsRouteFormRequest;
import kr.co.eicn.ippbx.model.search.CsRouteSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CsRouteApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(CsRouteApiInterface.class);
    private static final String subUrl = "/api/v1/admin/acd/route/cs/";

    public Pagination<CsRouteSummaryResponse> pagination(CsRouteSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, CsRouteSummaryResponse.class).getData();
    }

    public List<SearchQueueResponse> queue() throws IOException, ResultFailException {
        return getList(subUrl + "queue", null, SearchQueueResponse.class).getData();
    }

    public CsRouteGetResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, CsRouteGetResponse.class).getData();
    }

    public Integer post(CsRouteFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    public void put(Integer seq, CsRouteFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }

}
