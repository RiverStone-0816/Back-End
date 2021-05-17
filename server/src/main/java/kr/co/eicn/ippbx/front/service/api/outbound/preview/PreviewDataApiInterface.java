package kr.co.eicn.ippbx.front.service.api.outbound.preview;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PrvGroup;
import kr.co.eicn.ippbx.server.model.entity.customdb.PrvCustomInfoEntity;
import kr.co.eicn.ippbx.server.model.form.PrvCustomInfoFormRequest;
import kr.co.eicn.ippbx.server.model.form.PrvCustomInfoRedistributionFormRequest;
import kr.co.eicn.ippbx.server.model.search.PrvCustomInfoSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Service
public class PreviewDataApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(PreviewDataApiInterface.class);
    private static final String subUrl = "/api/v1/admin/outbound/preview/custominfo/";

    public List<PrvGroup> prvGroup() throws IOException, ResultFailException {
        return getList(subUrl + "preview-group", null, PrvGroup.class).getData();
    }

    public Pagination<PrvCustomInfoEntity> getPagination(Integer groupSeq, PrvCustomInfoSearchRequest search) throws IOException, ResultFailException {
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

        return getPagination(subUrl + groupSeq + "/data", param, PrvCustomInfoEntity.class).getData();
    }

    public void redistribution(Integer groupSeq, PrvCustomInfoRedistributionFormRequest form) throws IOException, ResultFailException {
        post(subUrl + groupSeq + "/data/redistribution", form);
    }

    public PrvCustomInfoEntity get(Integer groupSeq, String customId) throws IOException, ResultFailException {
        return getData(subUrl + groupSeq + "/data/" + customId, null, PrvCustomInfoEntity.class).getData();
    }

    public void post(PrvCustomInfoFormRequest form) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, ResultFailException {
        post(subUrl, form);
    }

    public void put(String customId, PrvCustomInfoFormRequest form) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, ResultFailException {
        super.put(subUrl + customId, form);
    }

    public void delete(Integer groupSeq, String customId) throws IOException, ResultFailException {
        delete(subUrl + groupSeq + "/data/" + customId);
    }
}
