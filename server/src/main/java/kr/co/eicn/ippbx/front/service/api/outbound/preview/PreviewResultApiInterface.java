package kr.co.eicn.ippbx.front.service.api.outbound.preview;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.front.util.page.Pagination;
import kr.co.eicn.ippbx.server.jooq.eicn.tables.pojos.PrvGroup;
import kr.co.eicn.ippbx.server.model.entity.customdb.PrvResultCustomInfoEntity;
import kr.co.eicn.ippbx.server.model.form.ResultCustomInfoFormRequest;
import kr.co.eicn.ippbx.server.model.form.PrvCustomInfoRedistributionFormRequest;
import kr.co.eicn.ippbx.server.model.search.PrvResultCustomInfoSearchRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Service
public class PreviewResultApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(PreviewResultApiInterface.class);
    private static final String subUrl = "/api/v1/admin/outbound/preview/resultcustominfo/";

    public Pagination<PrvResultCustomInfoEntity> getPagination(Integer groupSeq, PrvResultCustomInfoSearchRequest search) throws IOException, ResultFailException {
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

        return getPagination(subUrl + groupSeq + "/data", param, PrvResultCustomInfoEntity.class).getData();
    }
 
    public PrvResultCustomInfoEntity get(Integer groupSeq, Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + groupSeq + "/data/" + seq, null, PrvResultCustomInfoEntity.class).getData();
    }

    public void post(ResultCustomInfoFormRequest form) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, ResultFailException {
        post(subUrl, form);
    }

    public void put(Integer seq, ResultCustomInfoFormRequest form) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, IOException, ResultFailException {
        super.put(subUrl + seq, form);
    }

    public void delete(Integer groupSeq, Integer seq) throws IOException, ResultFailException {
        delete(subUrl + groupSeq + "/data/" + seq);
    }
}
