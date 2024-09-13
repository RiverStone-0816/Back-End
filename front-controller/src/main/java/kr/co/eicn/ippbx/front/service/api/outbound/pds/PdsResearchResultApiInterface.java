package kr.co.eicn.ippbx.front.service.api.outbound.pds;

import kr.co.eicn.ippbx.model.dto.eicn.HistoryPdsResearchGroupResponse;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.entity.pds.PdsResearchResultEntity;
import kr.co.eicn.ippbx.model.search.PDSResearchResultSearchRequest;
import kr.co.eicn.ippbx.util.page.Pagination;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class PdsResearchResultApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(PdsResearchResultApiInterface.class);
    private static final String subUrl = "/api/v1/admin/outbound/pds/research-result-history/";

    public Pagination<PdsResearchResultEntity> pagination(String executeId, PDSResearchResultSearchRequest search) throws IOException, ResultFailException {
        final Map<String, Object> param = objectMapper.convertValue(search, typeFactory.constructParametricType(Map.class, String.class, Object.class));

        // FIXME: extract
        param.remove("dbTypeFields");
        search.getDbTypeFields().forEach((key, condition) -> {
            if (StringUtils.isNotEmpty(condition.getKeyword()))
                param.put("dbTypeFields[" + key + "].keyword", condition.getKeyword());
            if (StringUtils.isNotEmpty(condition.getCode()))
                param.put("dbTypeFields[" + key + "].code", condition.getCode());
            if (condition.getStartDate() != null)
                param.put("dbTypeFields[" + key + "].startDate", condition.getStartDate().toString());
            if (condition.getEndDate() != null)
                param.put("dbTypeFields[" + key + "].endDate", condition.getEndDate().toString());
        });

        return getPagination(subUrl + executeId + "/pagination", param, PdsResearchResultEntity.class).getData();
    }

    public List<PdsResearchResultEntity> list(String executeId, PDSResearchResultSearchRequest search) throws IOException, ResultFailException {
        final Map<String, Object> param = objectMapper.convertValue(search, typeFactory.constructParametricType(Map.class, String.class, Object.class));

        // FIXME: extract
        param.remove("dbTypeFields");
        search.getDbTypeFields().forEach((key, condition) -> {
            if (StringUtils.isNotEmpty(condition.getKeyword()))
                param.put("dbTypeFields[" + key + "].keyword", condition.getKeyword());
            if (StringUtils.isNotEmpty(condition.getCode()))
                param.put("dbTypeFields[" + key + "].code", condition.getCode());
            if (condition.getStartDate() != null)
                param.put("dbTypeFields[" + key + "].startDate", condition.getStartDate().toString());
            if (condition.getEndDate() != null)
                param.put("dbTypeFields[" + key + "].endDate", condition.getEndDate().toString());
        });

        return getList(subUrl + executeId + "/list", param, PdsResearchResultEntity.class).getData();
    }

    public List<HistoryPdsResearchGroupResponse> getExecutingPdsList() throws IOException, ResultFailException {
        return getList(subUrl + "execute-pds-info", null, HistoryPdsResearchGroupResponse.class).getData();
    }
}
