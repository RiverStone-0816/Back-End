package kr.co.eicn.ippbx.front.service.api.application.code;

import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.CommonFieldResponse;
import kr.co.eicn.ippbx.model.dto.eicn.CommonTypeDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.RelatedFieldResponse;
import kr.co.eicn.ippbx.model.form.CommonCodeUpdateFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class EnumerationValueApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(EnumerationValueApiInterface.class);
    private static final String subUrl = "/api/v1/admin/application/code/multi/";

    public List<CommonTypeDetailResponse> list() throws IOException, ResultFailException {
        return getList(subUrl, null, CommonTypeDetailResponse.class).getData();
    }

    public void put(Integer type, String fieldId, CommonCodeUpdateFormRequest form) throws IOException, ResultFailException {
        put(subUrl + "type/" + type + "/field/" + fieldId + "/code", form);
    }

    public List<RelatedFieldResponse> getRelatedField(Integer type, String fieldId) throws IOException, ResultFailException {
        return getList(subUrl + "type/" + type + "/field/" + fieldId + "/related", null, RelatedFieldResponse.class).getData();
    }

    public void postFieldsByExcel(Integer type, String fieldId, FileForm form) throws IOException, ResultFailException {
        sendByMultipartFile(HttpMethod.POST, subUrl + type + "/" + fieldId + "/codes/by-excel", form, Collections.singletonMap("file", new FileResource(form.getFilePath(), form.getOriginalName())));
    }

    public CommonFieldResponse getField(Integer type, String fieldId) throws IOException, ResultFailException {
        return getData(subUrl + "type/" + type + "/field/" + fieldId, null, CommonFieldResponse.class).getData();
    }
}
