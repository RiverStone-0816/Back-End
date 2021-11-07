package kr.co.eicn.ippbx.front.service.api.application.sms;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.SendSmsCategoryDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.SendSmsCategorySummaryResponse;
import kr.co.eicn.ippbx.model.form.SendCategoryUpdateRequest;
import kr.co.eicn.ippbx.model.form.SendSmsCategoryFormRequest;
import kr.co.eicn.ippbx.model.search.SendCategorySearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class SmsCategoryApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(SmsCategoryApiInterface.class);

    private static final String subUrl = "/api/v1/admin/application/sms/category/";

    public Pagination<SendSmsCategoryDetailResponse> pagination(SendCategorySearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, SendSmsCategoryDetailResponse.class).getData();
    }

    public SendSmsCategoryDetailResponse get(String categoryCode) throws IOException, ResultFailException {
        return getData(subUrl + categoryCode, null, SendSmsCategoryDetailResponse.class).getData();
    }

    public String post(SendSmsCategoryFormRequest form) throws IOException, ResultFailException {
        return getData(HttpMethod.POST, subUrl, form, String.class, false).getData();
    }

    public void put(String categoryCode, SendCategoryUpdateRequest form) throws IOException, ResultFailException {
        super.put(subUrl + categoryCode, form);
    }

    public void delete(String categoryCode) throws IOException, ResultFailException {
        super.delete(subUrl + categoryCode);
    }

    public List<SendSmsCategorySummaryResponse> sendCategory() throws IOException, ResultFailException {
        return getList(subUrl + "category", null, SendSmsCategorySummaryResponse.class).getData();
    }
}
