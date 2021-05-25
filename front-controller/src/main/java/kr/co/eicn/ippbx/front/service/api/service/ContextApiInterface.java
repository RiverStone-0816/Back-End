package kr.co.eicn.ippbx.front.service.api.service;

import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.ContextInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.WebVoiceResponse;
import kr.co.eicn.ippbx.model.form.ContextInfoFormRequest;
import kr.co.eicn.ippbx.model.form.WebVoiceItemsFormRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ContextApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(ContextApiInterface.class);
    private static final String subUrl = "/api/v1/admin/service/context/context/";

    public List<ContextInfoResponse> list() throws IOException, ResultFailException {
        return getList(subUrl, null, ContextInfoResponse.class).getData();
    }

    public ContextInfoResponse get(String context) throws IOException, ResultFailException {
        return getData(subUrl + context, null, ContextInfoResponse.class).getData();
    }

    public WebVoiceResponse getWebVoice(String context) throws IOException, ResultFailException {
        return getData(subUrl + context + "/web-voice", null, WebVoiceResponse.class).getData();
    }

    public void post(ContextInfoFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void put(String context, ContextInfoFormRequest form) throws IOException, ResultFailException {
        super.put(subUrl + context, form);
    }

    public void delete(String context) throws IOException, ResultFailException {
        super.delete(subUrl + context);
    }

    public void apply(String context, WebVoiceItemsFormRequest form) throws IOException, ResultFailException {
        post(subUrl + context + "/apply", form);
    }
}
