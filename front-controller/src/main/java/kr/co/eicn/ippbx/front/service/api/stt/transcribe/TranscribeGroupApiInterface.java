package kr.co.eicn.ippbx.front.service.api.stt.transcribe;

import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.front.model.form.TranscribeFileForm;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.model.dto.eicn.TranscribeGroupResponse;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchPersonListResponse;
import kr.co.eicn.ippbx.model.form.TranscribeGroupFormRequest;
import kr.co.eicn.ippbx.model.search.TranscribeGroupSearchRequest;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.util.page.Pagination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranscribeGroupApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(TranscribeGroupApiInterface.class);
    private static final String subUrl = "/api/v1/admin/stt/transcribe/group/";

    public Pagination<TranscribeGroupResponse> pagination(TranscribeGroupSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, TranscribeGroupResponse.class).getData();
    }

    public TranscribeGroupResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, TranscribeGroupResponse.class).getData();
    }

    public Integer post(TranscribeGroupFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    public void put(Integer seq, TranscribeGroupFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void statusUpdate(Integer seq) throws IOException, ResultFailException {
        put(subUrl + "status/" + seq, null);
    }

    public void upload(Integer seq, TranscribeFileForm form) throws IOException, ResultFailException {
        //sendByMultipartFile(HttpMethod.POST, subUrl + seq + "/fields/by-excel", form, Collections.singletonMap("file", new FileResource(form.getFilePath(), form.getOriginalName())));

        final Map<String, FileResource> files = new HashMap<>();//fileFormList
        if (form.getAddingFiles() != null)
            for (int i = 0; i < form.getAddingFiles().size(); i++)
                files.put("files[" + i + "]", new FileResource(form.getAddingFiles().get(i).getFilePath(), form.getAddingFiles().get(i).getOriginalName()));
        sendByMultipartFile(HttpMethod.POST, subUrl + "/upload/" + seq, form, String.class, files);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }

    public List<SearchPersonListResponse> persons() throws IOException, ResultFailException {
        return getList(subUrl + "person", null, SearchPersonListResponse.class).getData();
    }

    public List<TranscribeGroupResponse> transcribeGroup() throws IOException, ResultFailException {
        return getList(subUrl + "transcribe-group", null, TranscribeGroupResponse.class).getData();
    }

    public void postFieldsByExcel(Integer seq, FileForm form) throws IOException, ResultFailException {
        sendByMultipartFile(HttpMethod.POST, subUrl + seq + "/fields/by-excel", form, Collections.singletonMap("file", new FileResource(form.getFilePath(), form.getOriginalName())));
    }

    public Integer executeStt(Integer fileSeq) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl + fileSeq + "/execute", null, Integer.class, false).getData();
    }
}
