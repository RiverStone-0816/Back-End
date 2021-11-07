package kr.co.eicn.ippbx.front.service.api.service.etc;

import kr.co.eicn.ippbx.util.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.model.dto.eicn.CidInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.PhoneInfoResponse;
import kr.co.eicn.ippbx.model.dto.eicn.NumberListResponse;
import kr.co.eicn.ippbx.model.form.CidInfoChangeRequest;
import kr.co.eicn.ippbx.model.form.CidInfoUpdateFormRequest;
import kr.co.eicn.ippbx.model.search.CidInfoSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ExtensionExtraInfoApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(ExtensionExtraInfoApiInterface.class);
    private static final String subUrl = "/api/v1/admin/service/etc/extension/";

    public Pagination<PhoneInfoResponse> pagination(CidInfoSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, PhoneInfoResponse.class).getData();
    }

    public void put(CidInfoUpdateFormRequest form) throws IOException, ResultFailException {
        put(subUrl, form);
    }

    public void putCid(CidInfoChangeRequest form) throws IOException, ResultFailException {
        put(subUrl + "cid", form);
    }

    public List<CidInfoResponse> cidList() throws IOException, ResultFailException {
        return getList(subUrl + "cids", null, CidInfoResponse.class).getData();
    }

    public List<NumberListResponse> numberList() throws IOException, ResultFailException {
        return getList(subUrl + "numbers", null, NumberListResponse.class).getData();
    }
}
