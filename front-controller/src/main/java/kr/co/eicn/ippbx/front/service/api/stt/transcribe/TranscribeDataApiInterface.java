package kr.co.eicn.ippbx.front.service.api.stt.transcribe;

import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.TranscribeDataResponse;
import kr.co.eicn.ippbx.model.dto.eicn.TranscribeGroupResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchPersonListResponse;
import kr.co.eicn.ippbx.model.form.TranscribeDataFormRequest;
import kr.co.eicn.ippbx.model.search.TranscribeDataSearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TranscribeDataApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(TranscribeDataApiInterface.class);
    private static final String subUrl = "/api/v1/admin/stt/transcribe/data/";


    public List<TranscribeGroupResponse> list(TranscribeDataSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl, search, TranscribeGroupResponse.class).getData();
    }

    public TranscribeDataResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, TranscribeDataResponse.class).getData();
    }

    public Integer post(TranscribeDataFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    public void updateLearnStatus(Integer seq, String status) throws IOException, ResultFailException {
        put(subUrl + seq + "/" + status, null);
    }

    public List<SearchPersonListResponse> persons() throws IOException, ResultFailException {
        return getList(subUrl + "person", null, SearchPersonListResponse.class).getData();
    }

    public List<TranscribeDataResponse> transcribeGroup() throws IOException, ResultFailException {
        return getList(subUrl + "transcribe-group", null, TranscribeDataResponse.class).getData();
    }

}
