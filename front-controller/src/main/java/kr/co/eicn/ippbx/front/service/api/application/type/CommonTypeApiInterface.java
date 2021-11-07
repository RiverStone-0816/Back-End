package kr.co.eicn.ippbx.front.service.api.application.type;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonBasicField;
import kr.co.eicn.ippbx.model.entity.eicn.CommonTypeEntity;
import kr.co.eicn.ippbx.model.form.CommonTypeFormRequest;
import kr.co.eicn.ippbx.model.form.CommonTypeUpdateFormRequest;
import kr.co.eicn.ippbx.util.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class CommonTypeApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(CommonTypeApiInterface.class);
    private static final String subUrl = "/api/v1/admin/application/type/";

    public List<CommonTypeEntity> list(String kind) throws IOException, ResultFailException {
        return getList(subUrl + "?kind=" + kind, null, CommonTypeEntity.class).getData();
    }

    public CommonTypeEntity get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, CommonTypeEntity.class).getData();
    }

    public void post(CommonTypeFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void put(Integer seq, CommonTypeUpdateFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void updateStatus(Integer seq) throws IOException, ResultFailException {
        patch(subUrl + seq, null);
    }

    public void move(Integer seq, List<Integer> sequences) throws IOException, ResultFailException {
        final JsonResult<?> result = call(subUrl + seq + "/move", Collections.singletonMap("sequences", sequences), JsonResult.class, HttpMethod.PUT, true);
        if (result.isFailure())
            throw new ResultFailException(result);
    }

    public List<CommonBasicField> getBasicFields(String kind) throws IOException, ResultFailException {
        return getList(subUrl + "basic-field?kind=" + kind, null, CommonBasicField.class).getData();
    }
}
