package kr.co.eicn.ippbx.front.service.api.application.maindb;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.ConCodeFieldResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchMaindbGroupResponse;
import kr.co.eicn.ippbx.model.entity.customdb.MaindbCustomInfoEntity;
import kr.co.eicn.ippbx.model.form.MaindbCustomInfoFormRequest;
import kr.co.eicn.ippbx.model.search.MaindbDataSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class MaindbDataApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(MaindbDataApiInterface.class);
    private static final String subUrl = "/api/v1/admin/application/maindb/custominfo/";

    public Pagination<MaindbCustomInfoEntity> getPagination(MaindbDataSearchRequest search) throws IOException, ResultFailException {
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

        return getPagination(subUrl, param, MaindbCustomInfoEntity.class).getData();
    }

    public Pagination<MaindbCustomInfoEntity> getPagination(Integer groupSeq, MaindbDataSearchRequest search) throws IOException, ResultFailException {
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

        return getPagination(subUrl + groupSeq + "/data", param, MaindbCustomInfoEntity.class).getData();
    }

    public List<SearchMaindbGroupResponse> customdbGroup() throws IOException, ResultFailException {
        return getList(subUrl + "customdb_group", null, SearchMaindbGroupResponse.class).getData();
    }

    public List<ConCodeFieldResponse> searchItem() throws IOException, ResultFailException {
        return getList(subUrl + "search_item", null, ConCodeFieldResponse.class).getData();
    }

    public String post(MaindbCustomInfoFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, String.class, false).getData();
    }

    public void put(String id, MaindbCustomInfoFormRequest form) throws IOException, ResultFailException {
        super.put(subUrl + id, form);
    }

    public void deleteData(String id) throws IOException, ResultFailException {
        super.delete(subUrl + id);
    }

    public MaindbCustomInfoEntity get(String id) throws IOException, ResultFailException {
        return getData(subUrl + id, null, MaindbCustomInfoEntity.class).getData();
    }

    public String getCustomName(String phoneNumber) throws IOException, ResultFailException {
        return getData(subUrl + phoneNumber + "/name", null, String.class).getData();
    }
}
