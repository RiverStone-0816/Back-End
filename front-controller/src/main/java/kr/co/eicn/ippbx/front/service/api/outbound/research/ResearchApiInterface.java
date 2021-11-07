package kr.co.eicn.ippbx.front.service.api.outbound.research;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ResearchTree;
import kr.co.eicn.ippbx.model.dto.eicn.ResearchListResponse;
import kr.co.eicn.ippbx.model.entity.eicn.ResearchListEntity;
import kr.co.eicn.ippbx.model.form.ResearchListFormRequest;
import kr.co.eicn.ippbx.model.form.ResearchTreeFormRequest;
import kr.co.eicn.ippbx.model.search.ResearchListSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ResearchApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(ResearchApiInterface.class);
    private static final String subUrl = "/api/v1/admin/outbound/research/";

    public Pagination<ResearchListResponse> pagination(ResearchListSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, ResearchListResponse.class).getData();
    }

    public ResearchListResponse get(Integer researchId) throws IOException, ResultFailException {
        return getData(subUrl + researchId, null, ResearchListResponse.class).getData();
    }

    public void register(ResearchListFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void update(Integer researchId, ResearchListFormRequest form) throws IOException, ResultFailException {
        put(subUrl + researchId, form);
    }

    public void delete(Integer researchId) throws IOException, ResultFailException {
        delete(subUrl + researchId);
    }

    public ResearchListEntity getScenario(Integer researchId) throws IOException, ResultFailException {
        return getData(subUrl + "scenario/" + researchId, null, ResearchListEntity.class).getData();
    }

    public void scenarioRegister(Integer researchId, ResearchTreeFormRequest form) throws IOException, ResultFailException {
        post(subUrl + researchId + "/scenario", form);
    }

    public List<ResearchTree> getTrees(Integer researchId) throws IOException, ResultFailException {
        return getList(subUrl + researchId + "/trees", null, ResearchTree.class).getData();
    }
}
