package kr.co.eicn.ippbx.front.service.api.acd.grade;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.dto.eicn.search.SearchPersonListResponse;
import kr.co.eicn.ippbx.server.model.entity.eicn.RouteApplicationEntity;
import kr.co.eicn.ippbx.server.model.form.RAFormUpdateRequest;
import kr.co.eicn.ippbx.server.model.form.RouteApplicationFormRequest;
import kr.co.eicn.ippbx.server.model.search.RouteApplicationSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RouteAppApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(RouteAppApiInterface.class);
    private static final String subUrl = "/api/v1/admin/acd/grade/routeapp/";

    public Pagination<RouteApplicationEntity> pagination(RouteApplicationSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, RouteApplicationEntity.class).getData();
    }

    public List<SearchPersonListResponse> person() throws IOException, ResultFailException {
        return getList(subUrl + "person", null, SearchPersonListResponse.class).getData();
    }

    public RouteApplicationEntity get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, RouteApplicationEntity.class).getData();
    }

    public void post(RouteApplicationFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void accept(Integer seq, RAFormUpdateRequest form) throws IOException, ResultFailException {
        put(subUrl + seq + "/accept", form);
    }

    public void reject(Integer seq) throws IOException, ResultFailException {
        put(subUrl + seq + "/reject", null);
    }
}
