package kr.co.eicn.ippbx.front.service.api.sounds.sounds;

import kr.co.eicn.ippbx.front.model.form.ArsForm;
import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.SoundDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SoundListSummaryResponse;
import kr.co.eicn.ippbx.model.search.SoundListSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class ArsApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(ArsApiInterface.class);
    private static final String subUrl = "/api/v1/admin/sounds/ars/";

    public List<SoundListSummaryResponse> list(SoundListSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl , search, SoundListSummaryResponse.class).getData();
    }

    public Pagination<SoundListSummaryResponse> pagination(SoundListSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl + "search", search, SoundListSummaryResponse.class).getData();
    }

    public SoundDetailResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, SoundDetailResponse.class).getData();
    }

    public Integer post(ArsForm form) throws IOException, ResultFailException {
        return sendByMultipartFile(HttpMethod.POST, subUrl, form, Integer.class, Collections.singletonMap("file", new FileResource(form.getFilePath(), form.getOriginalName())));
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }

    public void updateWebLog(Integer seq, String type) throws IOException, ResultFailException {
        final JsonResult<?> result = call(subUrl + seq + "/web-log", Collections.singletonMap("type", type), JsonResult.class, HttpMethod.PUT, true);
        if (result.isFailure())
            throw new ResultFailException(result);
    }

    public Resource getResource(Integer arsId) throws IOException, ResultFailException {
        return getResource(subUrl + arsId + "/resource");
    }
}
