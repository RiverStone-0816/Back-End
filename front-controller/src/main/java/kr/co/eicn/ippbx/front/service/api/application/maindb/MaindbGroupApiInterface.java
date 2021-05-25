package kr.co.eicn.ippbx.front.service.api.application.maindb;

import kr.co.eicn.ippbx.front.model.form.FileForm;
import kr.co.eicn.ippbx.front.service.ResultFailException;
import kr.co.eicn.ippbx.front.service.api.ApiServerInterface;
import kr.co.eicn.ippbx.util.page.Pagination;
import kr.co.eicn.ippbx.meta.jooq.eicn.tables.pojos.CommonField;
import kr.co.eicn.ippbx.model.dto.eicn.CommonTypeResponse;
import kr.co.eicn.ippbx.model.dto.eicn.MaindbGroupDetailResponse;
import kr.co.eicn.ippbx.model.dto.eicn.MaindbGroupSummaryResponse;
import kr.co.eicn.ippbx.model.form.MaindbGroupFormRequest;
import kr.co.eicn.ippbx.model.form.MaindbGroupUpdateRequest;
import kr.co.eicn.ippbx.model.search.MaindbGroupSearchRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
public class MaindbGroupApiInterface extends ApiServerInterface {
    private static final Logger logger = LoggerFactory.getLogger(MaindbGroupApiInterface.class);
    private static final String subUrl = "/api/v1/admin/application/maindb/group/";

    public List<MaindbGroupSummaryResponse> list(MaindbGroupSearchRequest search) throws IOException, ResultFailException {
        return getList(subUrl + "search", search, MaindbGroupSummaryResponse.class).getData();
    }

    public Pagination<MaindbGroupSummaryResponse> pagination(MaindbGroupSearchRequest search) throws IOException, ResultFailException {
        return getPagination(subUrl, search, MaindbGroupSummaryResponse.class).getData();
    }

    public MaindbGroupDetailResponse get(Integer seq) throws IOException, ResultFailException {
        return getData(subUrl + seq, null, MaindbGroupDetailResponse.class).getData();
    }

    public void post(MaindbGroupFormRequest form) throws IOException, ResultFailException {
        post(subUrl, form);
    }

    public void put(Integer seq, MaindbGroupUpdateRequest form) throws IOException, ResultFailException {
        put(subUrl + seq, form);
    }

    public void delete(Integer seq) throws IOException, ResultFailException {
        delete(subUrl + seq);
    }

    public List<CommonTypeResponse> maindbType() throws IOException, ResultFailException {
        return getList(subUrl + "maindb-type", null, CommonTypeResponse.class).getData();
    }

    public List<CommonField> maindbField() throws IOException, ResultFailException {
        return getList(subUrl + "maindb-field", null, CommonField.class).getData();
    }

    public List<CommonTypeResponse> resultType() throws IOException, ResultFailException {
        return getList(subUrl + "result-type", null, CommonTypeResponse.class).getData();
    }

    public void postFieldsByExcel(Integer seq, FileForm form) throws IOException, ResultFailException {
        sendByMultipartFile(HttpMethod.POST, subUrl + seq + "/fields/by-excel", form, Collections.singletonMap("file", new FileResource(form.getFilePath(), form.getOriginalName())));
    }
}
