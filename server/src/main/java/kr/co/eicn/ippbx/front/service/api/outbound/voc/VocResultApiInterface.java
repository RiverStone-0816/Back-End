package kr.co.eicn.ippbx.front.service.api.outbound.voc;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.entity.customdb.VocResearchResultEntity;
import kr.co.eicn.ippbx.server.model.search.CustomDBVOCResultSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class VocResultApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(VocResultApiInterface.class);
    private static final String subUrl = "/api/v1/admin/outbound/voc/result-history/";

    public Pagination<VocResearchResultEntity> pagination(CustomDBVOCResultSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, VocResearchResultEntity.class).getData();
    }
}
