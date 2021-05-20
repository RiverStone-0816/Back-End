package kr.co.eicn.ippbx.front.service.api.application.code;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.ConCodeResponse;
import kr.co.eicn.ippbx.model.dto.eicn.ConGroupResponse;
import kr.co.eicn.ippbx.model.form.ConCodeFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EnumerationValueMappingApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(EnumerationValueMappingApiInterface.class);
    private static final String subUrl = "/api/v1/admin/application/code/con/";

    public List<ConCodeResponse> list() throws IOException, ResultFailException {
        return getList(subUrl, null, ConCodeResponse.class).getData();
    }

    public void put(Integer type, String fieldId, ConCodeFormRequest form) throws IOException, ResultFailException {
        super.put(subUrl + "type/" + type + "/field/" + fieldId, form);
    }

    public List<ConGroupResponse> getConGroupList() throws IOException, ResultFailException {
        return getList(subUrl + "group", null, ConGroupResponse.class).getData();
    }
}
