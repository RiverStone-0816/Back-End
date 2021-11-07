package kr.co.eicn.ippbx.front.service.api.outbound.research;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.ResearchItem;
import kr.co.eicn.ippbx.model.dto.eicn.ResearchItemResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummaryResearchItemResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SummarySoundListResponse;
import kr.co.eicn.ippbx.model.form.ResearchItemFormRequest;
import kr.co.eicn.ippbx.model.search.ResearchItemSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ResearchItemApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(ResearchItemApiInterface.class);
    private static final String subUrl = "/api/v1/admin/outbound/research/item/";

    public List<ResearchItem> list(ResearchItemSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, ResearchItem.class).getData();
    }

    public Pagination<ResearchItemResponse> pagination(ResearchItemSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl + "search", search, ResearchItemResponse.class).getData();
    }

    public ResearchItemResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, ResearchItemResponse.class).getData();
    }

    public void register(ResearchItemFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void update(Integer seq, ResearchItemFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }

    public List<SummarySoundListResponse> addSounds() throws IOException, ResultFailException {
        return getList(subUrl + "add-sounds", null, SummarySoundListResponse.class).getData();
    }

    public List<SummaryResearchItemResponse> addResearchItem() throws IOException, ResultFailException {
        return getList(subUrl + "add-research-item", null, SummaryResearchItemResponse.class).getData();
    }
}
