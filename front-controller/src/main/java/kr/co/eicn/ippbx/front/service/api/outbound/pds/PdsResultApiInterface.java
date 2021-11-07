package kr.co.eicn.ippbx.front.service.api.outbound.pds;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.entity.eicn.ExecutePDSGroupEntity;
import kr.co.eicn.ippbx.model.entity.pds.PDSResultCustomInfoEntity;
import kr.co.eicn.ippbx.model.form.PDSResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.ExecutePDSGroupSearchRequest;
import kr.co.eicn.ippbx.model.search.PDSResultCustomInfoSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class PdsResultApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(PdsResultApiInterface.class);
    private static final String subUrl = "/api/v1/admin/outbound/pds/resultcustominfo/";

    public List<ExecutePDSGroupEntity> getExecutingPdsList(ExecutePDSGroupSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "excutepds_info", search, ExecutePDSGroupEntity.class).getData();
    }

    public Pagination<PDSResultCustomInfoEntity> getPagination(String executeId, PDSResultCustomInfoSearchRequest search) throws IOException, ResultFailException {
        final Map<String, Object> param = objectMapper.convertValue(search, typeFactory.constructParametricType(Map.class, String.class, Object.class));

        // FIXME: extract
        param.remove("dbTypeFields");
        search.getDbTypeFields().forEach((key, condition) -> {
            if (StringUtils.isNotEmpty(condition.getKeyword()))
                param.put("dbTypeFields[" + key + "].keyword", condition.getKeyword());
            if (condition.getStartDate() != null)
                param.put("dbTypeFields[" + key + "].startDate", condition.getStartDate().toString());
            if (condition.getEndDate() != null)
                param.put("dbTypeFields[" + key + "].endDate", condition.getEndDate().toString());
        });

        return getPagination(subUrl + executeId + "/data", param, PDSResultCustomInfoEntity.class).getData();
    }

    public PDSResultCustomInfoEntity get(String executeId, Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + executeId + "/data/" + seq, null, PDSResultCustomInfoEntity.class).getData();
    }

    public void put(String executeId, Integer seq, PDSResultCustomInfoFormRequest form) throws IOException, ResultFailException {
        super.put(subUrl + executeId + "/data/" + seq, form);
    }

    public void delete(String executeId, Integer seq) throws IOException, ResultFailException {
        super.delete(subUrl + executeId + "/data/" + seq);
    }
}
