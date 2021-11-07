package kr.co.eicn.ippbx.front.service.api.outbound.preview;

import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.*;
import kr.co.eicn.ippbx.model.dto.eicn.search.SearchPersonListResponse;
import kr.co.eicn.ippbx.model.form.PrvGroupFormRequest;
import kr.co.eicn.ippbx.model.search.PrvGroupSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class PreviewGroupApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(PreviewGroupApiInterface.class);
    private static final String subUrl = "/api/v1/admin/outbound/preview/group/";

    public Pagination<PrvGroupSummaryResponse> pagination(PrvGroupSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, PrvGroupSummaryResponse.class).getData();
    }

    public PrvGroupDetailResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, PrvGroupDetailResponse.class).getData();
    }

    public Integer post(PrvGroupFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, Integer.class, false).getData();
    }

    public void put(Integer seq, PrvGroupFormRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }

    public List<CommonTypeResponse> prvType() throws IOException, ResultFailException {
        return getList(subUrl + "preview-type", null, CommonTypeResponse.class).getData();
    }

    public List<CommonTypeResponse> resultType() throws IOException, ResultFailException {
        return getList(subUrl + "result-type", null, CommonTypeResponse.class).getData();
    }

    public List<PrvGroupNumber070Response> prvNumber() throws IOException, ResultFailException {
        return getList(subUrl + "preview-number", null, PrvGroupNumber070Response.class).getData();
    }

    public List<SearchPersonListResponse> persons() throws IOException, ResultFailException {
        return getList(subUrl + "person", null, SearchPersonListResponse.class).getData();
    }

    public List<PrvGroupResponse> prvGroup() throws IOException, ResultFailException {
        return getList(subUrl + "preview-group", null, PrvGroupResponse.class).getData();
    }

    public void postFieldsByExcel(Integer seq, FileForm form) throws IOException, ResultFailException {
        sendByMultipartFile(HttpMethod.POST, subUrl + seq + "/fields/by-excel", form, Collections.singletonMap("file", new FileResource(form.getFilePath(), form.getOriginalName())));
    }
}
