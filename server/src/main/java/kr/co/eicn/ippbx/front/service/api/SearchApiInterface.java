package kr.co.eicn.ippbx.front.service.api;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.server.model.dto.eicn.search.*;
import kr.co.eicn.ippbx.server.model.search.search.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SearchApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(SearchApiInterface.class);

    private static final String subUrl = "/api/v1/search/";

    public List<SearchNumber070Response> numbers(SearchNumber070Request search) throws IOException, ResultFailException {
        return getList(subUrl + "number", search, SearchNumber070Response.class).getData();
    }

    public List<SearchCidResponse> cids(SearchCidRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "cid", search, SearchCidResponse.class).getData();
    }

    public List<SearchGwInfoResponse> gateways(SearchGwInfoRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "gateway", search, SearchGwInfoResponse.class).getData();
    }

    public List<SearchPhoneInfoResponse> phones(SearchPhoneInfoRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "phone", search, SearchPhoneInfoResponse.class).getData();
    }

    public List<SearchQueueNameResponse> queues(SearchQueueNameRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "queue", search, SearchQueueNameResponse.class).getData();
    }

    public List<SearchServiceResponse> services(SearchServiceRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "service", search, SearchServiceResponse.class).getData();
    }

    public List<SearchPDSGroupResponse> pdsGroup() throws IOException, ResultFailException {
        return getList(subUrl + "pds-group", null, SearchPDSGroupResponse.class).getData();
    }
}
