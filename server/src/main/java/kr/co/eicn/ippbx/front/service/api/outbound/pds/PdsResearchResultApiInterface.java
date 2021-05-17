package kr.co.eicn.ippbx.front.service.api.outbound.pds;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.HistoryPdsGroup;
import kr.co.eicn.ippbx.server.model.entity.pds.PdsResearchResultEntity;
import kr.co.eicn.ippbx.server.model.search.HistoryPdsGroupSearchRequest;
import kr.co.eicn.ippbx.server.model.search.PDSResearchResultSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PdsResearchResultApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(PdsResearchResultApiInterface.class);
    private static final String subUrl = "/api/v1/admin/outbound/pds/research-result-history/";

    public List<PdsResearchResultEntity> getList(String executeId, PDSResearchResultSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl + executeId + "/data", search, PdsResearchResultEntity.class).getData();
    }

    public List<HistoryPdsGroup> addExecuteLists(HistoryPdsGroupSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "add-execute", search, HistoryPdsGroup.class).getData();
    }
}
