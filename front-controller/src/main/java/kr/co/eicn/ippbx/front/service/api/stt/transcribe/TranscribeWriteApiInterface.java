package kr.co.eicn.ippbx.front.service.api.stt.transcribe;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.TranscribeWriteResponse;
import kr.co.eicn.ippbx.model.form.TranscribeWriteFormRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TranscribeWriteApiInterface extends ApiServerInterface {
    private static final String subUrl = "/api/v1/admin/stt/transcribe/write/";

    public TranscribeWriteResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, TranscribeWriteResponse.class).getData();
    }

    public String update(Integer seq, TranscribeWriteFormRequest request) throws IOException, ResultFailException {
        return getData(HttpMethod.PUT, subUrl + seq, request, String.class, false).getData();
    }

    public Resource getResources(Integer fileSeq) throws IOException, ResultFailException {
        return getResourceResponse(subUrl + fileSeq + "/resource");
    }

}
