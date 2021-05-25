package kr.co.eicn.ippbx.front.service.api.outbound.pds;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.entity.pds.PDSCustomInfoEntity;
import kr.co.eicn.ippbx.model.form.PDSCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.PDSDataSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class PdsCustominfoApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(PdsCustominfoApiInterface.class);
    private static final String subUrl = "/api/v1/admin/outbound/pds/custominfo/";

    public Pagination<PDSCustomInfoEntity> getPagination(Integer groupSeq, PDSDataSearchRequest search) throws IOException, ResultFailException {
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

        return getPagination(subUrl + groupSeq + "/data", param, PDSCustomInfoEntity.class).getData();
    }

    public void post(PDSCustomInfoFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void put(String id, PDSCustomInfoFormRequest form) throws IOException, ResultFailException {
        super.put(subUrl + id, form);
    }

    public void deleteData(Integer groupSeq, String id) throws IOException, ResultFailException {
        super.delete(subUrl + "?groupSeq=" + groupSeq + "&id=" + id);
    }

    public PDSCustomInfoEntity get(Integer groupSeq, String id) throws IOException, ResultFailException {
        return getData(subUrl + groupSeq + "/data/" + id, null, PDSCustomInfoEntity.class).getData();
    }
}
