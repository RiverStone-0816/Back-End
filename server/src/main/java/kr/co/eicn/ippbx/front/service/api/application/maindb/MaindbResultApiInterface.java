package kr.co.eicn.ippbx.front.service.api.application.maindb;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.model.RecordFile;
import kr.co.eicn.ippbx.server.model.dto.eicn.search.SearchMaindbGroupResponse;
import kr.co.eicn.ippbx.server.model.entity.customdb.ResultCustomInfoEntity;
import kr.co.eicn.ippbx.server.model.form.ResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.server.model.search.ResultCustomInfoSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Service
public class MaindbResultApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(MaindbResultApiInterface.class);
    private static final String subUrl = "/api/v1/admin/application/maindb/resultcustominfo/";

    public List<SearchMaindbGroupResponse> customdbGroup() throws IOException, ResultFailException {
        return getList(subUrl + "customdb_group", null, SearchMaindbGroupResponse.class).getData();
    }

    public Pagination<ResultCustomInfoEntity> getPagination(ResultCustomInfoSearchRequest search) throws IOException, ResultFailException {
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

        return getPagination(subUrl, param, ResultCustomInfoEntity.class).getData();
    }

    public Pagination<ResultCustomInfoEntity> getPagination(Integer groupSeq, ResultCustomInfoSearchRequest search) throws IOException, ResultFailException {
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

        return getPagination(subUrl + groupSeq + "/data", param, ResultCustomInfoEntity.class).getData();
    }

    public ResultCustomInfoEntity get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, ResultCustomInfoEntity.class).getData();
    }

    public List<ResultCustomInfoEntity> getTodo(String userId, String phone) throws IOException, ResultFailException {
        return getList(subUrl + userId +"/"+ phone, null, ResultCustomInfoEntity.class).getData();
    }

    public void post(ResultCustomInfoFormRequest form) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, ResultFailException {
        post(subUrl, form);
    }

    public void put(Integer seq, ResultCustomInfoFormRequest form) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void deleteData(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }

    public List<RecordFile> getFiles(String uniqueId) throws IOException, ResultFailException {
        return getList(subUrl + uniqueId + "/record-files", null, RecordFile.class).getData();
    }
}
