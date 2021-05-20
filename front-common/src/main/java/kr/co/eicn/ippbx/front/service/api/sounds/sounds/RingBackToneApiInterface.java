package kr.co.eicn.ippbx.front.service.api.sounds.sounds;

import kr.co.eicn.ippbx.front.model.form.RingBackToneForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.JsonResult;
import kr.co.eicn.ippbx.util.ReflectionUtils;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.MohDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.MohListSummaryResponse;
import kr.co.eicn.ippbx.model.form.MohListRequest;
import kr.co.eicn.ippbx.model.search.MohListSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;

@Service
public class RingBackToneApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(RingBackToneApiInterface.class);

    private static final String subUrl = "/api/v1/admin/sounds/ring-back-tone/";

    public Pagination<MohListSummaryResponse> pagination(MohListSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, MohListSummaryResponse.class).getData();
    }

    public MohDetailResponse get(String category) throws IOException, ResultFailException {
        return getData(subUrl + category, null, MohDetailResponse.class).getData();
    }

    public String post(RingBackToneForm form) throws IOException, ResultFailException {
        MohListRequest request = new MohListRequest();
        ReflectionUtils.copy(request, form);
        return sendByMultipartFile(HttpMethod.POST, subUrl, request, String.class, Collections.singletonMap("file" , new FileResource(form.getFilePath(),  form.getOriginalName())) );
    }

    public void delete(String category) throws IOException, ResultFailException {
        super.delete(subUrl + category);
    }

    public void updateWebLog(String category, String type) throws IOException, ResultFailException {
        final JsonResult<?> result = call(subUrl + category + "/web-log", Collections.singletonMap("type", type), JsonResult.class, HttpMethod.PUT, true);
        if (result.isFailure())
            throw new ResultFailException(result);
    }
}
